<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Đăng Nhập - Quản Lý Ký Túc Xá FPT</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css" type="text/css">
        <style>
            .login-container {
                background: rgba(0, 0, 0, 0.7);
                padding: 20px;
                border-radius: 10px;
                width: 300px;
                text-align: center;
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
            }
            .login-container h2 {
                margin-bottom: 20px;
                color: #ffffff; /* Màu trắng cho tiêu đề */
            }
            .login-container input {
                width: 100%;
                padding: 10px;
                margin: 10px 0;
                border: none;
                border-radius: 5px;
                text-align: left;
                box-sizing: border-box;
            }
            .login-container button {
                width: 100%;
                padding: 10px;
                background-color: #ff6200; /* Màu cam, đồng nhất với nút Đăng Nhập */
                border: none;
                border-radius: 5px;
                color: #fff;
                cursor: pointer;
                margin-top: 10px;
            }
            .login-container button:hover {
                background-color: #e65c00;
            }
            .login-container a {
                display: block;
                margin-top: 10px;
                color: #007bff; /* Màu xanh, đồng nhất với Đăng Ký */
                text-decoration: none;
            }
            .login-container a:hover {
                text-decoration: underline;
            }
            .error {
                color: #ff0000;
                margin-bottom: 10px;
            }
            .form-group {
                margin-bottom: 10px;
            }
        </style>
    </head>
    <body>
        <div class="header">
            <a href="${pageContext.request.contextPath}/RegisterServlet" class="nav-link">Đăng Ký</a>
            <a href="${pageContext.request.contextPath}/view/common/login.jsp" class="nav-link">Đăng Nhập</a>
        </div>
        <div class="login-container">
            <h2>Đăng Nhập</h2>
            <% if (request.getAttribute("error") != null) { %>
            <p class="error"><%= request.getAttribute("error") %></p>
            <% } %>
            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group">
                    <input type="text" name="emailOrUsername" placeholder="Tên đăng nhập hoặc Email" required>
                </div>
                <div class="form-group">
                    <input type="password" name="password" placeholder="Mật khẩu" required>
                </div>
                <button type="submit">Đăng Nhập</button>
            </form>
            <a href="${pageContext.request.contextPath}/RegisterServlet">Chưa có tài khoản? Đăng Ký</a>
        </div>
    </body>
</html>