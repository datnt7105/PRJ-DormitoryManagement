<%-- 
    Document   : register
    Created on : Jun 17, 2025, 11:11:30 AM
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đăng ký tài khoản</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .register-container {
                background: white;
                border-radius: 15px;
                box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
                padding: 40px;
                width: 100%;
                max-width: 500px;
            }
            .form-control:focus {
                border-color: #667eea;
                box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
            }
            .btn-register {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                border: none;
                border-radius: 25px;
                padding: 12px 30px;
                font-weight: 600;
                transition: all 0.3s ease;
            }
            .btn-register:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
            }
            .alert {
                border-radius: 10px;
                border: none;
            }
            .form-label {
                font-weight: 600;
                color: #333;
            }
            .login-link {
                text-align: center;
                margin-top: 20px;
            }
            .login-link a {
                color: #667eea;
                text-decoration: none;
                font-weight: 600;
            }
            .login-link a:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <div class="register-container">
            <div class="text-center mb-4">
                <h2 class="fw-bold text-primary">Đăng ký tài khoản</h2>
                <p class="text-muted">Vui lòng điền đầy đủ thông tin bên dưới</p>
            </div>

            <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= request.getAttribute("error") %>
            </div>
            <% } %>

            <% if (request.getAttribute("successMessage") != null) { %>
            <div class="alert alert-success" role="alert">
                <%= request.getAttribute("successMessage") %>
            </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/RegisterServlet" method="POST" id="registerForm">
                <input type="hidden" name="role" value="student">

                <div class="mb-3">
                    <label for="username" class="form-label">Tên đăng nhập *</label>
                    <input type="text" class="form-control" id="username" name="username" 
                           value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>"
                           required>
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label">Email *</label>
                    <input type="email" class="form-control" id="email" name="email" 
                           value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>"
                           placeholder="example@gmail.com hoặc student@fpt.edu.vn" required>
                    <div class="form-text">Email phải có định dạng @gmail.com hoặc @fpt.edu.vn</div>
                </div>

                <div class="mb-3">
                    <label for="fullName" class="form-label">Họ và tên *</label>
                    <input type="text" class="form-control" id="fullName" name="fullName" 
                           value="<%= request.getAttribute("fullName") != null ? request.getAttribute("fullName") : "" %>"
                           required>
                </div>

                <!-- Đã xóa trường nhập CCCD -->

                <div class="mb-3">
                    <label for="cccd" class="form-label">Giới tính *</label>
                    <select type="text" class="form-control" id="gender" name="gender" require>  
                        <option value="" <%= request.getAttribute("gender") == null ? "selected" : "" %>>Chọn giới tính</option>
                        <option value="Male" <%= "Nam".equals(request.getAttribute("gender")) ? "selected" : "" %>>Nam</option>
                        <option value="Female" <%= "Nữ".equals(request.getAttribute("gender")) ? "selected" : "" %>>Nữ</option>
                        <option value="Other" <%= "Khác".equals(request.getAttribute("gender")) ? "selected" : "" %>>Khác</option>
                    </select>
                    <div class="form-text">Vui lòng chọn giới tính</div>
                </div>

                <div class="mb-3">
                    <label for="cccd" class="form-label">Địa chỉ *</label>
                    <input type="text" class="form-control" id="address" name="address" 
                           value="<%= request.getAttribute("address") != null ? request.getAttribute("address") : "" %>"
                           placeholder="Đông Anh - Hà Nội" required>
                    <div class="form-text">Vui lòng nhập địa chỉ đầy đủ</div>
                </div>

                <div class="mb-3">
                    <label for="phone" class="form-label">Số điện thoại *</label>
                    <input type="text" class="form-control" id="phone" name="phone"
                           value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : "" %>"
                           placeholder="Nhập số điện thoại 10 hoặc 11 số" required maxlength="11">
                    <div class="form-text">Số điện thoại phải có 10 hoặc 11 số và không trùng với tài khoản khác</div>
                </div>
                <div class="mb-3">
                    <label for="dob" class="form-label">Ngày sinh *</label>
                    <input type="date" class="form-control" id="dob" name="dob" 
                           value="<%= request.getAttribute("dob") != null ? request.getAttribute("dob") : "" %>"
                           required>
                </div>


                <div class="mb-3">
                    <label for="password" class="form-label">Mật khẩu *</label>
                    <input type="password" class="form-control" id="password" name="password" 
                           minlength="8" required>
                    <div class="form-text">Mật khẩu phải có ít nhất 8 ký tự</div>
                </div>

                <div class="mb-4">
                    <label for="confirmPassword" class="form-label">Xác nhận mật khẩu *</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                           minlength="8" required>
                </div>

                <div class="d-grid">
                    <button type="submit" class="btn btn-primary btn-register">Đăng ký</button>
                </div>
            </form>

            <div class="login-link">
                <p>Đã có tài khoản? <a href="${pageContext.request.contextPath}/view/common/login.jsp">Đăng nhập ngay</a></p>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Validate form trước khi submit
            document.getElementById('registerForm').addEventListener('submit', function (e) {
                const password = document.getElementById('password').value;
                const confirmPassword = document.getElementById('confirmPassword').value;
                const email = document.getElementById('email').value;
                const cccd = document.getElementById('cccd').value;
                const phone = document.getElementById('phone').value;

                // Kiểm tra password
                if (password !== confirmPassword) {
                    e.preventDefault();
                    alert('Mật khẩu xác nhận không khớp!');
                    return false;
                }
                // Kiểm tra mật khẩu đủ mạnh
                if (!/^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,}$/.test(password)) {
                    e.preventDefault();
                    alert('Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ cái, số và ký tự đặc biệt!');
                    return false;
                }
                // Kiểm tra email
                if (!email.endsWith('@gmail.com') && !email.endsWith('@fpt.edu.vn')) {
                    e.preventDefault();
                    alert('Email phải có định dạng @gmail.com hoặc @fpt.edu.vn hợp lệ!');
                    return false;
                }
                // Kiểm tra CCCD
                if (!/^\d{12}$/.test(cccd)) {
                    e.preventDefault();
                    alert('CCCD phải có đúng 12 ký tự số!');
                    return false;
                }
                // Kiểm tra số điện thoại
                if (!/^\d{10,11}$/.test(phone)) {
                    e.preventDefault();
                    alert('Số điện thoại phải có 10 hoặc 11 số!');
                    return false;
                }
            });
            document.getElementById('cccd').addEventListener('input', function (e) {
                this.value = this.value.replace(/[^0-9]/g, '');
            });
            document.getElementById('phone').addEventListener('input', function (e) {
                this.value = this.value.replace(/[^0-9]/g, '');
            });
        </script>
    </body>
</html>
