<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.dao.BuildingDAO, model.entity.Building, java.util.*" %>
<%@ page import="model.dao.DBContext" %>


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

<c:choose>
    <c:when test="${empty buildings}">
        <div class="alert alert-warning">Chưa có tòa nhà nào.</div>
    </c:when>
    <c:otherwise>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4">
            <c:forEach var="building" items="${buildings}">
                <div class="col">
                    <div class="card h-100 shadow-sm border-0 hover-shadow" style="transition: transform 0.2s ease-in-out;">
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
                        <div class="card-footer bg-white d-flex justify-content-between">
                            <a href="${pageContext.request.contextPath}/BuildingController?action=view&buildingID=${building.buildingID}" class="btn btn-sm btn-primary">
                                <i class="bi bi-eye"></i> Xem Chi Tiết
                            </a>
                            <a href="${pageContext.request.contextPath}/BuildingController?action=edit&buildingID=${building.buildingID}" class="btn btn-sm btn-warning">
                                <i class="bi bi-pencil"></i> Sửa
                            </a>
                            <a href="${pageContext.request.contextPath}/BuildingController?action=delete&buildingID=${building.buildingID}" class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc muốn xóa tòa nhà ${building.buildingName}?')">
                                <i class="bi bi-trash"></i> Xóa
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<style>
.card:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 20px rgba(0,0,0,0.15);
}
</style>

<script>
// Auto-dismiss alerts after 5 seconds
setTimeout(() => {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        new bootstrap.Alert(alert).close();
    });
}, 5000);
</script>
