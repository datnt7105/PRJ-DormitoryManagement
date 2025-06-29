<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh Sách Tòa Nhà - Hệ Thống Quản Lý Ký Túc Xá</title>
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
            .card {
                position: relative;
                border: none;
                margin-bottom: 20px;
                overflow: hidden;
            }
            .card-img-top {
                width: 100%;
                height: 250px;
                object-fit: cover;
            }
            .card-overlay {
                position: absolute;
                bottom: 10px;
                left: 0;
                width: 100%;
                padding: 10px;
                background: rgba(0, 0, 0, 0.5);
                color: white;
                text-align: center;
            }
            .card-overlay h5 {
                margin: 0;
                font-size: 1.25rem;
            }
            .card-body {
                margin: 0;
                padding: 0;
            }
            .btn-primary, .btn-warning, .btn-danger {
                padding: 5px 10px;
                font-size: 14px;
                margin-top: 5px;
                margin-right: 5px;
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
                                <a class="nav-link" href="${pageContext.request.contextPath}/dashboard"><i class="bi bi-speedometer2"></i> Bảng Điều Khiển</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link active" href="${pageContext.request.contextPath}/BuildingController?action=list"><i class="bi bi-building"></i> Tòa Nhà</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/rooms"><i class="bi bi-door-open"></i> Phòng</a>
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
                <div class="col-md-9 col-lg-10 p-4">
                    <h2>Danh Sách Tòa Ký Túc Xá</h2>
                    <hr>
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            ${successMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                        <c:remove var="successMessage" scope="session"/>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <div class="mb-3">
                        <a href="${pageContext.request.contextPath}/BuildingController?action=showAddForm" class="btn btn-primary">
                            <i class="bi bi-plus-circle"></i> Thêm Tòa Nhà
                        </a>
                    </div>
                    <!-- Static Buildings (Symbolic) -->
                    <h4>Tòa Nhà Mẫu</h4>
                    <div class="row">
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img src="${pageContext.request.contextPath}/images/dormA.jpg" class="card-img-top" alt="Tòa A">
                                <div class="card-overlay">
                                    <h5>Dormitory A</h5>
                                    <p>Số tầng: 5 | Trạng thái: Hoạt động</p>
                                    <div class="card-body">
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=view&buildingID=1" class="btn btn-primary">
                                            <i class="bi bi-eye"></i> Xem Chi Tiết
                                        </a>
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=edit&buildingID=1" class="btn btn-warning">
                                            <i class="bi bi-pencil"></i> Sửa
                                        </a>
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=delete&buildingID=1" class="btn btn-danger" 
                                           onclick="return confirm('Bạn có chắc muốn xóa tòa nhà Dormitory A?')">
                                            <i class="bi bi-trash"></i> Xóa
                                        </a>
                                    </div>
                                </div>
                            </div> 
                        </div>
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img src="${pageContext.request.contextPath}/images/dormB.jpg" class="card-img-top" alt="Tòa B">
                                <div class="card-overlay">
                                    <h5>Dormitory B</h5>
                                    <p>Số tầng: 5 | Trạng thái: Hoạt động</p>
                                    <div class="card-body">
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=view&buildingID=2" class="btn btn-primary">
                                            <i class="bi bi-eye"></i> Xem Chi Tiết
                                        </a>
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=edit&buildingID=2" class="btn btn-warning">
                                            <i class="bi bi-pencil"></i> Sửa
                                        </a>
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=delete&buildingID=2" class="btn btn-danger" 
                                           onclick="return confirm('Bạn có chắc muốn xóa tòa nhà Dormitory B?')">
                                            <i class="bi bi-trash"></i> Xóa
                                        </a>
                                    </div>
                                </div>
                            </div> 
                        </div>
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img src="${pageContext.request.contextPath}/images/dormC.jpg" class="card-img-top" alt="Tòa C">
                                <div class="card-overlay">
                                    <h5>Dormitory C</h5>
                                    <p>Số tầng: 5 | Trạng thái: Hoạt động</p>
                                    <div class="card-body">
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=view&buildingID=3" class="btn btn-primary">
                                            <i class="bi bi-eye"></i> Xem Chi Tiết
                                        </a>
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=edit&buildingID=3" class="btn btn-warning">
                                            <i class="bi bi-pencil"></i> Sửa
                                        </a>
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=delete&buildingID=3" class="btn btn-danger" 
                                           onclick="return confirm('Bạn có chắc muốn xóa tòa nhà Dormitory C?')">
                                            <i class="bi bi-trash"></i> Xóa
                                        </a>
                                    </div>
                                </div>
                            </div> 
                        </div>
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img src="${pageContext.request.contextPath}/images/DormD.png" class="card-img-top" alt="Tòa D">
                                <div class="card-overlay">
                                    <h5>Dormitory D</h5>
                                    <p>Số tầng: 5 | Trạng thái: Hoạt động</p>
                                    <div class="card-body">
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=view&buildingID=4" class="btn btn-primary">
                                            <i class="bi bi-eye"></i> Xem Chi Tiết
                                        </a>
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=edit&buildingID=4" class="btn btn-warning">
                                            <i class="bi bi-pencil"></i> Sửa
                                        </a>
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=delete&buildingID=4" class="btn btn-danger" 
                                           onclick="return confirm('Bạn có chắc muốn xóa tòa nhà Dormitory D?')">
                                            <i class="bi bi-trash"></i> Xóa
                                        </a>
                                    </div>
                                </div>
                            </div> 
                        </div>
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img src="${pageContext.request.contextPath}/images/dormE.webp" class="card-img-top" alt="Tòa E">
                                <div class="card-overlay">
                                    <h5>Dormitory E</h5>
                                    <p>Số tầng: 5 | Trạng thái: Hoạt động</p>
                                    <div class="card-body">
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=view&buildingID=5" class="btn btn-primary">
                                            <i class="bi bi-eye"></i> Xem Chi Tiết
                                        </a>
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=edit&buildingID=5" class="btn btn-warning">
                                            <i class="bi bi-pencil"></i> Sửa
                                        </a>
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=delete&buildingID=5" class="btn btn-danger" 
                                           onclick="return confirm('Bạn có chắc muốn xóa tòa nhà Dormitory E?')">
                                            <i class="bi bi-trash"></i> Xóa
                                        </a>
                                    </div>
                                </div>
                            </div> 
                        </div>
                    </div>
                    <!-- Dynamic Buildings -->
                    <h4>Tòa Nhà Động</h4>
                    <div class="row">
                        <c:forEach var="building" items="${buildings}">
                            <div class="col-md-4 mb-4">
                                <div class="card">
                                    <c:choose>
                                        <c:when test="${not empty building.imageUrl}">
                                            <img src="${building.imageUrl}" class="card-img-top" alt="${building.buildingName}">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/images/default-building.jpg" class="card-img-top" alt="${building.buildingName}">
                                        </c:otherwise>
                                    </c:choose>
                                    <div class="card-overlay">
                                        <h5>${building.buildingName}</h5>
                                        <p>Số tầng: ${building.numberFloors} | Trạng thái: ${building.status == 'active' ? 'Hoạt động' : 'Không hoạt động'}</p>
                                        <div class="card-body">
                                            <a href="${pageContext.request.contextPath}/BuildingController?action=view&buildingID=${building.buildingID}" class="btn btn-primary">
                                                <i class="bi bi-eye"></i> Xem Chi Tiết
                                            </a>
                                            <a href="${pageContext.request.contextPath}/BuildingController?action=edit&buildingID=${building.buildingID}" class="btn btn-warning">
                                                <i class="bi bi-pencil"></i> Sửa
                                            </a>
                                            <a href="${pageContext.request.contextPath}/BuildingController?action=delete&buildingID=${building.buildingID}" class="btn btn-danger" 
                                               onclick="return confirm('Bạn có chắc muốn xóa tòa nhà ${building.buildingName}?')">
                                                <i class="bi bi-trash"></i> Xóa
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
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
            // Log URLs for debugging
            document.querySelectorAll('a').forEach(link => {
                link.addEventListener('click', () => {
                    console.log('Navigating to:', link.href);
                });
            });
        </script>
    </body>
</html>