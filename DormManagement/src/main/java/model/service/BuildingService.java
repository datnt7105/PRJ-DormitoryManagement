package model.service;

import model.dao.BuildingDAO;
import model.entity.Building;
import java.sql.SQLException;
import java.util.List;

public class BuildingService {
    private BuildingDAO buildingDAO;

    public BuildingService(BuildingDAO buildingDAO) {
        this.buildingDAO = buildingDAO;
    }

    public List<Building> getAllBuildings() throws SQLException {
        return buildingDAO.getAllBuildings();
    }
}