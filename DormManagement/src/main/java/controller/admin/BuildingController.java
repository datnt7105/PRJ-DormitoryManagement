package controller.admin;

import model.dao.BuildingDAO;
import model.entity.Building;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.DBContext;

@WebServlet(name = "BuildingController", urlPatterns = {"/BuildingController"})
public class BuildingController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Integer adminId = (Integer) session.getAttribute("adminId");
        
        if (adminId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp"); // Chuyển hướng nếu không phải admin
            return;
        }

        
        try (Connection conn = new DBContext().getConnection()) {
            BuildingDAO buildingDAO = new BuildingDAO(conn);
            String action = request.getParameter("action");
            // Debug log: kiểm tra giá trị action
            System.out.println("BuildingController.action = " + action);

            // 1. List (mặc định hoặc action=list)
            if (action == null || "list".equals(action)) {
                List<Building> buildings = buildingDAO.getAllBuildings();
                request.setAttribute("buildings", buildings);
                String jspPath = "/view/admin/Building.jsp";
                System.out.println("Forwarding LIST to: " + jspPath + " -> real: " + request.getServletContext().getRealPath(jspPath));
                request.getRequestDispatcher(jspPath).forward(request, response);
                return;
            }

            // 2. Thêm tòa nhà
            if ("add".equals(action)) {
                String buildingName = request.getParameter("buildingName");
                int adminID = Integer.parseInt(request.getParameter("adminID"));
                int numberFloors = Integer.parseInt(request.getParameter("numberFloors"));
                String status = request.getParameter("status");
                String imageUrl = request.getParameter("imageUrl");
                Building building = new Building(0, buildingName, adminID, numberFloors, status, imageUrl);
                buildingDAO.addBuilding(building);
                response.sendRedirect(request.getContextPath() + "/BuildingController?action=list");
                return;
            }

            // 3. Sửa tòa nhà
            if ("edit".equals(action)) {
                int buildingID = Integer.parseInt(request.getParameter("buildingID"));
                String buildingName = request.getParameter("buildingName");
                int adminID = Integer.parseInt(request.getParameter("adminID"));
                int numberFloors = Integer.parseInt(request.getParameter("numberFloors"));
                String status = request.getParameter("status");
                String imageUrl = request.getParameter("imageUrl");
                Building building = new Building(buildingID, buildingName, adminID, numberFloors, status, imageUrl);
                buildingDAO.updateBuilding(building);
                response.sendRedirect(request.getContextPath() + "/BuildingController?action=list");
                return;
            }

            // 4. Xóa tòa nhà
            if ("delete".equals(action)) {
                int buildingID = Integer.parseInt(request.getParameter("buildingID"));
                buildingDAO.deleteBuilding(buildingID);
                response.sendRedirect(request.getContextPath() + "/BuildingController?action=list");
                return;
            }

            // 5. Xem chi tiết tòa nhà
            if ("view".equals(action)) {
                int buildingID;
                try {
                    buildingID = Integer.parseInt(request.getParameter("buildingID"));
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/BuildingController?action=list");
                    return;
                }
                Building building = buildingDAO.getBuildingById(buildingID);
                if (building == null) {
                    response.sendRedirect(request.getContextPath() + "/BuildingController?action=list");
                    return;
                }
                request.setAttribute(   "building", building);
                String jspDetail = "/view/admin/BuildingDetailed.jsp";
                System.out.println("Forwarding VIEW to: " + jspDetail + " -> real: " + request.getServletContext().getRealPath(jspDetail));
                request.getRequestDispatcher(jspDetail).forward(request, response);
                return;
            }

            // 6. Hiển thị form thêm
            if ("showAddForm".equals(action)) {
                String jspForm = "/view/admin/AddBuilding.jsp";
                System.out.println("Forwarding FORM to: " + jspForm + " -> real: " + request.getServletContext().getRealPath(jspForm));
                request.getRequestDispatcher(jspForm).forward(request, response);
                return;
            }

            // Action không hợp lệ -> redirect về list
            response.sendRedirect(request.getContextPath() + "/BuildingController?action=list");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
