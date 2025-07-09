<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Enhanced CSS for Toast and UI -->
<style>
    .toast-container {
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 9999;
        max-width: 350px;
        pointer-events: none;
    }
    .toast {
        min-width: 320px;
        max-width: 350px;
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
        border-radius: 12px;
        overflow: hidden;
        margin-bottom: 10px;
        backdrop-filter: blur(10px);
        pointer-events: auto;
        background-color: white;
        border: 1px solid rgba(0,0,0,0.1);
    }
    .toast.show {
        opacity: 1 !important;
        display: block !important;
    }
    .toast-header {
        border-radius: 12px 12px 0 0 !important;
        padding: 12px 16px;
        border: none !important;
        display: flex !important;
        align-items: center !important;
    }
    .toast-body {
        padding: 12px 16px;
        border-radius: 0 0 12px 12px;
        font-weight: 500;
        background-color: #f8f9fa;
        color: #333 !important;
        display: block !important;
    }
</style>

<div class="content-section active">
    <!-- Toast Container -->
    <div class="toast-container">
        <!-- Toast will be created by JavaScript -->
    </div>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Quản Lý Phòng</h2>
        <a href="${pageContext.request.contextPath}/AddRoomServlet" class="btn btn-primary">
            <i class="bi bi-plus-lg"></i> Thêm Phòng Mới
        </a>
    </div>

    <!-- Error Display -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            <h4 class="alert-heading">Lỗi kết nối!</h4>
            <p>${error}</p>
        </div>
    </c:if>
    <c:if test="${not empty success}">

    </c:if>
    
    

    <!-- Rooms Table -->
    <div class="card">
        <div class="card-body">
            <c:if test="${not empty rooms and fn:length(rooms) > 0}">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead class="table-dark">
                            <tr>
                                <th>STT</th>
                                <th>Số phòng</th>
                                <th>Tòa nhà</th>
                                <th>Tầng</th>
                                <th>Sức chứa</th>
                                <th>Số người hiện tại</th>
                                <th>Giá</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th> 

                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="room" items="${rooms}" varStatus="loop">
                                <tr>
                                    <td>${loop.count}</td>
                                    <td><strong>${room.roomNumber}</strong></td>
                                    <td>${not empty room.buildingName ? room.buildingName : room.buildingId}</td>
                                    <td>Tầng ${room.floor}</td>
                                    <td>${room.occupancy} người</td>
                                    <td>${room.currentOccupants} người</td>
                                    <td><fmt:formatNumber value="${room.price}" type="number" maxFractionDigits="0"/> VNĐ</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${room.status == 'Available'}">
                                                <span class="badge bg-success">Trống</span>
                                            </c:when>
                                            <c:when test="${room.status == 'Occupied'}">
                                                <span class="badge bg-warning">Đã thuê</span>
                                            </c:when>
                                            <c:when test="${room.status == 'Maintenance'}">
                                                <span class="badge bg-danger">Bảo trì</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">Không xác định</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>
                                        <!-- ✅ Hành động -->
                                        <a href="${pageContext.request.contextPath}/EditRoomServlet?roomId=${room.roomId}" class="btn btn-sm btn-outline-primary me-1">
                                            <i class="bi bi-pencil-square"></i> Sửa
                                        </a>
                                        <a href="${pageContext.request.contextPath}/DeleteRoomServlet?roomId=${room.roomId}" 
                                           class="btn btn-sm btn-outline-danger"
                                           onclick="return confirm('Bạn có chắc chắn muốn xóa phòng này không?');">
                                            <i class="bi bi-trash"></i> Xóa
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <c:if test="${empty rooms or fn:length(rooms) == 0}">
                <div class="text-center py-5">
                    <i class="bi bi-inbox display-1 text-muted"></i>
                    <h3 class="text-muted">Chưa có dữ liệu phòng</h3>
                    <p class="text-muted">Vui lòng thêm phòng để bắt đầu quản lý.</p>
                </div>
            </c:if>
        </div>
    </div>
</div>