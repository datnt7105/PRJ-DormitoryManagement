package controller.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.RoomDAO;
import model.dao.BuildingDAO;
import model.dao.DBContext;
import model.entity.Room;
import model.entity.RoomType;
import model.entity.Building;

@WebServlet(name = "RoomController", urlPatterns = {"/RoomController"})
public class RoomController extends HttpServlet {

    private RoomDAO roomDAO;
    private DBContext dbContext;

    @Override
    public void init() throws ServletException {
        roomDAO = new RoomDAO();
        dbContext = DBContext.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            switch (action != null ? action : "list") {
                case "list":
                    listRooms(request, response);
                    break;
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteRoom(request, response);
                    break;
                default:
                    listRooms(request, response);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/view/admin/dashboard.jsp?tab=rooms").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            switch (action != null ? action : "") {
                case "insert":
                    insertRoom(request, response);
                    break;
                case "update":
                    updateRoom(request, response);
                    break;
                default:
                    listRooms(request, response);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/view/admin/dashboard.jsp?tab=rooms").forward(request, response);
        }
    }

    private void listRooms(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Room> rooms = roomDAO.getAllRooms();
        request.setAttribute("rooms", rooms);
        request.getRequestDispatcher("/view/admin/dashboard.jsp?tab=rooms").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            // Load data cho form
            List<RoomType> roomTypes = roomDAO.getAllRoomTypes();
            
            // Khởi tạo BuildingDAO với connection và imagePath
            String imagePath = request.getServletContext().getRealPath("/images");
            BuildingDAO buildingDAO = new BuildingDAO(dbContext.getConnection(), imagePath);
            List<Building> buildings = buildingDAO.getAllBuildings();
            
            request.setAttribute("roomTypes", roomTypes);
            request.setAttribute("buildings", buildings);
            request.setAttribute("action", "add");
            request.getRequestDispatcher("/view/admin/AddRoom.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải dữ liệu: " + e.getMessage());
            listRooms(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int roomId = Integer.parseInt(request.getParameter("id"));
            Room room = roomDAO.getRoomById(roomId);
            
            if (room != null) {
                List<RoomType> roomTypes = roomDAO.getAllRoomTypes();
                
                // Khởi tạo BuildingDAO với connection và imagePath
                String imagePath = request.getServletContext().getRealPath("/images");
                BuildingDAO buildingDAO = new BuildingDAO(dbContext.getConnection(), imagePath);
                List<Building> buildings = buildingDAO.getAllBuildings();
                
                request.setAttribute("room", room);
                request.setAttribute("roomTypes", roomTypes);
                request.setAttribute("buildings", buildings);
                request.setAttribute("action", "edit");
                request.getRequestDispatcher("/view/admin/EditRoom.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Không tìm thấy phòng!");
                listRooms(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID phòng không hợp lệ!");
            listRooms(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải dữ liệu: " + e.getMessage());
            listRooms(request, response);
        }
    }

    private void insertRoom(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            String roomNumber = request.getParameter("roomNumber");
            int roomTypeId = Integer.parseInt(request.getParameter("roomTypeId"));
            int buildingId = Integer.parseInt(request.getParameter("buildingId"));
            int floor = Integer.parseInt(request.getParameter("floor"));
            String status = request.getParameter("status");

            // Validate input
            if (roomNumber == null || roomNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Số phòng không được để trống");
            }

            // Kiểm tra trùng lặp số phòng
            if (roomDAO.isRoomNumberExists(roomNumber, buildingId, 0)) {
                response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&error=" 
                    + java.net.URLEncoder.encode("Số phòng " + roomNumber + " đã tồn tại trong tòa nhà này!", "UTF-8"));
                return;
            }

            Room room = new Room();
            room.setRoomNumber(roomNumber.trim());
            room.setRoomTypeId(roomTypeId);
            room.setBuildingId(buildingId);
            room.setFloor(floor);
            room.setStatus(status);

            if (roomDAO.insertRoom(room)) {
                response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&success=" 
                    + java.net.URLEncoder.encode("Thêm phòng " + roomNumber + " thành công!", "UTF-8"));
            } else {
                response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&error=" 
                    + java.net.URLEncoder.encode("Không thể thêm phòng do lỗi hệ thống!", "UTF-8"));
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&error=" 
                + java.net.URLEncoder.encode("Dữ liệu số không hợp lệ!", "UTF-8"));
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&error=" 
                + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&error=" 
                + java.net.URLEncoder.encode("Lỗi hệ thống: " + e.getMessage(), "UTF-8"));
        }
    }

    private void updateRoom(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            String roomNumber = request.getParameter("roomNumber");
            int roomTypeId = Integer.parseInt(request.getParameter("roomTypeId"));
            int buildingId = Integer.parseInt(request.getParameter("buildingId"));
            int floor = Integer.parseInt(request.getParameter("floor"));
            String status = request.getParameter("status");

            // Validate input
            if (roomNumber == null || roomNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Số phòng không được để trống");
            }

            // Kiểm tra trùng lặp số phòng (trừ chính nó)
            if (roomDAO.isRoomNumberExists(roomNumber, buildingId, roomId)) {
                response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&error=" 
                    + java.net.URLEncoder.encode("Số phòng " + roomNumber + " đã tồn tại trong tòa nhà này!", "UTF-8"));
                return;
            }

            Room room = new Room();
            room.setRoomId(roomId);
            room.setRoomNumber(roomNumber.trim());
            room.setRoomTypeId(roomTypeId);
            room.setBuildingId(buildingId);
            room.setFloor(floor);
            room.setStatus(status);

            if (roomDAO.updateRoom(room)) {
                response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&success=" 
                    + java.net.URLEncoder.encode("Cập nhật phòng " + roomNumber + " thành công!", "UTF-8"));
            } else {
                response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&error=" 
                    + java.net.URLEncoder.encode("Không thể cập nhật phòng do lỗi hệ thống!", "UTF-8"));
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&error=" 
                + java.net.URLEncoder.encode("Dữ liệu số không hợp lệ!", "UTF-8"));
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&error=" 
                + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&error=" 
                + java.net.URLEncoder.encode("Lỗi hệ thống: " + e.getMessage(), "UTF-8"));
        }
    }

    private void deleteRoom(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int roomId = Integer.parseInt(request.getParameter("id"));
            
            // Lấy thông tin phòng trước khi xóa để hiển thị thông báo
            Room room = roomDAO.getRoomById(roomId);
            String roomNumber = (room != null) ? room.getRoomNumber() : "";
            
            if (roomDAO.deleteRoom(roomId)) {
                response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&success=" 
                    + java.net.URLEncoder.encode("Xóa phòng " + roomNumber + " thành công!", "UTF-8"));
            } else {
                response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&error=" 
                    + java.net.URLEncoder.encode("Không thể xóa phòng " + roomNumber + "! Phòng có thể đang được sử dụng.", "UTF-8"));
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&error=" 
                + java.net.URLEncoder.encode("ID phòng không hợp lệ!", "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp?tab=rooms&error=" 
                + java.net.URLEncoder.encode("Lỗi hệ thống: " + e.getMessage(), "UTF-8"));
        }
    }
}
