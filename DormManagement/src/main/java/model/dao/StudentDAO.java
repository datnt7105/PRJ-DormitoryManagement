package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.entity.Students;
import org.mindrot.jbcrypt.BCrypt;
import model.dao.DBContext;

public class StudentDAO extends DBContext {
    public StudentDAO() {
        super();
    }

    /**
     * Kiểm tra mật khẩu có hợp lệ không - Độ dài tối thiểu 8 ký tự - Có ít nhất
     * 1 chữ cái in hoa, 1 ký tự đặc biệt, 1 số
     */
    private void validatePassword(String password) throws SQLException {
        if (password == null || password.length() < 8
                || !password.matches("^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*\\d).+$")) {
            throw new SQLException("Mật khẩu phải có ít nhất 8 ký tự, 1 chữ cái in hoa, 1 ký tự đặc biệt và 1 số.");
            
        }
    }

    /**
     * Kiểm tra email hoặc username đã tồn tại chưa trong bảng Students
     */
    public boolean isEmailOrUsernameExists(String email, String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Students WHERE Email = ? OR Username = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /**
     * Kiểm tra số điện thoại đã tồn tại chưa (chỉ kiểm tra nếu phone không
     * null)
     */
    public boolean isPhoneExists(String phone) throws SQLException {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM Students WHERE Phone = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /**
     * Kiểm tra xem đã có sinh viên nào trong bảng Students chưa
     */
    public boolean checkIfStudentExists() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Students";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            System.out.println("StudentDAO: Kiểm tra số lượng sinh viên trong database...");
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("StudentDAO: Số lượng sinh viên: " + count);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("StudentDAO: Lỗi khi kiểm tra sinh viên tồn tại: " + e.getMessage());
            throw e;
        }
        return false;
    }

