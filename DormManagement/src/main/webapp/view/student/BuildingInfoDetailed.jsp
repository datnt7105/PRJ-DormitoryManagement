<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
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
        .main-content {
            background-color: #f8f9fa;
            padding: 2rem;
        }
        .card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .building-image {
            max-width: 100%;
            height: auto;
            max-height: 400px;
            object-fit: cover;
            border-radius: 8px;
        }
        .btn-action {
            font-size: 0.9rem;
            padding: 0.5rem 1rem;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3 col-lg-2 px-0 sidebar">
            <div class="p-3">
                <h4>Bảng Điều Khiển Sinh Viên</h4>
                <hr>
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/student/dashboard"><i class="bi bi-speedometer2"></i> Tổng Quan</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/BuildingInfoServlet"><i class="bi bi-building"></i> Tòa Nhà</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/student/services"><i class="bi bi-gear"></i> Dịch Vụ</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/student/invoices"><i class="bi bi-receipt"></i> Hóa Đơn</a>
                    </li>
                    <li class="nav-item mt-3">
                        <a class="nav-link text-danger" href="${pageContext.request.contextPath}/view/student/dashboardStudent.jsp"><i class="bi bi-box-arrow-right"></i>Quay lại</a>
                    </li>
                </ul>
            </div>
        </div>

        <!-- Main Content -->
        <div class="col-md-9 col-lg-10 main-content">
            <h2>Chi Tiết Tòa Nhà</h2>
            <hr>
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:choose>
                <c:when test="${empty building}">
                    <div class="alert alert-warning" role="alert">
                        Không tìm thấy tòa nhà với ID này.
                    </div>
                    <a href="${pageContext.request.contextPath}/BuildingInfoServlet" class="btn btn-secondary btn-action">Quay lại</a>
                </c:when>
                <c:otherwise>
                    <div class="card">
                        <div class="card-body">
                            <h4 class="card-title">${building.buildingName}</h4>
                            <p class="card-text"><strong>Mã Tòa Nhà:</strong> ${building.buildingID}</p>
                            <p class="card-text"><strong>Số Tầng:</strong> ${building.numberFloors}</p>
                            <p class="card-text"><strong>Trạng Thái:</strong> 
                                <c:choose>
                                    <c:when test="${building.status == 'active'}">Hoạt động</c:when>
                                    <c:otherwise>Không hoạt động</c:otherwise>
                                </c:choose>
                            </p>
                            <p class="card-text"><strong>Mã Quản Trị:</strong> ${building.adminID}</p>
                            <c:if test="${not empty building.imageUrl}">
                                <p class="card-text"><strong>Hình Ảnh:</strong></p>
                                <img src="${building.imageUrl}" class="building-image" alt="${building.buildingName}">
                            </c:if>
                        </div>
                        <div class="card-footer">
                            <a href="${pageContext.request.contextPath}/BuildingInfoServlet" class="btn btn-secondary btn-action">Quay lại</a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Tự động ẩn thông báo sau 5 giây
    setTimeout(() => {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(alert => {
            new bootstrap.Alert(alert).close();
        });
    }, 5000);
</script>
</body>
</html>