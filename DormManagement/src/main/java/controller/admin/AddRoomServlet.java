package controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.BuildingDAO;
import model.dao.DBContext;
import model.entity.Building;
import model.entity.Room;
import model.service.RoomService;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AddRoomServlet extends HttpServlet {

    private RoomService roomService;
    private BuildingDAO buildingDAO;

    @Override
    public void init() {
        roomService = new RoomService();
        try {
            DBContext dbContext = new DBContext();
            Connection conn = dbContext.getConnection();
            String imagePath = getServletContext().getRealPath("/images");
            buildingDAO = new BuildingDAO(conn, imagePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize BuildingDAO: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Building> buildings = buildingDAO.getAllBuildings();
            request.setAttribute("buildings", buildings);
            String error = request.getParameter("error");
            if (error != null) {
                request.setAttribute("error", java.net.URLDecoder.decode(error, "UTF-8"));
            }
            request.getRequestDispatcher("/view/admin/AddRoom.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/AddRoomServlet?error=" 
                + java.net.URLEncoder.encode("Lỗi khi tải dữ liệu tòa nhà: " + e.getMessage(), "UTF-8"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Room room = new Room();
            room.setRoomNumber(request.getParameter("roomNumber"));
            room.setBuildingId(Integer.parseInt(request.getParameter("buildingId")));
            room.setFloor(Integer.parseInt(request.getParameter("floor")));
            room.setOccupancy(Integer.parseInt(request.getParameter("occupancy")));
            room.setCurrentOccupants(Integer.parseInt(request.getParameter("currentOccupants")));
            room.setPrice(Double.parseDouble(request.getParameter("price")));
            room.setStatus(request.getParameter("status"));

            roomService.addRoom(room);
            response.sendRedirect(request.getContextPath() + "/ViewRoomServlet?success=" 
                + java.net.URLEncoder.encode("Thêm phòng " + room.getRoomNumber() + " thành công!", "UTF-8"));
        } catch (SQLException e) {
            String errorMessage = e.getMessage().contains("FOREIGN KEY") 
                ? "ID tòa nhà không hợp lệ."
                : e.getMessage();
            response.sendRedirect(request.getContextPath() + "/AddRoomServlet?error=" 
                + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/AddRoomServlet?error=" 
                + java.net.URLEncoder.encode("Dữ liệu số không hợp lệ!", "UTF-8"));
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet to handle adding a new room in the admin dashboard.";
    }
}