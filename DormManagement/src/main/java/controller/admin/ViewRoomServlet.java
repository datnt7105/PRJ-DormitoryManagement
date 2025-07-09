package controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.BuildingDAO;
import model.dao.DBContext;
import model.entity.Building;
import model.entity.Room;
import model.service.BuildingService;
import model.service.RoomService;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ViewRoomServlet extends HttpServlet {

    private RoomService roomService;
    private BuildingService buildingService;

    @Override
    public void init() {
        roomService = new RoomService();
        try {
            DBContext dbContext = new DBContext();
            Connection conn = dbContext.getConnection();
            String imagePath = getServletContext().getRealPath("/images");
            BuildingDAO buildingDAO = new BuildingDAO(conn, imagePath);
            buildingService = new BuildingService(buildingDAO);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize services: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Room> rooms = roomService.getAllRooms();
            List<Building> buildings = buildingService.getAllBuildings();
            request.setAttribute("rooms", rooms);
            request.setAttribute("buildings", buildings);
            request.getRequestDispatcher("/view/admin/dashboard.jsp?tab=rooms").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/ViewRoomServlet?error=" 
                + java.net.URLEncoder.encode("Lỗi khi tải dữ liệu: " + e.getMessage(), "UTF-8"));
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet to display the list of rooms in the admin dashboard.";
    }
}