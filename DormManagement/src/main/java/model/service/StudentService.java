package model.service;

import model.dao.StudentDAO;
import model.entity.Students;
import java.util.List;

public class StudentService {
    private StudentDAO studentDAO = new StudentDAO();

    public List<Students> getAll() {
        return studentDAO.getAll();
    }

    public Students getById(int id) {
        return studentDAO.getById(id);
    }

    public boolean add(Students s) {
        java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
        s.setCreatedAt(now);
        s.setUpdatedAt(now);
        s.setIsApproved(0); // Đảm bảo sinh viên mới luôn ở trạng thái chờ duyệt
        return studentDAO.add(s);
    }

    public boolean update(Students s) {
        return studentDAO.update(s);
    }

    public boolean delete(int id) {
        return studentDAO.delete(id);
    }
} 