    /**
     * Tìm sinh viên dựa trên email hoặc username
     */
    public Students findStudentByEmailOrUsername(String emailOrUsername) throws SQLException {
        if (emailOrUsername == null) {
            throw new IllegalArgumentException("Email hoặc username không được null.");
        }
        String sql = "SELECT * FROM Students WHERE Email = ? OR Username = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, emailOrUsername);
            stmt.setString(2, emailOrUsername);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Students student = new Students();
                    student.setStudentId(rs.getInt("StudentID"));
                    student.setUsername(rs.getString("Username"));
                    student.setPassword(rs.getString("Password"));
                    student.setEmail(rs.getString("Email"));
                    student.setFullName(rs.getString("FullName"));
                    student.setDob(rs.getDate("Dob"));
                    student.setGender(rs.getString("Gender"));
                    student.setPhone(rs.getString("Phone"));
                    student.setAddress(rs.getString("Address"));
                    student.setStatusRoom(rs.getString("Status_Room"));
                    student.setCreatedAt(rs.getDate("CreatedAt"));
                    student.setUpdatedAt(rs.getDate("UpdatedAt"));
                    student.setCccd(rs.getString("CCCD"));
                    student.setIsApproved(rs.getInt("isApproved"));
                    System.out.println("StudentDAO: Tìm thấy sinh viên: " + student.getUsername());
                    return student;
                }
            }
        } catch (SQLException e) {
            System.err.println("StudentDAO: Lỗi khi tìm sinh viên: " + e.getMessage());
            throw e;
        }
        System.out.println("StudentDAO: Không tìm thấy sinh viên với email/username: " + emailOrUsername);
        return null;
    }
    
  /**
     * Cập nhật thông tin profile sinh viên 
     */
    public void updateStudentProfile(Students student) throws SQLException {
        if (student == null || student.getStudentId() <= 0) {
            throw new IllegalArgumentException("Sinh viên hoặc studentID không hợp lệ.");
        }
        String sql = "UPDATE Students SET Username = ?, Email = ?, FullName = ?, Dob = ?, Gender = ?, Phone = ?, Address = ?, Status_Room = ?, CCCD = ?, UpdatedAt = GETDATE() WHERE StudentID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getUsername());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getFullName());
            stmt.setDate(4, student.getDob());
            stmt.setString(5, student.getGender());
            stmt.setString(6, student.getPhone());
            stmt.setString(7, student.getAddress());
            stmt.setString(8, student.getStatusRoom());
            stmt.setString(9, student.getCccd());
            stmt.setInt(10, student.getStudentId());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Không tìm thấy sinh viên để cập nhật.");
            }
            System.out.println("StudentDAO: Cập nhật profile sinh viên thành công, rows affected: " + rows);
        } catch (SQLException e) {
            System.err.println("StudentDAO: Lỗi khi cập nhật profile sinh viên: " + e.getMessage());
            throw e;
        }
    }
    /**
     * Cập nhật mật khẩu sinh viên
     */
    public boolean updatePassword(String email, String newPassword) throws SQLException {
        validatePassword(newPassword);
        String sql = "UPDATE Students SET [Password] = ?, UpdatedAt = GETDATE() WHERE Email = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Lấy thông tin sinh viên theo ID
     */
    public Students getStudentById(int studentId) throws SQLException {
        if (studentId <= 0) {
            throw new IllegalArgumentException("studentID không hợp lệ.");
        }
        String sql = "SELECT * FROM Students WHERE StudentID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Students student = new Students();
                    student.setStudentId(rs.getInt("StudentID"));
                    student.setUsername(rs.getString("Username"));
                    student.setPassword(rs.getString("Password"));
                    student.setEmail(rs.getString("Email"));
                    student.setFullName(rs.getString("FullName"));
                    student.setDob(rs.getDate("Dob"));
                    student.setGender(rs.getString("Gender"));
                    student.setPhone(rs.getString("Phone"));
                    student.setAddress(rs.getString("Address"));
                    student.setStatusRoom(rs.getString("Status_Room"));
                    student.setCreatedAt(rs.getDate("CreatedAt"));
                    student.setUpdatedAt(rs.getDate("UpdatedAt"));
                    student.setIsApproved(rs.getInt("isApproved"));
                    return student;
                }
            }
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả sinh viên
     */
    public List<Students> getAllStudents() {
        List<Students> students = new ArrayList<>();
        String sql = "SELECT * FROM Students";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Students student = new Students();
                student.setStudentId(rs.getInt("StudentID"));
                student.setUsername(rs.getString("Username"));
                student.setPassword(rs.getString("Password"));
                student.setEmail(rs.getString("Email"));
                student.setFullName(rs.getString("FullName"));
                student.setDob(rs.getDate("Dob"));
                student.setGender(rs.getString("Gender"));
                student.setPhone(rs.getString("Phone"));
                student.setAddress(rs.getString("Address"));
                student.setStatusRoom(rs.getString("Status_Room"));
                student.setCreatedAt(rs.getDate("CreatedAt"));
                student.setUpdatedAt(rs.getDate("UpdatedAt"));
                student.setIsApproved(rs.getInt("isApproved"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    /**
     * Kiểm tra xem username hoặc email đã tồn tại chưa
     */
    public boolean checkUserExists(String username, String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Students WHERE Username = ? OR Email = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    /**
     * Thêm sinh viên mới vào database
     */
    public boolean insertStudent(Students student) throws SQLException {
        String sql = "INSERT INTO Students (Username, Password, Email, FullName, Dob, Gender, Phone, Address, Status_Room, CreatedAt, UpdatedAt, isApproved) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Hash password trước khi lưu
            String hashedPassword = BCrypt.hashpw(student.getPassword(), BCrypt.gensalt());
            
            stmt.setString(1, student.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getFullName());
            stmt.setDate(5, student.getDob());
            stmt.setString(6, student.getGender());
            stmt.setString(7, student.getPhone());
            stmt.setString(8, student.getAddress());
            stmt.setString(9, student.getStatusRoom());
            stmt.setDate(10, student.getCreatedAt());
            stmt.setDate(11, student.getUpdatedAt());
            stmt.setInt(12, student.getIsApproved()); // Bổ sung dòng này
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public void deleteStudent(int id) {
        String sql = "DELETE FROM Students WHERE StudentID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy sinh viên theo ID
    public Students getById(int id) {
        try {
            return getStudentById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Thêm sinh viên
    public boolean add(Students s) {
        try {
            return insertStudent(s);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật sinh viên
    public boolean update(Students student) {
        String sql = "UPDATE Students SET Username = ?, Password = ?, Email = ?, FullName = ?, Dob = ?, Gender = ?, Phone = ?, Address = ?, Status_Room = ?, UpdatedAt = GETDATE() WHERE StudentID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getUsername());
            stmt.setString(2, student.getPassword());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getFullName());
            stmt.setDate(5, student.getDob());
            stmt.setString(6, student.getGender());
            stmt.setString(7, student.getPhone());
            stmt.setString(8, student.getAddress());
            stmt.setString(9, student.getStatusRoom());
            stmt.setInt(10, student.getStudentId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.out.println("Error updating student: " + ex.getMessage());
            return false;
        } finally {
           
        }
    }

    // Xóa sinh viên
    public boolean delete(int id) {
        try {
            deleteStudent(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Students getByEmail(String email) {
        String sql = "SELECT * FROM Students WHERE Email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return getFromResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error getting student by email: " + ex.getMessage());
        } finally {
            
        }
        return null;
    }

    public Students getFromResultSet(ResultSet rs) throws SQLException {
        Students student = new Students();
        student.setStudentId(rs.getInt("StudentID"));
        student.setFullName(rs.getString("FullName"));
        student.setGender(rs.getString("Gender"));
        student.setDob(rs.getDate("Dob"));
        student.setEmail(rs.getString("Email"));
        student.setPhone(rs.getString("Phone"));
        student.setPassword(rs.getString("Password"));
        student.setIsApproved(rs.getInt("isApproved"));
        return student;
    }

    public Integer insert(Students student) {
        String sql = "INSERT INTO Students (Username, Password, Email, FullName, Dob, Gender, Phone, Address, Status_Room, CreatedAt, UpdatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, student.getUsername());
            stmt.setString(2, student.getPassword());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getFullName());
            stmt.setDate(5, student.getDob());
            stmt.setString(6, student.getGender());
            stmt.setString(7, student.getPhone());
            stmt.setString(8, student.getAddress());
            stmt.setString(9, student.getStatusRoom());
            stmt.setDate(10, student.getCreatedAt());
            stmt.setDate(11, student.getUpdatedAt());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating student failed, no rows affected.");
            }
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating student failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error inserting student: " + ex.getMessage());
            return -1;
        } finally {
           
        }
    }

    public boolean delete(Integer studentId) {
        String sql = "DELETE FROM Students WHERE StudentID = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.out.println("Error deleting student: " + ex.getMessage());
            return false;
        }finally{
            
       }
    }

    public List<Students> getAllPaginated(int page, int pageSize) {
        List<Students> students = new ArrayList<>();
        String sql = "SELECT * FROM Students ORDER BY StudentID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, (page - 1) * pageSize);
            stmt.setInt(2, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(getFromResultSet(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error getting paginated students: " + ex.getMessage());
        } finally {
            
        return students;
        }
    }

    public int getTotalStudents() {
        String sql = "SELECT COUNT(*) AS total FROM Students";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error getting total students: " + ex.getMessage());
        } finally {
            
        
        return 0;
        }
    }

    // Thêm hàm lấy toàn bộ sinh viên không lọc
    public List<Students> getAll() {
        List<Students> students = new ArrayList<>();
        String sql = "SELECT * FROM Students";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                students.add(getFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Khôi phục hàm getAll(String keyword, String gender) cho tìm kiếm/lọc
    public List<Students> getAll(String keyword, String gender) {
        List<Students> students = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Students WHERE 1=1");
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (FullName LIKE ? OR Email LIKE ? OR Phone LIKE ?)");
        }
        if (gender != null && !gender.trim().isEmpty()) {
            sql.append(" AND Gender = ?");
        }
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = "%" + keyword + "%";
                stmt.setString(idx++, kw);
                stmt.setString(idx++, kw);
                stmt.setString(idx++, kw);
            }
            if (gender != null && !gender.trim().isEmpty()) {
                stmt.setString(idx++, gender);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(getFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }
}
