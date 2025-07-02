package model.dao;

import model.entity.Room;
import model.entity.RoomType;
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
     * Lấy tất cả phòng với thông tin liên kết
     */
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.RoomID, r.RoomNumber, r.RoomTypeID, r.BuildingID, r.Floor, r.Status, " +
                     "rt.RoomTypeName, rt.Occupancy, rt.Price, " +
                     "b.BuildingName " +
                     "FROM Room r " +
                     "LEFT JOIN RoomType rt ON r.RoomTypeID = rt.RoomTypeID " +
                     "LEFT JOIN Building b ON r.BuildingID = b.BuildingID " +
                     "ORDER BY b.BuildingName, r.Floor, r.RoomNumber";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("RoomID"));
                room.setRoomNumber(rs.getString("RoomNumber"));
                room.setRoomTypeId(rs.getInt("RoomTypeID"));
                room.setBuildingId(rs.getInt("BuildingID"));
                room.setFloor(rs.getInt("Floor"));
                room.setStatus(rs.getString("Status"));
                room.setRoomTypeName(rs.getString("RoomTypeName"));
                room.setOccupancy(rs.getInt("Occupancy"));
                room.setPrice(rs.getDouble("Price"));
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
        String sql = "SELECT r.RoomID, r.RoomNumber, r.RoomTypeID, r.BuildingID, r.Floor, r.Status, " +
                     "rt.RoomTypeName, rt.Occupancy, rt.Price, " +
                     "b.BuildingName " +
                     "FROM Room r " +
                     "LEFT JOIN RoomType rt ON r.RoomTypeID = rt.RoomTypeID " +
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
                    room.setRoomTypeId(rs.getInt("RoomTypeID"));
                    room.setBuildingId(rs.getInt("BuildingID"));
                    room.setFloor(rs.getInt("Floor"));
                    room.setStatus(rs.getString("Status"));
                    room.setRoomTypeName(rs.getString("RoomTypeName"));
                    room.setOccupancy(rs.getInt("Occupancy"));
                    room.setPrice(rs.getDouble("Price"));
                    room.setBuildingName(rs.getString("BuildingName"));
                    return room;
                }
            }
        }
        return null;
    }

    /**
     * Thêm phòng mới
     */
    public boolean insertRoom(Room room) throws SQLException {
        String sql = "INSERT INTO Room (RoomNumber, RoomTypeID, BuildingID, Floor, Status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, room.getRoomNumber());
            stmt.setInt(2, room.getRoomTypeId());
            stmt.setInt(3, room.getBuildingId());
            stmt.setInt(4, room.getFloor());
            stmt.setString(5, room.getStatus());
            
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật phòng
     */
    public boolean updateRoom(Room room) throws SQLException {
        String sql = "UPDATE Room SET RoomNumber = ?, RoomTypeID = ?, BuildingID = ?, Floor = ?, Status = ? WHERE RoomID = ?";
        
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, room.getRoomNumber());
            stmt.setInt(2, room.getRoomTypeId());
            stmt.setInt(3, room.getBuildingId());
            stmt.setInt(4, room.getFloor());
            stmt.setString(5, room.getStatus());
            stmt.setInt(6, room.getRoomId());
            
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
     * Lấy tất cả loại phòng
     */
    public List<RoomType> getAllRoomTypes() throws SQLException {
        List<RoomType> roomTypes = new ArrayList<>();
        String sql = "SELECT RoomTypeID, RoomTypeName, Occupancy, Price FROM RoomType ORDER BY RoomTypeName";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RoomType roomType = new RoomType();
                roomType.setRoomTypeId(rs.getInt("RoomTypeID"));
                roomType.setRoomTypeName(rs.getString("RoomTypeName"));
                roomType.setOccupancy(rs.getInt("Occupancy"));
                roomType.setPrice(rs.getDouble("Price"));
                roomTypes.add(roomType);
            }
        }
        return roomTypes;
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
        String sql = "SELECT r.RoomID, r.RoomNumber, r.RoomTypeID, r.BuildingID, r.Floor, r.Status, " +
                     "rt.RoomTypeName, rt.Occupancy, rt.Price, " +
                     "b.BuildingName " +
                     "FROM Room r " +
                     "LEFT JOIN RoomType rt ON r.RoomTypeID = rt.RoomTypeID " +
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
                    room.setRoomTypeId(rs.getInt("RoomTypeID"));
                    room.setBuildingId(rs.getInt("BuildingID"));
                    room.setFloor(rs.getInt("Floor"));
                    room.setStatus(rs.getString("Status"));
                    room.setRoomTypeName(rs.getString("RoomTypeName"));
                    room.setOccupancy(rs.getInt("Occupancy"));
                    room.setPrice(rs.getDouble("Price"));
                    room.setBuildingName(rs.getString("BuildingName"));
                    rooms.add(room);
                }
            }
        }
        return rooms;
    }
}
