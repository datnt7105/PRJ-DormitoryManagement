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
                height: 250px; /* Fixed height for consistent size */
                object-fit: cover; /* Maintain aspect ratio and cover the area */
            }
            .card-overlay {
                position: absolute;
                bottom: 10px; /* Position near the bottom of the image */
                left: 0;
                width: 100%;
                padding: 10px;
                background: rgba(0, 0, 0, 0.5); /* Semi-transparent background for readability */
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
            .btn-primary {
                background-color: #007bff;
                border: none;
                padding: 5px 10px;
                font-size: 14px;
                margin-top: 5px;
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
                    <h2>Danh sách tòa Ký túc xá</h2>
                    <hr>
                    <div class="row">
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img src="../../images/dormA.jpg" class="card-img-top" alt="Tòa A">
                                <div class="card-overlay">
                                    <h5>Dormitory A</h5>
                                    <div class="card-body">
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=view&buildingID=1" class="btn btn-primary">Xem Chi Tiết</a>
                                    </div>
                                </div>
                            </div> 
                        </div>
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img src="../../images/dormB.jpg" class="card-img-top" alt="Tòa B">
                                <div class="card-overlay">
                                    <h5>Dormitory B</h5>
                                    <div class="card-body">
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=view&buildingID=2" class="btn btn-primary">Xem Chi Tiết</a>
                                    </div>
                                </div>
                            </div> 
                        </div>
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img src="../../images/dormC.jpg" class="card-img-top" alt="Tòa C">
                                <div class="card-overlay">
                                    <h5>Dormitory C</h5>
                                    <div class="card-body">
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=view&buildingID=3" class="btn btn-primary">Xem Chi Tiết</a>
                                    </div>
                                </div>
                            </div> 
                        </div>
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img src="../../images/DormD.png" class="card-img-top" alt="Tòa D">
                                <div class="card-overlay">
                                    <h5>Dormitory D</h5>
                                    <div class="card-body">
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=view&buildingID=4" class="btn btn-primary">Xem Chi Tiết</a>
                                    </div>
                                </div>
                            </div> 
                        </div>
                        <div class="col-md-4 mb-4">
                            <div class="card">
                                <img src="../../images/dormE.webp" class="card-img-top" alt="Tòa E">
                                <div class="card-overlay">
                                    <h5>Dormitory E</h5>
                                    <div class="card-body">
                                        <a href="${pageContext.request.contextPath}/BuildingController?action=view&buildingID=${building.buildingID}" class="btn btn-primary">Xem Chi Tiết</a>
                                    </div>
                                </div>
                            </div> 
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>