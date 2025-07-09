package model.service;

import model.dao.RoomDAO;
import model.entity.Room;
import java.sql.SQLException;
import java.util.List;

public class RoomService {
    private RoomDAO roomDAO;

    public RoomService() {
        this.roomDAO = new RoomDAO();
    }

    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAllRooms();
    }

    public Room getRoomById(int roomId) throws SQLException {
        return roomDAO.getRoomById(roomId);
    }

    public boolean addRoom(Room room) throws SQLException {
        if (room == null) {
            throw new SQLException("Đối tượng phòng không được để trống.");
        }
        // Kiểm tra dữ liệu đầu vào
        if (room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty()) {
            throw new SQLException("Số phòng không được để trống.");
        }
        // Kiểm tra số phòng đã tồn tại trong tòa nhà
        if (roomDAO.isRoomNumberExists(room.getRoomNumber(), room.getBuildingId(), 0)) {
            throw new SQLException("Số phòng đã tồn tại trong tòa nhà này.");
        }
        // Kiểm tra Occupancy hợp lệ
        if (room.getOccupancy() != 4 && room.getOccupancy() != 6 && room.getOccupancy() != 8) {
            throw new SQLException("Số lượng người tối đa phải là 4, 6 hoặc 8.");
        }
        // Kiểm tra CurrentOccupants
        if (room.getCurrentOccupants() < 0 || room.getCurrentOccupants() > room.getOccupancy()) {
            throw new SQLException("Số người hiện tại phải từ 0 đến số lượng tối đa.");
        }
        // Kiểm tra Status hợp lệ
        if (!List.of("Available", "Occupied", "Maintenance").contains(room.getStatus())) {
            throw new SQLException("Trạng thái không hợp lệ. Phải là 'Available', 'Occupied' hoặc 'Maintenance'.");
        }
        return roomDAO.addRoom(room);
    }

    public boolean updateRoom(Room room) throws SQLException {
        if (room == null) {
            throw new SQLException("Đối tượng phòng không được để trống.");
        }
        // Kiểm tra số phòng đã tồn tại trong tòa nhà
        if (roomDAO.isRoomNumberExists(room.getRoomNumber(), room.getBuildingId(), room.getRoomId())) {
            throw new SQLException("Số phòng đã tồn tại trong tòa nhà này.");
        }
        // Kiểm tra Occupancy hợp lệ
        if (room.getOccupancy() != 4 && room.getOccupancy() != 6 && room.getOccupancy() != 8) {
            throw new SQLException("Số lượng người tối đa phải là 4, 6 hoặc 8.");
        }
        // Kiểm tra CurrentOccupants
        if (room.getCurrentOccupants() < 0 || room.getCurrentOccupants() > room.getOccupancy()) {
            throw new SQLException("Số người hiện tại phải từ 0 đến số lượng tối đa.");
        }
        // Kiểm tra Status hợp lệ
        if (!List.of("Available", "Occupied", "Maintenance").contains(room.getStatus())) {
            throw new SQLException("Trạng thái không hợp lệ. Phải là 'Available', 'Occupied' hoặc 'Maintenance'.");
        }
        return roomDAO.updateRoom(room);
    }

    public boolean deleteRoom(int roomId) throws SQLException {
        return roomDAO.deleteRoom(roomId);
    }

    public List<Room> getRoomsByBuildingId(int buildingId) throws SQLException {
        return roomDAO.getRoomsByBuildingId(buildingId);
    }
    
       public List<Room> searchRooms(String keyword, int minOccupancy, int maxOccupancy, String status) throws SQLException {
        if (minOccupancy < 0 || maxOccupancy < 0) {
            throw new IllegalArgumentException("Sức chứa không thể âm.");
        }
        if (minOccupancy > maxOccupancy) {
            throw new IllegalArgumentException("Sức chứa tối thiểu phải nhỏ hơn hoặc bằng tối đa.");
        }
        return roomDAO.searchRooms(keyword != null ? keyword.trim() : "", minOccupancy, maxOccupancy, status);
    }

}