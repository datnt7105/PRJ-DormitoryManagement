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
import java.util.logging.Level;
import java.util.logging.Logger;
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
            throw new ServletException("Không thể khởi tạo kết nối cơ sở dữ liệu", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer adminId = (session != null) ? (Integer) session.getAttribute("adminId") : null;

        if (adminId == null || adminId <= 0) {
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
                    request.setAttribute("buildings", buildings != null ? buildings : List.of());
                    request.setAttribute("tab", "building");
                    request.getRequestDispatcher("/view/admin/dashboard.jsp").forward(request, response);
                    break;

                case "showAddForm":
                    request.getRequestDispatcher("/view/admin/AddBuilding.jsp").forward(request, response);
                    break;

                case "view":
                    handleViewOrEdit(request, response, true);
                    break;

                case "edit":
                    handleViewOrEdit(request, response, false);
                    break;

                case "delete":
                    handleDelete(request, response);
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/BuildingController?action=list");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            request.setAttribute("showBuildings", true);
            try {
                List<Building> buildings = buildingDAO.getAllBuildings();
                request.setAttribute("buildings", buildings != null ? buildings : List.of());
            } catch (SQLException ex) {
                Logger.getLogger(BuildingController.class.getName()).log(Level.SEVERE, null, ex);
            }
            request.getRequestDispatcher("/view/admin/dashboard.jsp").forward(request, response);
        }
    }

    private void handleViewOrEdit(HttpServletRequest request, HttpServletResponse response, boolean isView) throws ServletException, IOException, SQLException {
        int buildingId = Integer.parseInt(request.getParameter("buildingID"));
        Building building = buildingDAO.getBuildingById(buildingId);
        if (building != null) {
            request.setAttribute("building", building);
            String path = isView ? "/view/admin/BuildingDetailed.jsp" : "/view/admin/EditBuilding.jsp";
            request.getRequestDispatcher(path).forward(request, response);
        } else {
            request.setAttribute("error", isView ? "Không tìm thấy tòa nhà." : "Không tìm thấy tòa nhà để chỉnh sửa.");
            request.setAttribute("showBuildings", true);
            List<Building> buildings = buildingDAO.getAllBuildings();
            request.setAttribute("buildings", buildings != null ? buildings : List.of());
            request.getRequestDispatcher("/view/admin/dashboard.jsp").forward(request, response);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        int deleteId = Integer.parseInt(request.getParameter("buildingID"));
        if (buildingDAO.hasRelatedRooms(deleteId)) {
            request.setAttribute("error", "Không thể xóa tòa nhà vì có phòng liên quan.");
        } else if (buildingDAO.deleteBuilding(deleteId)) {
            request.getSession().setAttribute("successMessage", "Xóa tòa nhà thành công!");
        } else {
            request.setAttribute("error", "Lỗi khi xóa tòa nhà.");
        }
        List<Building> buildings = buildingDAO.getAllBuildings();
        request.setAttribute("buildings", buildings != null ? buildings : List.of());
        request.setAttribute("showBuildings", true);
        request.getRequestDispatcher("/view/admin/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer adminId = (session != null) ? (Integer) session.getAttribute("adminId") : null;

        if (adminId == null || adminId <= 0) {
            response.sendRedirect(request.getContextPath() + "/view/common/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("add".equals(action)) {
                handleAddOrUpdate(request, response, adminId, true);
            } else if ("update".equals(action)) {
                handleAddOrUpdate(request, response, adminId, false);
            }
        } catch (Exception e) {
            String jsp = ("add".equals(action)) ? "/view/admin/AddBuilding.jsp" : "/view/admin/EditBuilding.jsp";
            request.setAttribute("error", "Lỗi xử lý dữ liệu: " + e.getMessage());
            request.getRequestDispatcher(jsp).forward(request, response);
        }
    }

    private void handleAddOrUpdate(HttpServletRequest request, HttpServletResponse response, int adminId, boolean isAdd) throws Exception {
        String buildingName = request.getParameter("buildingName");
        int floors = Integer.parseInt(request.getParameter("floors"));
        String status = request.getParameter("status");

        Part filePart = request.getPart("imageFile");
        String imageUrl = null;
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = extractFileName(filePart);
            if (!fileName.toLowerCase().matches(".*\\.(jpg|png|webp)")) {
                request.setAttribute("error", "Chỉ hỗ trợ file JPG, PNG, hoặc WEBP.");
                String jsp = isAdd ? "/view/admin/AddBuilding.jsp" : "/view/admin/EditBuilding.jsp";
                request.getRequestDispatcher(jsp).forward(request, response);
                return;
            }
            String savePath = getServletContext().getRealPath("/images") + File.separator + fileName;
            filePart.write(savePath);
            imageUrl = request.getContextPath() + "/images/" + fileName;
        }

        Building building = new Building();
        building.setBuildingName(buildingName);
        building.setFloors(floors);
        building.setStatus(status);
        building.setImageUrl(imageUrl);
        building.setAdminID(adminId);

        if (!isAdd) {
            int buildingID = Integer.parseInt(request.getParameter("buildingID"));
            building.setBuildingID(buildingID);
            if (imageUrl == null) {
                Building existing = buildingDAO.getBuildingById(buildingID);
                building.setImageUrl(existing != null ? existing.getImageUrl() : null);
            }
        }

        boolean success = isAdd ? buildingDAO.addBuilding(building) : buildingDAO.updateBuilding(building);
        if (success) {
            String msg = isAdd ? "Thêm tòa nhà thành công!" : "Cập nhật tòa nhà thành công!";
            request.getSession().setAttribute("successMessage", msg);
            response.sendRedirect(request.getContextPath() + "/BuildingController?action=list");
        } else {
            request.setAttribute("error", isAdd ? "Lỗi khi thêm tòa nhà." : "Lỗi khi cập nhật tòa nhà.");
            String jsp = isAdd ? "/view/admin/AddBuilding.jsp" : "/view/admin/EditBuilding.jsp";
            request.setAttribute("building", building);
            request.getRequestDispatcher(jsp).forward(request, response);
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp == null) {
            return "";
        }
        for (String s : contentDisp.split(";")) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }
}
