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
    private String imageBasePath;

    public BuildingDAO(Connection conn, String imageBasePath) {
        this.conn = conn;
        this.imageBasePath = imageBasePath;
    }

    public List<Building> getAllBuildings() throws SQLException {
        List<Building> buildings = new ArrayList<>();
        String sql = "SELECT BuildingID, BuildingName, AdminID, Floors, Status, ImageUrl FROM Building";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
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
        Building building = getBuildingById(buildingID);
        if (building != null && building.getImageUrl() != null && !building.getImageUrl().isEmpty()) {
            // Lấy phần tên file từ ImageUrl (giả sử ImageUrl là /images/filename.jpg)
            String fileName = building.getImageUrl().substring(building.getImageUrl().lastIndexOf("/") + 1);
            String filePath = imageBasePath + File.separator + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }

        String sql = "DELETE FROM Building WHERE BuildingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, buildingID);
            return pstmt.executeUpdate() > 0;
        }
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