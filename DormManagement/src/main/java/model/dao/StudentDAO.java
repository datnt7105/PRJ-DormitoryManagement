package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.entity.StudentParent;
import model.entity.Students;
import org.mindrot.jbcrypt.BCrypt;

public class StudentDAO {

    private final DBContext dbContext;

    public StudentDAO() {
        this.dbContext = DBContext.getInstance();
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
        try (Connection conn = dbContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        try (Connection conn = dbContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        try (Connection conn = dbContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
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
        try (Connection conn = dbContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "UPDATE Students SET Username = ?, Email = ?, FullName = ?, Dob = ?, Gender = ?, Phone = ?, Address = ?, Status_Room = ?, UpdatedAt = GETDATE() WHERE StudentID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getUsername());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getFullName());
            stmt.setDate(4, student.getDob());
            stmt.setString(5, student.getGender());
            stmt.setString(6, student.getPhone());
            stmt.setString(7, student.getAddress());
            stmt.setString(8, student.getStatusRoom());
            stmt.setInt(9, student.getStudentId());

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
        try (Connection conn = dbContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        try (Connection conn = dbContext.getConnection();
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
                    return student;
                }
            }
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả sinh viên
     */
    public List<Students> getAll() throws SQLException {
        String sql = "SELECT * FROM Students";
        List<Students> students = new ArrayList<>();
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("StudentDAO: Lấy danh sách tất cả sinh viên...");
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
                students.add(student);
            }
            System.out.println("StudentDAO: Đã lấy " + students.size() + " sinh viên.");
            return students;
        } catch (SQLException e) {
            System.err.println("StudentDAO: Lỗi khi lấy danh sách sinh viên: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Kiểm tra xem username hoặc email đã tồn tại chưa
     */
    public boolean checkUserExists(String username, String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Students WHERE Username = ? OR Email = ?";
        try (Connection conn = dbContext.getConnection(); 
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
        String sql = "INSERT INTO Students (Username, Password, Email, FullName, Dob, Gender, Phone, Address, Status_Room, CreatedAt, UpdatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbContext.getConnection(); 
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
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public List<StudentParent> getParentsByStudentID(int studentId) throws SQLException{
        String sql = "Select * from StudentParent where StudentID = ? ";
        List<StudentParent> parents = new ArrayList<>();
        try(Connection conn = new DBContext().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, studentId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    StudentParent parent = new StudentParent();
                    parent.setParentName(rs.getString("ParentName"));
                    parent.setPhone(rs.getString("Phone"));
                    parent.setRelationship(rs.getString("Relationship"));
                    parent.setStudentId(rs.getInt("StudentId"));
                    parent.setStudentParentId(rs.getInt("StudentParentId"));
                    parents.add(parent);
                }
            }
        }
        return parents;
    }
    
    public boolean updateParent(StudentParent parent) throws SQLException {
        if (parent == null || parent.getStudentParentId() <= 0) {
            throw new IllegalArgumentException("Thông tin phụ huynh hoặc StudentParentID không hợp lệ.");
        }
        String sql = "UPDATE StudentParent SET ParentName = ?, Phone = ?, Relationship = ? WHERE StudentParentID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, parent.getParentName());
            stmt.setString(2, parent.getPhone());
            stmt.setString(3, parent.getRelationship());
            stmt.setInt(4, parent.getStudentParentId());
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }
    
    public boolean insertParent(StudentParent parent) throws SQLException{
        if(parent == null || parent.getStudentId() <= 0) {
            throw new IllegalArgumentException("Thông tin phụ huynh hoặc StudentID không hợp lệ.");
        }
        String query = "Insert into StudentParent (StudentID, ParentName, Phone, Relationship) values (?,?,?,?)";
        try(Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, parent.getStudentId());
            stmt.setString(2, parent.getParentName());
            stmt.setString(3, parent.getPhone());
            stmt.setString(4, parent.getRelationship());
            
            int rowsAffectted = stmt.executeUpdate();
            return rowsAffectted > 0;
        }
    }
    
    public boolean isParentPhoneExists(String phone, int excludeStudentParentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM StudentParent WHERE Phone = ? AND StudentParentID != ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            stmt.setInt(2, excludeStudentParentId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
}
