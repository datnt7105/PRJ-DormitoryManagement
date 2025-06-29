package model.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.entity.Building;

public class BuildingDAO {

    private Connection conn;
    private String imageBasePath; // Đường dẫn gốc để xóa file (truyền từ BuildingController)

    public BuildingDAO(Connection conn, String imageBasePath) {
        this.conn = conn;
        this.imageBasePath = imageBasePath; // Ví dụ: /path/to/webapp/images
    }

    public List<Building> getAllBuildings() throws SQLException {
        List<Building> buildings = new ArrayList<>();
        String sql = "SELECT BuildingID, BuildingName, AdminID, Floors, Status, ImageUrl FROM Building";
        try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Building building = new Building(
                        rs.getInt("BuildingID"),
                        rs.getString("BuildingName"),
                        rs.getInt("AdminID"),
                        rs.getInt("Floors"),
                        rs.getString("Status"),
                        rs.getString("ImageUrl")
                );
                buildings.add(building);
            }
        }
        return buildings;
    }

    public Building getBuildingById(int buildingID) throws SQLException {
        String sql = "SELECT BuildingID, BuildingName, AdminID, Floors, Status, ImageUrl FROM Building WHERE BuildingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, buildingID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Building(
                            rs.getInt("BuildingID"),
                            rs.getString("BuildingName"),
                            rs.getInt("AdminID"),
                            rs.getInt("Floors"),
                            rs.getString("Status"),
                            rs.getString("ImageUrl")
                    );
                }
            }
        }
        return null;
    }

    public boolean addBuilding(Building building) throws SQLException {
        String sql = "INSERT INTO Building (BuildingName, AdminID, Floors, Status, ImageUrl) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, building.getBuildingName());
            pstmt.setInt(2, building.getAdminID());
            pstmt.setInt(3, building.getNumberFloors());
            pstmt.setString(4, building.getStatus());
            pstmt.setString(5, building.getImageUrl());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateBuilding(Building building) throws SQLException {
        String sql = "UPDATE Building SET BuildingName = ?, AdminID = ?, Floors = ?, Status = ?, ImageUrl = ? WHERE BuildingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, building.getBuildingName());
            pstmt.setInt(2, building.getAdminID());
            pstmt.setInt(3, building.getNumberFloors());
            pstmt.setString(4, building.getStatus());
            pstmt.setString(5, building.getImageUrl());
            pstmt.setInt(6, building.getBuildingID());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteBuilding(int buildingID) throws SQLException {
        // Lấy thông tin tòa nhà để xóa file hình ảnh
        Building building = getBuildingById(buildingID);
        if (building != null && building.getImageUrl() != null && !building.getImageUrl().isEmpty()) {
            // Xóa file hình ảnh từ thư mục
            String filePath = imageBasePath + building.getImageUrl().substring(building.getImageUrl().lastIndexOf("/"));
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }

        // Xóa bản ghi trong cơ sở dữ liệu
        String sql = "DELETE FROM Building WHERE BuildingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, buildingID);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean isBuildingNameExist(String buildingName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Building WHERE BuildingName = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, buildingName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean hasRelatedRooms(int buildingID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Room WHERE BuildingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, buildingID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}