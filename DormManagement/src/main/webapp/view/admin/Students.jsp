<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.entity.Students"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Sinh viên</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
    <h2 class="mb-4">Danh sách Sinh viên</h2>
    
    <!-- Hiển thị thông báo -->
    <c:if test="${not empty message}">
        <div class="alert alert-success text-center" role="alert">
            ${message}
        </div>
    </c:if>
    
    <!-- Form tìm kiếm và filter -->
    <form method="get" action="${pageContext.request.contextPath}/student-manager" class="row g-2 mb-3 align-items-center">
        <div class="col-md-4">
            <input type="text" name="keyword" class="form-control" 
                   placeholder="Tìm kiếm theo tên, email, SĐT..." 
                   value="${param.keyword}"/>
        </div>
        <div class="col-md-2">
            <select name="gender" class="form-select">
                <option value="" ${empty param.gender ? 'selected' : ''}>Tất cả giới tính</option>
                <option value="Male" ${param.gender == 'Male' ? 'selected' : ''}>Nam</option>
                <option value="Female" ${param.gender == 'Female' ? 'selected' : ''}>Nữ</option>
                <option value="Other" ${param.gender == 'Other' ? 'selected' : ''}>Khác</option>
            </select>
        </div>
        <div class="col-md-2">
            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
            <a href="${pageContext.request.contextPath}/student-manager" class="btn btn-secondary">Đặt lại</a>
        </div>
        <div class="col-md-4 text-end">
            <a href="${pageContext.request.contextPath}/student-manager?action=addForm" class="btn btn-success">+ Thêm Sinh viên</a>
        </div>
    </form>
    
    <!-- Bảng danh sách sinh viên -->
    <table class="table table-bordered table-hover">
        <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Họ và tên</th>
                <th>Giới tính</th>
                <th>Ngày sinh</th>
                <th>Email</th>
                <th>SĐT</th>
                <th>Thao tác</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty students}">
                    <c:forEach var="student" items="${students}">
                        <tr>
                            <td>${student.studentId}</td>
                            <td>${student.fullName}</td>
                            <td>${student.gender}</td>
                            <td>${student.dob}</td>
                            <td>${student.email}</td>
                            <td>${student.phone}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/student-manager?action=editForm&id=${student.studentId}" 
                                   class="btn btn-warning btn-sm">Sửa</a>
                                <form action="${pageContext.request.contextPath}/student-manager" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="${student.studentId}">
                                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Xóa sinh viên này?')">Xóa</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7" class="text-center">Không có sinh viên nào.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
    
</div>
</body>
</html>