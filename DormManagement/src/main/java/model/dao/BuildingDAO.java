package model.dao;

import model.entity.Building;
import model.dao.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BuildingDAO {

    /**
     * Lấy danh sách tất cả Building
     */
    public List<Building> getAll() throws SQLException {
        String sql = "SELECT BuildingID, BuildingName, AdminID FROM Building";
        List<Building> list = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Building b = new Building();
                b.setBuildingID(rs.getInt("BuildingID"));
                b.setBuildingName(rs.getString("BuildingName"));
                b.setAdminID(rs.getInt("AdminID"));
                list.add(b);
            }
        }
        return list;
    }

    /**
     * Lấy Building theo ID
     */
    public Building getById(int id) throws SQLException {
        String sql = "SELECT BuildingID, BuildingName, AdminID FROM Building WHERE BuildingID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Building b = new Building();
                    b.setBuildingID(rs.getInt("BuildingID"));
                    b.setBuildingName(rs.getString("BuildingName"));
                    b.setAdminID(rs.getInt("AdminID"));
                    return b;
                }
            }
        }
        return null;
    }

    /**
     * Thêm mới Building và cập nhật lại ID sinh tự động cho model
     */
    public void insert(Building b) throws SQLException {
        String sql = "INSERT INTO Building (BuildingName, AdminID) VALUES (?, ?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.getBuildingName());
            ps.setInt(2, b.getAdminID());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    b.setBuildingID(keys.getInt(1));
                }
            }
        }
    }

    /**
     * Cập nhật thông tin Building theo ID
     */
    public void update(Building b) throws SQLException {
        String sql = "UPDATE Building SET BuildingName = ?, AdminID = ? WHERE BuildingID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getBuildingName());
            ps.setInt(2, b.getAdminID());
            ps.setInt(3, b.getBuildingID());
            ps.executeUpdate();
        }
    }

    /**
     * Xóa Building theo ID
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Building WHERE BuildingID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
