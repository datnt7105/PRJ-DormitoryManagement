package model.service;

import model.dao.BuildingDAO;
import model.entity.Building;
import java.sql.SQLException;
import java.util.List;

public class BuildingService {
    private BuildingDAO dao = new BuildingDAO();

    public List<Building> listAll() {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy danh sách Building", e);
        }
    }

    public Building findById(int id) {
        try {
            return dao.getById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi tìm Building id=" + id, e);
        }
    }

    public void create(Building b) {
        try {
            dao.insert(b);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi thêm Building", e);
        }
    }

    public void edit(Building b) {
        try {
            dao.update(b);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật Building", e);
        }
    }

    public void remove(int id) {
        try {
            dao.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi xóa Building", e);
        }
    }
}