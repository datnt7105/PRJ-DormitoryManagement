<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Student Profile - Quản Lý Ký Túc Xá FPT</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                padding: 20px;
            }
            .profile-container {
                background: rgba(255, 255, 255, 0.95);
                border-radius: 15px;
                box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
                padding: 30px;
                margin: 20px auto;
                max-width: 800px;
            }
            .error-message {
                color: red;
                text-align: center;
                padding: 10px;
            }
            .btn-group .btn {
                margin-right: 10px;
            }
        </style>
    </head>
    <body>
        <div class="profile-container">
            <h2><i class="fas fa-user me-2"></i>Thông tin cá nhân</h2>
            <c:if test="${not empty formErrorMessage}">
                <div class="error-message">${formErrorMessage}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/student/profile?action=update" method="POST">
                <div class="mb-3">
                    <label for="fullName" class="form-label">Họ và tên</label>
                    <input type="text" class="form-control" id="fullName" name="fullName" value="${student.fullName}" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email" value="${student.email}" required>
                </div>
                <div class="mb-3">
                    <label for="phone" class="form-label">Số điện thoại</label>
                    <input type="text" class="form-control" id="phone" name="phone" value="${student.phone}" required>
                </div>
                <div class="mb-3">
                    <label for="cccd" class="form-label">CCCD</label>
                    <input type="text" class="form-control" id="cccd" name="cccd" value="${student.cccd}" maxlength="12" pattern="\d{12}">
                    <div class="form-text">CCCD phải có đúng 12 ký tự số</div>
                </div>
                <div class="mb-3">
                    <label for="dob" class="form-label">Ngày sinh</label>
                    <input type="date" class="form-control" id="dob" name="dob" value="${student.dob}" required>
                </div>
                <div class="mb-3">
                    <label for="gender" class="form-label">Giới tính</label>
                    <select class="form-select" id="gender" name="gender" required>
                        <option value="" ${empty student.gender ? 'selected' : ''}>Chọn giới tính</option>
                        <option value="Male" ${student.gender == 'Male' ? 'selected' : ''}>Nam</option>
                        <option value="Female" ${student.gender == 'Female' ? 'selected' : ''}>Nữ</option>
                        <option value="Other" ${student.gender == 'Other' ? 'selected' : ''}>Khác</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Cập nhật</button>
            </form>

            <a href="${pageContext.request.contextPath}/view/student/dashboardStudent.jsp" class="btn btn-secondary mt-3">Quay lại Dashboard</a>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>