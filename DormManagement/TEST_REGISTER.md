# Hướng dẫn Test Chức năng Đăng ký

## Các bước test

### 1. Chuẩn bị
- Đảm bảo đã chạy script SQL `database_update.sql` để cập nhật database
- Build project: `mvn clean package`
- Deploy lên server (Tomcat)

### 2. Test Frontend

#### Test 1: Truy cập trang đăng ký
1. Mở trình duyệt
2. Truy cập: `http://localhost:8080/DormManagement/`
3. Click nút "Đăng Ký"
4. Hoặc truy cập trực tiếp: `http://localhost:8080/DormManagement/RegistraServlet`

**Kết quả mong đợi**: Hiển thị form đăng ký với các trường:
- Tên đăng nhập
- Email
- Họ và tên
- Mã sinh viên
- CCCD
- Mật khẩu
- Xác nhận mật khẩu

#### Test 2: Validate Client-side
1. Để trống các trường bắt buộc
2. Nhập email không đúng định dạng (không có @gmail.com)
3. Nhập mã sinh viên không phải số hoặc ít hơn 8 ký tự
4. Nhập CCCD không đúng 12 số
5. Nhập mật khẩu ít hơn 8 ký tự
6. Nhập mật khẩu xác nhận không khớp

**Kết quả mong đợi**: Hiển thị thông báo lỗi tương ứng

#### Test 3: Test form validation
1. Nhập dữ liệu hợp lệ:
   - Tên đăng nhập: `testuser`
   - Email: `test@gmail.com`
   - Họ và tên: `Nguyễn Văn A`
   - Mã sinh viên: `12345678`
   - CCCD: `123456789012`
   - Mật khẩu: `Password123`
   - Xác nhận mật khẩu: `Password123`

**Kết quả mong đợi**: Form submit thành công

### 3. Test Backend

#### Test 4: Test đăng ký thành công
1. Submit form với dữ liệu hợp lệ
2. Kiểm tra database xem có record mới được tạo không
3. Kiểm tra password đã được hash chưa

**Kết quả mong đợi**: 
- Redirect về trang login
- Hiển thị thông báo "Đăng ký thành công! Vui lòng đăng nhập."
- Record mới trong database với password đã hash

#### Test 5: Test trùng lặp username/email
1. Đăng ký với username/email đã tồn tại
2. Submit form

**Kết quả mong đợi**: 
- Hiển thị thông báo "Tên đăng nhập hoặc Email đã tồn tại."
- Giữ lại dữ liệu đã nhập (trừ trường bị lỗi)

#### Test 6: Test validate server-side
1. Bypass client-side validation
2. Submit form với dữ liệu không hợp lệ

**Kết quả mong đợi**: 
- Server validate và hiển thị thông báo lỗi
- Giữ lại dữ liệu hợp lệ

### 4. Test Navigation

#### Test 7: Test link chuyển hướng
1. Từ trang đăng ký, click "Đăng nhập ngay"
2. Từ trang đăng nhập, click "Chưa có tài khoản? Đăng Ký"
3. Từ trang chủ, click các nút Đăng Ký/Đăng Nhập

**Kết quả mong đợi**: Chuyển hướng đúng đến trang tương ứng

### 5. Test Database

#### Test 8: Kiểm tra dữ liệu trong database
```sql
-- Kiểm tra bảng Students có các cột mới
SELECT COLUMN_NAME, DATA_TYPE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Students' 
AND COLUMN_NAME IN ('CCCD');

-- Kiểm tra record mới được tạo
SELECT Username, Email, FullName, StudentCode, CCCD, CreatedAt 
FROM Students 
WHERE Username = 'testuser';
```

## Các trường hợp lỗi cần test

### 1. Lỗi kết nối database
- Tắt database server
- Submit form đăng ký
- **Kết quả mong đợi**: Hiển thị thông báo lỗi kết nối

### 2. Lỗi validation
- Email: `test@yahoo.com` (không phải gmail)
- Mã sinh viên: `1234567` (ít hơn 8 số)
- CCCD: `12345678901` (ít hơn 12 số)
- Mật khẩu: `123` (ít hơn 8 ký tự)

### 3. Lỗi trùng lặp
- Đăng ký 2 lần với cùng username
- Đăng ký 2 lần với cùng email

## Checklist hoàn thành

- [ ] Database đã được cập nhật với các cột mới
- [ ] Frontend hiển thị form đăng ký đúng
- [ ] Client-side validation hoạt động
- [ ] Server-side validation hoạt động
- [ ] Đăng ký thành công và lưu vào database
- [ ] Password được hash đúng cách
- [ ] Xử lý lỗi trùng lặp
- [ ] Navigation giữa các trang hoạt động
- [ ] Thông báo lỗi/thành công hiển thị đúng
- [ ] Responsive design hoạt động trên mobile

## Troubleshooting

### Lỗi thường gặp:

1. **404 Not Found**: Kiểm tra URL mapping trong web.xml
2. **500 Internal Server Error**: Kiểm tra log server và database connection
3. **Form không submit**: Kiểm tra action URL và method
4. **Validation không hoạt động**: Kiểm tra JavaScript console
5. **Database error**: Kiểm tra script SQL đã chạy chưa

### Debug:
- Kiểm tra log Tomcat: `logs/catalina.out`
- Kiểm tra console browser (F12)
- Kiểm tra network tab để xem request/response 