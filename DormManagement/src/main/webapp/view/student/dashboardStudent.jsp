<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard - Quản Lý Ký Túc Xá FPT</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .sidebar, .main-content {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 15px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin: 20px;
        }
        .main-content {
            padding: 30px;
        }
        .nav-link {
            color: #333;
            padding: 12px 15px;
            border-radius: 10px;
            margin-bottom: 5px;
            transition: all 0.3s ease;
        }
        .nav-link:hover, .nav-link.active {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            transform: translateX(5px);
        }
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s ease;
        }
        .card:hover {
            transform: translateY(-5px);
        }
        .welcome-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            padding: 30px;
            margin-bottom: 30px;
        }
        .stat-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            padding: 20px;
            text-align: center;
        }
        .stat-card.secondary {
            background: linear-gradient(135deg, #ff6200 0%, #e65c00 100%);
        }
        .stat-card.success {
            background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
        }
        .stat-card.info {
            background: linear-gradient(135deg, #17a2b8 0%, #6f42c1 100%);
        }
        .error-message {
            color: red;
            text-align: center;
            padding: 10px;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-md-3">
            <div class="sidebar">
                <div class="text-center mb-4">
                    <h4 class="fw-bold text-primary">Student Portal</h4>
                    <p class="text-muted">Ký Túc Xá FPT</p>
                </div>
                <nav class="nav flex-column">
                    <a class="nav-link active" href="#dashboard"><i class="fas fa-tachometer-alt me-2"></i> Dashboard</a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/studentProfileServlet?action=view"><i class="fas fa-user me-2"></i> Cập nhật thông tin cá nhân</a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/BuildingInfoServlet"><i class="fas fa-bed me-2"></i> Thông tin phòng</a>
                    <a class="nav-link" href="#payment"><i class="fas fa-credit-card me-2"></i> Thanh toán</a>
                    <a class="nav-link" href="#maintenance"><i class="fas fa-tools me-2"></i> Bảo trì</a>
                    <a class="nav-link" href="#announcements"><i class="fas fa-bullhorn me-2"></i> Thông báo</a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/view/common/login.jsp"><i class="fas fa-sign-out-alt me-2"></i> Đăng xuất</a>
                </nav>
            </div>
        </div>

        <!-- Main Content -->
        <div class="col-md-9">
            <div class="main-content">
                <c:if test="${not empty errorMessage}">
                    <div class="error-message">${errorMessage}</div>
                </c:if>
                <div class="welcome-section">
                    <h2><i class="fas fa-user-graduate me-2"></i>Chào mừng, <c:out value="${student.fullName}" default="N/A"/>!</h2>
                    <p class="mb-0">Họ và tên: <c:out value="${student.fullName}" default="N/A"/></p>
                    <p class="mb-0">Email: <c:out value="${student.email}" default="N/A"/></p>
                    <p class="mb-0">Trạng thái: <c:out value="${student.statusRoom}" default="N/A"/></p>
                </div>

                <!-- Stats -->
                <div class="row mb-4">
                    <div class="col-md-3">
                        <div class="stat-card">
                            <i class="fas fa-bed fa-2x mb-2"></i>
                            <h4><c:out value="${room.roomNumber}" default="Chưa có thông tin phòng"/></h4>
                            <p class="mb-0"><c:out value="${room.roomStatus}" default="Vui lòng cập nhật"/></p>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="stat-card secondary">
                            <i class="fas fa-calendar-alt fa-2x mb-2"></i>
                            <h4>
                                <c:choose>
                                    <c:when test="${not empty room.expiryDate}">
                                        <fmt:formatDate value="${room.expiryDate}" pattern="dd/MM/yyyy"/>
                                    </c:when>
                                    <c:otherwise>Chưa có ngày hết hạn</c:otherwise>
                                </c:choose>
                            </h4>
                            <p class="mb-0"><c:out value="${room.expiryStatus}" default="Vui lòng cập nhật"/></p>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="stat-card success">
                            <i class="fas fa-check-circle fa-2x mb-2"></i>
                            <h4><c:out value="${payment.status}" default="Chưa có thông tin thanh toán"/></h4>
                            <p class="mb-0"><c:out value="${payment.details}" default="Vui lòng cập nhật"/></p>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="stat-card info">
                            <i class="fas fa-bell fa-2x mb-2"></i>
                            <h4><c:out value="${fn:length(notifications)}" default="0"/></h4>
                            <p class="mb-0">Thông báo mới</p>
                        </div>
                    </div>
                </div>

                <!-- Activities and Announcements -->
                <div class="row">
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header bg-primary text-white">
                                <h5 class="mb-0"><i class="fas fa-history me-2"></i>Hoạt động gần đây</h5>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${not empty activities}">
                                        <c:forEach var="activity" items="${activities}">
                                            <p class="mb-2">
                                                <c:out value="${activity.description}"/> - 
                                                <fmt:formatDate value="${activity.date}" pattern="dd/MM/yyyy HH:mm"/>
                                            </p>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="text-center text-muted">Chưa có hoạt động nào gần đây.</div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header bg-warning text-white">
                                <h5 class="mb-0"><i class="fas fa-bullhorn me-2"></i>Thông báo mới</h5>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${not empty notifications}">
                                        <c:forEach var="notification" items="${notifications}">
                                            <div class="alert alert-info mb-2">
                                                <c:out value="${notification.message}"/>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="alert alert-info mb-0 text-center">Chưa có thông báo mới.</div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', function (e) {
            if (this.getAttribute('href').startsWith('#')) {
                e.preventDefault();
                document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
                this.classList.add('active');
            }
        });
    });
</script>
</body>
</html>