
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.entity.Building;

public class BuildingDAO {
    private Connection conn;

    public BuildingDAO(Connection conn) {
        this.conn = conn;
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

    public void addBuilding(Building building) throws SQLException {
        String sql = "INSERT INTO Building (BuildingName, AdminID, Floors, Status, ImageUrl) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, building.getBuildingName());
            pstmt.setInt(2, building.getAdminID());
            pstmt.setInt(3, building.getNumberFloors()); // Sử dụng getter numberFloors
            pstmt.setString(4, building.getStatus());
            pstmt.setString(5, building.getImageUrl());
            pstmt.executeUpdate();
        }
    }

    public void updateBuilding(Building building) throws SQLException {
        String sql = "UPDATE Building SET BuildingName = ?, AdminID = ?, Floors = ?, Status = ?, ImageUrl = ? WHERE BuildingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, building.getBuildingName());
            pstmt.setInt(2, building.getAdminID());
            pstmt.setInt(3, building.getNumberFloors()); // Sử dụng getter numberFloors
            pstmt.setString(4, building.getStatus());
            pstmt.setString(5, building.getImageUrl());
            pstmt.setInt(6, building.getBuildingID());
            pstmt.executeUpdate();
        }
    }

    public void deleteBuilding(int buildingID) throws SQLException {
        String sql = "DELETE FROM Building WHERE BuildingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, buildingID);
            pstmt.executeUpdate();
        }
    }
}
