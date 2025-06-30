<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thêm Tòa Nhà - Hệ Thống Quản Lý Ký Túc Xá</title>
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
            .form-label {
                font-weight: 500;
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
                        <h4>Bảng Điều Khiển Quản Trị</h4>
                        <hr>
                        <ul class="nav flex-column">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/dashboard"><i class="bi bi-speedometer2"></i> Tổng Quan</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link active" href="${pageContext.request.contextPath}/dashboard#buildings-section"><i class="bi bi-building"></i> Tòa Nhà</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/students"><i class="bi bi-people"></i> Sinh Viên</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/services"><i class="bi bi-gear"></i> Dịch Vụ</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/invoices"><i class="bi bi-receipt"></i> Hóa Đơn</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/register-forms"><i class="bi bi-file-text"></i> Đơn Đăng Ký</a>
                            </li>
                            <li class="nav-item mt-3">
                                <a class="nav-link text-danger" href="${pageContext.request.contextPath}/logout"><i class="bi bi-box-arrow-right"></i> Đăng Xuất</a>
                            </li>
                        </ul>
                    </div>
                </div>

                <!-- Main Content -->
                <div class="col-md-9 col-lg-10 main-content">
                    <h2>Thêm Tòa Nhà</h2>
                    <hr>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <div class="card">
                        <div class="card-body">
                            <form action="${pageContext.request.contextPath}/BuildingController?action=add" method="POST" enctype="multipart/form-data">
                                <div class="mb-3">
                                    <label for="buildingName" class="form-label">Tên Tòa Nhà</label>
                                    <input type="text" class="form-control" id="buildingName" name="buildingName" required>
                                </div>
                                <div class="mb-3">
                                    <label for="numberFloors" class="form-label">Số Tầng</label>
                                    <input type="number" class="form-control" id="numberFloors" name="numberFloors" min="1" required>
                                </div>
                                <div class="mb-3">
                                    <label for="status" class="form-label">Trạng Thái</label>
                                    <select class="form-select" id="status" name="status" required>
                                        <option value="active">Hoạt động</option>
                                        <option value="inactive">Không hoạt động</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="imageFile" class="form-label">Hình Ảnh</label>
                                    <input type="file" class="form-control" id="imageFile" name="imageFile" accept=".jpg,.png,.webp">
                                </div>
                                <button type="submit" class="btn btn-primary btn-action"><i class="bi bi-save"></i> Thêm</button>
                                <a href="${pageContext.request.contextPath}/dashboard#buildings-section" class="btn btn-secondary btn-action">Hủy</a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Auto-dismiss alerts after 5 seconds
            setTimeout(() => {
                const alerts = document.querySelectorAll('.alert');
                alerts.forEach(alert => {
                    new bootstrap.Alert(alert).close();
                });
            }, 5000);
        </script>
    </body>
</html>