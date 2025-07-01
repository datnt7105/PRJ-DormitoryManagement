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
            .main-content {
                background-color: #f8f9fa;
                padding: 2rem;
            }
            .card {
                border: none;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            }
            .card:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 20px rgba(0,0,0,0.15);
                transition: transform 0.2s ease-in-out;
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
                                <a class="nav-link" href="${pageContext.request.contextPath}/BuildingInfoServlet">
                                    <i class="bi bi-building"></i> Xem thông tin phòng
                                </a>


                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/student/services"><i class="bi bi-gear"></i> Dịch Vụ</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/student/invoices"><i class="bi bi-receipt"></i> Hóa Đơn</a>
                            </li>
                            <li class="nav-item mt-3">
                                <a class="nav-link text-danger" href="${pageContext.request.contextPath}/view/student/dashboardStudent.jsp"><i class="bi bi-box-arrow-right"></i>Trở lại</a>
                            </li>
                        </ul>
                    </div>
                </div>

                <!-- Main Content -->
                <div class="col-md-9 col-lg-10 main-content">
                    <h2>Danh Sách Tòa Ký Túc Xá</h2>
                    <hr>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <c:choose>
                        <c:when test="${empty buildings}">
                            <div class="alert alert-warning">Chưa có tòa nhà nào.</div>
                        </c:when>
                        <c:otherwise>
                            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4">
                                <c:forEach var="building" items="${buildings}">
                                    <div class="col">
                                        <div class="card h-100 shadow-sm border-0">
                                            <c:choose>
                                                <c:when test="${not empty building.imageUrl}">
                                                    <img src="${building.imageUrl}" class="card-img-top" alt="${building.buildingName}" style="height: 200px; object-fit: cover;">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${pageContext.request.contextPath}/images/default-building.jpg" class="card-img-top" alt="${building.buildingName}" style="height: 200px; object-fit: cover;">
                                                </c:otherwise>
                                            </c:choose>
                                            <div class="card-body">
                                                <h5 class="card-title">${building.buildingName}</h5>
                                                <p class="card-text">Số tầng: ${building.numberFloors} | Trạng thái: ${building.status == 'active' ? 'Hoạt động' : 'Không hoạt động'}</p>
                                            </div>
                                            <div class="card-footer bg-white">
                                                <a href="${pageContext.request.contextPath}/BuildingInfoServlet?action=view&buildingID=${building.buildingID}" class="btn btn-sm btn-primary">
                                                    <i class="bi bi-eye"></i> Xem Chi Tiết
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
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