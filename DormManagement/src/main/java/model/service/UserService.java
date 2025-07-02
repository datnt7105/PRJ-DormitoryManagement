package model.service;

import model.dao.StudentDAO;
import model.entity.Students;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Calendar;

public class UserService {
    private StudentDAO studentDAO;
    
    public UserService() {
        studentDAO = new StudentDAO();
    }
    
    public boolean registerUser(String username, String email, String password, String fullName, String cccd, String phone, String gender, String address, Date dob) throws SQLException {
        // Kiểm tra xem username, email hoặc phone đã tồn tại chưa
        if (studentDAO.checkUserExists(username, email) || studentDAO.isPhoneExists(phone)) {
            return false;
        }
        
        // Tạo đối tượng Students mới
        Students student = new Students();
        student.setUsername(username);
        student.setEmail(email);
        student.setPassword(password);
        student.setFullName(fullName);
        student.setPhone(phone);
        
        // Set các trường mặc định cho các trường không bắt buộc
        student.setDob(dob);
        student.setGender(gender);
        student.setAddress(address);
        // Set ngày tạo
        Calendar cal = Calendar.getInstance();
        student.setCreatedAt(new Date(cal.getTimeInMillis()));
        student.setUpdatedAt(new Date(cal.getTimeInMillis()));
        
        // Set trạng thái mặc định
        student.setStatusRoom("Active");
        
        // Lưu vào database
        return studentDAO.insertStudent(student);
    }
    
    public boolean checkEmailFormat(String email) {
        // Kiểm tra định dạng email
        if (email == null || !email.contains("@")) {
            return false;
        }
        
        // Kiểm tra email phải kết thúc bằng @gmail.com hoặc @fpt.edu.vn
        return email.endsWith("@gmail.com") || email.endsWith("@fpt.edu.vn");
    }
    public boolean validatePhone(String phone) {
        // Kiểm tra rỗng
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        // Regex kiểm tra số điện thoại Việt Nam (10 chữ số, bắt đầu bằng 03, 05, 07, 08, 09)
        return phone.matches("^(03|05|07|08|09)\\d{8}$");
    }
    
    public boolean validateCCCD(String cccd) {
        // Kiểm tra CCCD (phải có đúng 12 số)
        if (cccd == null || cccd.trim().isEmpty()) {
            return false;
        }
        return cccd.matches("\\d{12}");
    }
} 