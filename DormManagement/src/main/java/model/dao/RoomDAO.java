package model.dao;

import model.entity.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private DBContext dbContext;

    public RoomDAO() {
        this.dbContext = DBContext.getInstance();
    }

    /**
     * Thêm phòng mới
     */
    public boolean addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO Room (RoomNumber, BuildingID, Floor, Occupancy, current_occupants, Price, Status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, room.getRoomNumber());
            stmt.setInt(2, room.getBuildingId());
            stmt.setInt(3, room.getFloor());
            stmt.setInt(4, room.getOccupancy());
            stmt.setInt(5, room.getCurrentOccupants());
            stmt.setDouble(6, room.getPrice());
            stmt.setString(7, room.getStatus());
            
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Lấy tất cả phòng với thông tin tòa nhà
     */
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.RoomID, r.RoomNumber, r.BuildingID, r.Floor, r.Occupancy, " +
                     "r.current_occupants, r.Price, r.Status, b.BuildingName " +
                     "FROM Room r " +
                     "LEFT JOIN Building b ON r.BuildingID = b.BuildingID " +
                     "ORDER BY b.BuildingName, r.Floor, r.RoomNumber";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("RoomID"));
                room.setRoomNumber(rs.getString("RoomNumber"));
                room.setBuildingId(rs.getInt("BuildingID"));
                room.setFloor(rs.getInt("Floor"));
                room.setOccupancy(rs.getInt("Occupancy"));
                room.setCurrentOccupants(rs.getInt("current_occupants"));
                room.setPrice(rs.getDouble("Price"));
                room.setStatus(rs.getString("Status"));
                room.setBuildingName(rs.getString("BuildingName"));
                rooms.add(room);
            }
        }
        return rooms;
    }

    /**
     * Lấy phòng theo ID
     */
    public Room getRoomById(int roomId) throws SQLException {
        String sql = "SELECT r.RoomID, r.RoomNumber, r.BuildingID, r.Floor, r.Occupancy, " +
                     "r.current_occupants, r.Price, r.Status, b.BuildingName " +
                     "FROM Room r " +
                     "LEFT JOIN Building b ON r.BuildingID = b.BuildingID " +
                     "WHERE r.RoomID = ?";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Room room = new Room();
                    room.setRoomId(rs.getInt("RoomID"));
                    room.setRoomNumber(rs.getString("RoomNumber"));
                    room.setBuildingId(rs.getInt("BuildingID"));
                    room.setFloor(rs.getInt("Floor"));
                    room.setOccupancy(rs.getInt("Occupancy"));
                    room.setCurrentOccupants(rs.getInt("current_occupants"));
                    room.setPrice(rs.getDouble("Price"));
                    room.setStatus(rs.getString("Status"));
                    room.setBuildingName(rs.getString("BuildingName"));
                    return room;
                }
            }
        }
        return null;
    }

    /**
     * Cập nhật phòng
     */
    public boolean updateRoom(Room room) throws SQLException {
        String sql = "UPDATE Room SET RoomNumber = ?, BuildingID = ?, Floor = ?, Occupancy = ?, " +
                     "current_occupants = ?, Price = ?, Status = ? WHERE RoomID = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, room.getRoomNumber());
            stmt.setInt(2, room.getBuildingId());
            stmt.setInt(3, room.getFloor());
            stmt.setInt(4, room.getOccupancy());
            stmt.setInt(5, room.getCurrentOccupants());
            stmt.setDouble(6, room.getPrice());
            stmt.setString(7, room.getStatus());
            stmt.setInt(8, room.getRoomId());
            
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Xóa phòng
     */
    public boolean deleteRoom(int roomId) throws SQLException {
        String sql = "DELETE FROM Room WHERE RoomID = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Kiểm tra số phòng đã tồn tại trong tòa nhà chưa
     */
    public boolean isRoomNumberExists(String roomNumber, int buildingId, int excludeRoomId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Room WHERE RoomNumber = ? AND BuildingID = ? AND RoomID != ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, roomNumber);
            stmt.setInt(2, buildingId);
            stmt.setInt(3, excludeRoomId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /**
     * Lấy phòng theo tòa nhà
     */
    public List<Room> getRoomsByBuildingId(int buildingId) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.RoomID, r.RoomNumber, r.BuildingID, r.Floor, r.Occupancy, " +
                     "r.current_occupants, r.Price, r.Status, b.BuildingName " +
                     "FROM Room r " +
                     "LEFT JOIN Building b ON r.BuildingID = b.BuildingID " +
                     "WHERE r.BuildingID = ? " +
                     "ORDER BY r.Floor, r.RoomNumber";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, buildingId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room();
                    room.setRoomId(rs.getInt("RoomID"));
                    room.setRoomNumber(rs.getString("RoomNumber"));
                    room.setBuildingId(rs.getInt("BuildingID"));
                    room.setFloor(rs.getInt("Floor"));
                    room.setOccupancy(rs.getInt("Occupancy"));
                    room.setCurrentOccupants(rs.getInt("current_occupants"));
                    room.setPrice(rs.getDouble("Price"));
                    room.setStatus(rs.getString("Status"));
                    room.setBuildingName(rs.getString("BuildingName"));
                    rooms.add(room);
                }
            }
        }
        return rooms;
    }
    
    
      public List<Room> searchRooms(String keyword, int minOccupancy, int maxOccupancy, String status) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.*, b.BuildingName FROM Room r LEFT JOIN Building b ON r.BuildingID = b.BuildingID " +
                     "WHERE (CAST(r.RoomID AS CHAR) LIKE ? OR r.RoomNumber LIKE ? OR b.BuildingName LIKE ?) " +
                     "AND r.Occupancy BETWEEN ? AND ?" +
                     (status != null && !status.equals("all") ? " AND r.Status = ?" : "") +
                     " ORDER BY r.Floor, r.RoomNumber";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String key = "%" + keyword + "%";
            stmt.setString(1, key);
            stmt.setString(2, key);
            stmt.setString(3, key);
            stmt.setInt(4, minOccupancy);
            stmt.setInt(5, maxOccupancy);
            if (status != null && !status.equals("all")) {
                stmt.setString(6, status);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room();
                    room.setRoomId(rs.getInt("RoomID"));
                    room.setRoomNumber(rs.getString("RoomNumber"));
                    room.setBuildingId(rs.getInt("BuildingID"));
                    room.setFloor(rs.getInt("Floor"));
                    room.setOccupancy(rs.getInt("Occupancy"));
                    room.setCurrentOccupants(rs.getInt("current_occupants"));
                    room.setPrice(rs.getDouble("Price"));
                    room.setStatus(rs.getString("Status"));
                    room.setBuildingName(rs.getString("BuildingName"));
                    rooms.add(room);
                }
            }
        }
        return rooms;
    }
}