<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sửa Tòa Nhà - Hệ Thống Quản Lý Ký Túc Xá</title>
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
                max-height: 200px;
                object-fit: cover;
                border-radius: 8px;
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
                    <h2>Sửa Tòa Nhà</h2>
                    <hr>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <c:if test="${empty building}">
                        <div class="alert alert-warning" role="alert">
                            Không tìm thấy tòa nhà để chỉnh sửa.
                        </div>
                        <a href="${pageContext.request.contextPath}/dashboard#buildings-section" class="btn btn-secondary btn-action">Quay lại</a>
                    </c:if>
                    <c:if test="${not empty building}">
                        <div class="card">
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/BuildingController?action=update" method="POST" enctype="multipart/form-data">
                                    <input type="hidden" name="buildingID" value="${building.buildingID}">
                                    <div class="mb-3">
                                        <label for="buildingName" class="form-label">Tên Tòa Nhà</label>
                                        <input type="text" class="form-control" id="buildingName" name="buildingName" value="${building.buildingName}" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="numberFloors" class="form-label">Số Tầng</label>
                                        <input type="number" class="form-control" id="numberFloors" name="numberFloors" value="${building.numberFloors}" min="1" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="status" class="form-label">Trạng Thái</label>
                                        <select class="form-select" id="status" name="status" required>
                                            <option value="active" ${building.status == 'active' ? 'selected' : ''}>Hoạt động</option>
                                            <option value="inactive" ${building.status == 'inactive' ? 'selected' : ''}>Không hoạt động</option>
                                        </select>
                                    </div>
                                    <div class="mb-3">
                                        <label for="imageFile" class="form-label">Hình Ảnh</label>
                                        <input type="file" class="form-control" id="imageFile" name="imageFile" accept=".jpg,.png,.webp">
                                        <c:if test="${not empty building.imageUrl}">
                                            <p class="mt-2">Hình ảnh hiện tại:</p>
                                            <img src="${building.imageUrl}" class="building-image" alt="${building.buildingName}">
                                        </c:if>
                                    </div>
                                    <button type="submit" class="btn btn-primary btn-action"><i class="bi bi-save"></i> Cập nhật</button>
                                    <a href="${pageContext.request.contextPath}/dashboard#buildings-section" class="btn btn-secondary btn-action">Hủy</a>
                                </form>
                            </div>
                        </div>
                    </c:if>
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