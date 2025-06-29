package controller.admin;

import model.dao.BuildingDAO;
import model.entity.Building;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.dao.DBContext;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10, // 10MB
    maxRequestSize = 1024 * 1024 * 50 // 50MB
)
@WebServlet(name = "BuildingController", urlPatterns = {"/BuildingController"})
public class BuildingController extends HttpServlet {
    private BuildingDAO buildingDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = new DBContext().getConnection();
            String imageBasePath = getServletContext().getRealPath("/images");
            buildingDAO = new BuildingDAO(conn, imageBasePath);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize database connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer adminId = session == null ? null : (Integer) session.getAttribute("adminId");
        System.out.println("BuildingController: Session = " + (session == null ? "null" : session.getId()) + ", adminId = " + adminId);
        if (adminId == null || adminId <= 0) {
            System.out.println("BuildingController: Redirecting to login.jsp because adminId is invalid");
            response.sendRedirect(request.getContextPath() + "/view/common/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    List<Building> buildings = buildingDAO.getAllBuildings();
                    request.setAttribute("buildings", buildings);
                    request.getRequestDispatcher("/view/admin/Building.jsp").forward(request, response);
                    break;
                case "showAddForm":
                    request.getRequestDispatcher("/view/admin/AddBuilding.jsp").forward(request, response);
                    break;
                case "view":
                    int viewId = Integer.parseInt(request.getParameter("buildingID"));
                    Building viewBuilding = buildingDAO.getBuildingById(viewId);
                    if (viewBuilding != null) {
                        request.setAttribute("building", viewBuilding);
                        request.getRequestDispatcher("/view/admin/BuildingDetailed.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error", "Không tìm thấy tòa nhà.");
                        request.getRequestDispatcher("/view/admin/Building.jsp").forward(request, response);
                    }
                    break;
                case "edit":
                    int editId = Integer.parseInt(request.getParameter("buildingID"));
                    Building editBuilding = buildingDAO.getBuildingById(editId);
                    if (editBuilding != null) {
                        request.setAttribute("building", editBuilding);
                        request.getRequestDispatcher("/view/admin/EditBuilding.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error", "Không tìm thấy tòa nhà để chỉnh sửa.");
                        request.getRequestDispatcher("/view/admin/Building.jsp").forward(request, response);
                    }
                    break;
                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("buildingID"));
                    if (buildingDAO.hasRelatedRooms(deleteId)) {
                        request.setAttribute("error", "Không thể xóa tòa nhà vì có phòng liên quan.");
                    } else if (buildingDAO.deleteBuilding(deleteId)) {
                        request.getSession().setAttribute("successMessage", "Xóa tòa nhà thành công!");
                    } else {
                        request.setAttribute("error", "Lỗi khi xóa tòa nhà.");
                    }
                    response.sendRedirect(request.getContextPath() + "/BuildingController?action=list");
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/BuildingController?action=list");
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer adminId = session == null ? null : (Integer) session.getAttribute("adminId");
        System.out.println("BuildingController: Session = " + (session == null ? "null" : session.getId()) + ", adminId = " + adminId);
        if (adminId == null || adminId <= 0) {
            System.out.println("BuildingController: Redirecting to login.jsp because adminId is invalid");
            response.sendRedirect(request.getContextPath() + "/view/common/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                String buildingName = request.getParameter("buildingName");
                int numberFloors = Integer.parseInt(request.getParameter("numberFloors"));
                String status = request.getParameter("status");
                
                Part filePart = request.getPart("imageFile");
                String imageUrl = null;
                if (filePart != null && filePart.getSize() > 0) {
                    String fileName = extractFileName(filePart);
                    if (!fileName.toLowerCase().endsWith(".jpg") && !fileName.toLowerCase().endsWith(".png") && !fileName.toLowerCase().endsWith(".webp")) {
                        request.setAttribute("error", "Chỉ hỗ trợ file JPG, PNG, hoặc WEBP.");
                        request.getRequestDispatcher("/view/admin/AddBuilding.jsp").forward(request, response);
                        return;
                    }
                    String savePath = getServletContext().getRealPath("/images") + File.separator + fileName;
                    filePart.write(savePath);
                    imageUrl = request.getContextPath() + "/images/" + fileName;
                }

                Building building = new Building();
                building.setBuildingName(buildingName);
                building.setNumberFloors(numberFloors);
                building.setStatus(status);
                building.setImageUrl(imageUrl);
                building.setAdminID(adminId);

                if (buildingDAO.addBuilding(building)) {
                    request.getSession().setAttribute("successMessage", "Thêm tòa nhà thành công!");
                    response.sendRedirect(request.getContextPath() + "/BuildingController?action=list");
                } else {
                    request.setAttribute("error", "Lỗi khi thêm tòa nhà.");
                    request.getRequestDispatcher("/view/admin/AddBuilding.jsp").forward(request, response);
                }
            } else if ("update".equals(action)) {
                int buildingID = Integer.parseInt(request.getParameter("buildingID"));
                String buildingName = request.getParameter("buildingName");
                int numberFloors = Integer.parseInt(request.getParameter("numberFloors"));
                String status = request.getParameter("status");

                Part filePart = request.getPart("imageFile");
                String imageUrl = null;
                if (filePart != null && filePart.getSize() > 0) {
                    String fileName = extractFileName(filePart);
                    if (!fileName.toLowerCase().endsWith(".jpg") && !fileName.toLowerCase().endsWith(".png") && !fileName.toLowerCase().endsWith(".webp")) {
                        request.setAttribute("error", "Chỉ hỗ trợ file JPG, PNG, hoặc WEBP.");
                        Building existingBuilding = buildingDAO.getBuildingById(buildingID);
                        request.setAttribute("building", existingBuilding);
                        request.getRequestDispatcher("/view/admin/EditBuilding.jsp").forward(request, response);
                        return;
                    }
                    String savePath = getServletContext().getRealPath("/images") + File.separator + fileName;
                    filePart.write(savePath);
                    imageUrl = request.getContextPath() + "/images/" + fileName;
                } else {
                    Building existingBuilding = buildingDAO.getBuildingById(buildingID);
                    imageUrl = existingBuilding.getImageUrl();
                }

                Building building = new Building();
                building.setBuildingID(buildingID);
                building.setBuildingName(buildingName);
                building.setNumberFloors(numberFloors);
                building.setStatus(status);
                building.setImageUrl(imageUrl);
                building.setAdminID(adminId);

                if (buildingDAO.updateBuilding(building)) {
                    request.getSession().setAttribute("successMessage", "Cập nhật tòa nhà thành công!");
                    response.sendRedirect(request.getContextPath() + "/BuildingController?action=list");
                } else {
                    request.setAttribute("error", "Lỗi khi cập nhật tòa nhà.");
                    request.setAttribute("building", building);
                    request.getRequestDispatcher("/view/admin/EditBuilding.jsp").forward(request, response);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }
}