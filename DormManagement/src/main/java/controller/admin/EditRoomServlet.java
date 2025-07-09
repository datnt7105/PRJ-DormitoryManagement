/*
 * Click nbsp://netbeans.org/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbsp://netbeans.org/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entity.Room;
import model.entity.Building;
import model.dao.BuildingDAO;
import model.service.RoomService;
import model.dao.DBContext;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet to handle editing a room.
 * @author HP
 */
public class EditRoomServlet extends HttpServlet {

    private RoomService roomService;
    private BuildingDAO buildingDAO;

    @Override
    public void init() {
        roomService = new RoomService();
        try {
            DBContext dbContext = DBContext.getInstance();
            String imagePath = getServletContext().getRealPath("/images");
            if (imagePath == null) {
                imagePath = ""; // Gán giá trị mặc định nếu null
            }
            buildingDAO = new BuildingDAO(dbContext.getConnection(), imagePath); // Dòng 32
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize BuildingDAO: " + e.getMessage(), e);
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method to display the edit room form.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int roomId = Integer.parseInt(request.getParameter("id"));
            Room room = roomService.getRoomById(roomId);
            if (room != null) {
                List<Building> buildings = buildingDAO.getAllBuildings();
                request.setAttribute("room", room);
                request.setAttribute("buildings", buildings);
                String error = request.getParameter("error");
                if (error != null) {
                    request.setAttribute("error", error);
                }
                request.getRequestDispatcher("/view/admin/EditRoom.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/ViewRoomServlet?error=" 
                    + java.net.URLEncoder.encode("Không tìm thấy phòng!", "UTF-8"));
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/ViewRoomServlet?error=" 
                + java.net.URLEncoder.encode("ID phòng không hợp lệ!", "UTF-8"));
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/ViewRoomServlet?error=" 
                + java.net.URLEncoder.encode("Lỗi khi tải dữ liệu: " + e.getMessage(), "UTF-8"));
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method to update a room.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            Room room = new Room();
            room.setRoomId(roomId);
            room.setRoomNumber(request.getParameter("roomNumber"));
            room.setBuildingId(Integer.parseInt(request.getParameter("buildingId")));
            room.setFloor(Integer.parseInt(request.getParameter("floor")));
            room.setOccupancy(Integer.parseInt(request.getParameter("occupancy")));
            room.setCurrentOccupants(Integer.parseInt(request.getParameter("currentOccupants")));
            room.setPrice(Double.parseDouble(request.getParameter("price")));
            room.setStatus(request.getParameter("status"));

            roomService.updateRoom(room);
            response.sendRedirect(request.getContextPath() + "/ViewRoomServlet?success=" 
                + java.net.URLEncoder.encode("Cập nhật phòng " + room.getRoomNumber() + " thành công!", "UTF-8"));
        } catch (SQLException e) {
            String errorMessage = e.getMessage().contains("FOREIGN KEY") 
                ? "ID tòa nhà không hợp lệ."
                : e.getMessage();
            response.sendRedirect(request.getContextPath() + "/EditRoomServlet?id=" + request.getParameter("roomId") + "&error=" 
                + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/EditRoomServlet?id=" + request.getParameter("roomId") + "&error=" 
                + java.net.URLEncoder.encode("Dữ liệu số không hợp lệ!", "UTF-8"));
        }
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet to handle editing a room in the admin dashboard.";
    }
}