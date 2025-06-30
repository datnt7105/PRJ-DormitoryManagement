<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.dao.BuildingDAO, model.entity.Building, java.util.*" %>
<%@ page import="model.dao.DBContext" %>


<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Bảng Điều Khiển Quản Trị - Hệ Thống Quản Lý Ký Túc Xá</title>
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
                color: white;
            }
            .sidebar .nav-link.active {
                background-color: rgba(255,255,255,.1);
                color: white;
            }
            .main-content {
                background-color: #f8f9fa;
                padding: 2rem;
            }
            .content-section {
                display: none;
            }
            .content-section.active {
                display: block;
            }
            .dashboard-card {
                border: none;
                border-radius: 10px;
                background: linear-gradient(135deg, #6b48ff, #a970ff);
                color: white;
                padding: 1rem;
                position: relative;
                overflow: hidden;
                transition: transform 0.2s;
                height: 100%;
            }
            .dashboard-card:hover {
                transform: translateY(-5px);
            }
            .dashboard-card .icon {
                font-size: 1.5rem;
            }
            .chart-container {
                position: relative;
                height: 200px;
                background: #fff;
                border-radius: 10px;
                padding: 1rem;
                box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            }
            .info-card {
                border: none;
                border-radius: 10px;
                background: #6b48ff;
                color: white;
                padding: 1.5rem;
                position: relative;
                overflow: hidden;
                height: 100%;
            }
            .info-card::before {
                content: '';
                position: absolute;
                width: 100px;
                height: 100px;
                background: rgba(255,255,255,0.1);
                border-radius: 50%;
                top: -50px;
                right: -50px;
            }
        </style>
    </head>
    <body>
        <%
          String tab = request.getParameter("tab");
          if (tab == null) tab = "overview"; // mặc định là overview nếu không có tab
          request.setAttribute("tab", tab);
        %>




        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-3 col-lg-2 px-0 sidebar">
                    <div class="p-3">
                        <h4>Bảng Điều Khiển Quản Trị</h4>
                        <hr>
                        <ul class="nav flex-column">
                            <li class="nav-item">
                                <a class="nav-link ${tab == 'overview' ? 'active' : ''}" 
                                   href="${pageContext.request.contextPath}/view/admin/dashboard.jsp?tab=overview">
                                    <i class="bi bi-speedometer2"></i> Tổng Quan
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${tab == 'building' ? 'active' : ''}" 
                                   href="${pageContext.request.contextPath}/view/admin/dashboard.jsp?tab=building">
                                    <i class="bi bi-building"></i> Tòa Nhà
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${tab == 'students' ? 'active' : ''}" 
                                   href="${pageContext.request.contextPath}/view/admin/dashboard.jsp?tab=students">
                                    <i class="bi bi-people"></i> Sinh Viên
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${tab == 'services' ? 'active' : ''}" 
                                   href="${pageContext.request.contextPath}/view/admin/dashboard.jsp?tab=services">
                                    <i class="bi bi-gear"></i> Dịch Vụ
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${tab == 'invoices' ? 'active' : ''}" 
                                   href="${pageContext.request.contextPath}/view/admin/dashboard.jsp?tab=invoices">
                                    <i class="bi bi-receipt"></i> Hóa Đơn
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link ${tab == 'register-forms' ? 'active' : ''}" 
                                   href="${pageContext.request.contextPath}/view/admin/dashboard.jsp?tab=register-forms">
                                    <i class="bi bi-file-text"></i> Đơn Đăng Ký
                                </a>
                            </li>
                            <li class="nav-item mt-3">
                                <a class="nav-link text-danger" 
                                   href="${pageContext.request.contextPath}/logout">
                                    <i class="bi bi-box-arrow-right"></i> Đăng Xuất
                                </a>
                            </li>
                        </ul>

                    </div>
                </div>

                <!-- Main Content -->
                <div class="col-md-9 col-lg-10 main-content">
                    <c:choose>
                        <c:when test="${tab == 'building'}">
                            <%
                                BuildingDAO buildingDAO = new BuildingDAO(new DBContext().getConnection(), application.getRealPath("/images"));
                                List<Building> buildings = buildingDAO.getAllBuildings();
                                request.setAttribute("buildings", buildings);
                            %>
                            <%@ include file="Building.jsp" %>
                        </c:when>
                        
                        <c:when test="${tab == 'students'}">
                            <%@ include file="Students.jsp" %>
                        </c:when>
                        <c:when test="${tab == 'services'}">
                            <%@ include file="Services.jsp" %>
                        </c:when>
                        <c:when test="${tab == 'invoices'}">
                            <%@ include file="Invoices.jsp" %>
                        </c:when>
                        <c:when test="${tab == 'register-forms'}">
                            <%@ include file="RegisterForms.jsp" %>
                        </c:when>
                        <c:otherwise>
                            <div class="content-section active">
                                <h2 class="mb-4">Tổng Quan Hệ Thống</h2>
                                <div class="row g-4 mb-4">
                                    <!-- Today's Money -->
                                    <div class="col-md-3">
                                        <div class="dashboard-card">
                                            <div class="d-flex justify-content-between align-items-center h-100">
                                                <div>
                                                    <h6 class="mb-1">Tiền Hôm Nay</h6>
                                                    <h3 class="mb-0">233,000 VNĐ</h3>
                                                    <small class="text-success">+5% so với hôm qua</small>
                                                </div>
                                                <div class="icon"><i class="bi bi-currency-dollar"></i></div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Today's Users -->
                                    <div class="col-md-3">
                                        <div class="dashboard-card">
                                            <div class="d-flex justify-content-between align-items-center h-100">
                                                <div>
                                                    <h6 class="mb-1">Người Dùng Hôm Nay</h6>
                                                    <h3 class="mb-0">2,500</h3>
                                                    <small class="text-success">+3% so với tuần trước</small>
                                                </div>
                                                <div class="icon"><i class="bi bi-people"></i></div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- New Clients -->
                                    <div class="col-md-3">
                                        <div class="dashboard-card">
                                            <div class="d-flex justify-content-between align-items-center h-100">
                                                <div>
                                                    <h6 class="mb-1">Khách Hàng Mới</h6>
                                                    <h3 class="mb-0">+2,402</h3>
                                                    <small class="text-danger">-2% so với quý trước</small>
                                                </div>
                                                <div class="icon"><i class="bi bi-person-plus"></i></div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Sales -->
                                    <div class="col-md-3">
                                        <div class="dashboard-card">
                                            <div class="d-flex justify-content-between align-items-center h-100">
                                                <div>
                                                    <h6 class="mb-1">Doanh Thu</h6>
                                                    <h3 class="mb-0">970,450 VNĐ</h3>
                                                    <small class="text-success">+5% so với tháng trước</small>
                                                </div>
                                                <div class="icon"><i class="bi bi-cart"></i></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row g-4">
                                    <!-- Sales Overview Chart -->
                                    <div class="col-md-8">
                                        <div class="chart-container">
                                            <h6 class="mb-2">Tổng Quan Doanh Thu</h6>
                                            <small class="text-muted">Tăng 4% trong 2025</small>
                                            <canvas id="salesChart"></canvas>
                                        </div>
                                    </div>
                                    <!-- Info Card -->
                                    <div class="col-md-4">
                                        <div class="info-card">
                                            <h6 class="mb-2">Bắt Đầu Với Hệ Thống</h6>
                                            <p class="mb-0">Không có gì tôi thực sự muốn làm trong cuộc sống mà tôi không thể tốt.</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script>
            const ctx = document.getElementById('salesChart').getContext('2d');
            const salesChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: ['Th4', 'Th5', 'Th6', 'Th7', 'Th8', 'Th9', 'Th10', 'Th11', 'Th12'],
                    datasets: [{
                            label: 'Doanh Thu',
                            data: [12000, 19000, 30000, 25000, 28000, 32000, 35000, 40000, 45000],
                            borderColor: '#6b48ff',
                            tension: 0.4,
                            fill: true,
                            backgroundColor: 'rgba(107, 72, 255, 0.2)'
                        }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        </script>
    </body>
</html>