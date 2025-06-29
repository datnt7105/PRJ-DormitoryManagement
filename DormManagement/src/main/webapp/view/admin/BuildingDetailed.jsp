
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="model.entity.Building" %>
<!DOCTYPE html>
<%
    Building building = (Building) request.getAttribute("building");
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Chi Tiết Tòa Nhà - Hệ Thống Quản Lý Ký Túc Xá</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .sidebar {
            min-height: 100vh;
            background-color: #343a40;
            color: white;
        }
        .sidebar .nav-link {
            color: rgba(255,255,255,.75);
        }
        .sidebar .nav-link:hover {
            color: rgba(255,255,255,1);
        }
        .sidebar .nav-link.active {
            color: white;
            background-color: rgba(255,255,255,.1);
        }
        .card img {
            height: 300px;
            object-fit: cover;
            width: 100%;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3 col-lg-2 px-0 sidebar">
            <div class="p-3">
                <h4>Bảng Điều Khiển Quản Trị</h4>
                <hr>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link" href="dashboard"><i class="bi bi-speedometer2"></i> Bảng Điều Khiển</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="buildings"><i class="bi bi-building"></i> Tòa Nhà</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="rooms"><i class="bi bi-door-open"></i> Phòng</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="students"><i class="bi bi-people"></i> Sinh Viên</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="services"><i class="bi bi-gear"></i> Dịch Vụ</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="invoices"><i class="bi bi-receipt"></i> Hóa Đơn</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="register-forms"><i class="bi bi-file-text"></i> Đơn Đăng Ký</a>
                    </li>
                    <li class="nav-item mt-3">
                        <a class="nav-link text-danger" href="../logout"><i class="bi bi-box-arrow-right"></i> Đăng Xuất</a>
                    </li>
                </ul>
            </div>
        </div>

        <!-- Main Content -->
        <div class="col-md-9 col-lg-10 p-4">
            <h2>Chi Tiết Tòa Nhà</h2>
            <hr>
            <c:if test="${building == null}">
                <div class="alert alert-warning">Không tìm thấy thông tin tòa nhà.</div>
            </c:if>
            <c:if test="${building != null}">
                <div class="card mb-4">
                    <img src="${building.imageUrl}" class="card-img-top" alt="${building.buildingName}">
                    <div class="card-body">
                        <h5 class="card-title">${building.buildingName}</h5>
                        <p class="card-text"><strong>ID Quản Trị Viên:</strong> ${building.adminID}</p>
                        <p class="card-text"><strong>Số Tầng:</strong> ${building.numberFloors}</p>
                        <p class="card-text"><strong>Trạng Thái:</strong> ${building.status}</p>
                        <a href="${pageContext.request.contextPath}/BuildingController?action=list" class="btn btn-primary">Quay lại</a>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
