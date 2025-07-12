# Hướng dẫn sử dụng chức năng đăng ký

## Tổng quan
Chức năng đăng ký đã được cập nhật để hỗ trợ các trường thông tin mới:
- **Email**: Phải có định dạng @gmail.com
- **Tên người dùng**: Tên đăng nhập duy nhất
- **Mã sinh viên**: 8-10 ký tự số
- **CCCD**: Đúng 12 ký tự số

## Các file đã được cập nhật

### 1. Entity (Students.java)
- Thêm trường `cccd` (String)
- Cập nhật constructor và getter/setter

### 2. DAO (StudentDAO.java)
- Thêm phương thức `checkUserExists()` để kiểm tra username/email trùng lặp
- Thêm phương thức `insertStudent()` để thêm sinh viên mới
- Cập nhật các phương thức hiện có để hỗ trợ trường mới

### 3. Service (UserService.java)
- Tạo mới class để xử lý logic đăng ký
- Các phương thức validate:
  - `checkEmailFormat()`: Kiểm tra định dạng email
  - `validateStudentCode()`: Kiểm tra mã sinh viên
  - `validateCCCD()`: Kiểm tra CCCD
  - `registerUser()`: Thực hiện đăng ký

### 4. Servlet (RegistraServlet.java)
- Cập nhật để xử lý form đăng ký với các trường mới
- Validate dữ liệu đầu vào
- Xử lý lỗi và thông báo

### 5. JSP (register.jsp)
- Tạo form đăng ký với giao diện đẹp
- Validate client-side với JavaScript
- Responsive design với Bootstrap

## Cài đặt và sử dụng

### 1. Cập nhật Database
Chạy script SQL trong file `database_update.sql` để thêm các cột mới:
```sql
-- Thêm cột CCCD vào bảng Students
ALTER TABLE Students ADD CCCD VARCHAR(12);
```

### 2. Build và Deploy
```bash
mvn clean package
```

### 3. Truy cập trang đăng ký
URL: `http://localhost:8080/DormManagement/RegistraServlet`

## Validation Rules

### Email
- Phải có định dạng @gmail.com
- Không được để trống

### Tên đăng nhập
- Không được để trống
- Phải là duy nhất trong hệ thống

### Mã sinh viên
- 8-10 ký tự số
- Không được để trống

### CCCD
- Đúng 12 ký tự số
- Không được để trống

### Mật khẩu
- Tối thiểu 8 ký tự
- Phải khớp với mật khẩu xác nhận

## Tính năng bảo mật

1. **Password Hashing**: Sử dụng BCrypt để mã hóa mật khẩu
2. **Input Validation**: Validate cả client-side và server-side
3. **SQL Injection Prevention**: Sử dụng PreparedStatement
4. **Duplicate Check**: Kiểm tra username/email trùng lặp

## Xử lý lỗi

- Hiển thị thông báo lỗi cụ thể cho từng trường
- Giữ lại dữ liệu đã nhập khi có lỗi (trừ trường bị lỗi)
- Redirect về trang đăng ký khi có lỗi
- Redirect về trang đăng nhập khi thành công

## Giao diện

- Sử dụng Bootstrap 5 cho responsive design
- Gradient background đẹp mắt
- Form validation real-time
- Thông báo lỗi rõ ràng
- Link chuyển hướng đến trang đăng nhập

## Lưu ý

1. Đảm bảo database đã được cập nhật trước khi sử dụng
2. Kiểm tra kết nối database trong `DBContext.java`
3. Có thể thêm ràng buộc UNIQUE cho CCCD nếu cần
4. Có thể tùy chỉnh validation rules trong `