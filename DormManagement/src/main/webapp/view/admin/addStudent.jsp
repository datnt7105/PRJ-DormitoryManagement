<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm Sinh viên</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5" style="max-width: 600px;">
    <h2 class="mb-4 text-center">Thêm Sinh viên</h2>
    <%-- Thông báo thành công (session hoặc request) --%>
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success">${sessionScope.message}</div>
        <c:remove var="message" scope="session"/>
    </c:if>
    <c:if test="${not empty message}">
        <div class="alert alert-success">${message}</div>
        <c:remove var="message" scope="request"/>
    </c:if>
    <%-- Thông báo lỗi (session hoặc request) --%>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
        <c:remove var="error" scope="request"/>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/student-manager">
        <input type="hidden" name="formType" value="add"/>
        <div class="mb-3">
            <label class="form-label">Username:</label>
            <input type="text" name="username" class="form-control" required/>
        </div>
        <div class="mb-3">
            <label class="form-label">Password:</label>
            <input type="password" name="password" class="form-control" required/>
        </div>
        <div class="mb-3">
            <label class="form-label">Email:</label>
            <input type="email" name="email" class="form-control" required/>
        </div>
        <div class="mb-3">
            <label class="form-label">Họ và tên:</label>
            <input type="text" name="fullName" class="form-control" required/>
        </div>
        <div class="mb-3">
            <label class="form-label">Ngày sinh:</label>
            <input type="date" name="dob" class="form-control" required/>
        </div>
        <div class="mb-3">
            <label class="form-label">Giới tính:</label>
            <select name="gender" class="form-select" required>
                <option value="Nam">Nam</option>
                <option value="Nữ">Nữ</option>
                <option value="Other">Other</option>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">SĐT:</label>
            <input type="text" name="phone" class="form-control"/>
        </div>
        <div class="mb-3">
            <label class="form-label">Địa chỉ:</label>
            <input type="text" name="address" class="form-control"/>
        </div>
        <div class="mb-3">
            <label class="form-label">Trạng thái phòng:</label>
            <input type="text" name="statusRoom" class="form-control"/>
        </div>
        <div class="d-flex justify-content-between">
            <button type="submit" class="btn btn-success">Thêm</button>
            <a href="${pageContext.request.contextPath}/student-manager" class="btn btn-secondary">Hủy</a>
        </div>
    </form>
</div>
</body>
</html> 