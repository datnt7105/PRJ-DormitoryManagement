<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm Phòng Mới</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .form-control:focus {
            border-color: #6b48ff;
            box-shadow: 0 0 0 0.2rem rgba(107, 72, 255, 0.25);
        }
        .form-select:focus {
            border-color: #6b48ff;
            box-shadow: 0 0 0 0.2rem rgba(107, 72, 255, 0.25);
        }
        .btn-primary {
            background-color: #6b48ff;
            border-color: #6b48ff;
        }
        .btn-primary:hover {
            background-color: #5a3de8;
            border-color: #5a3de8;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h2>Thêm Phòng Mới</h2>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/AddRoomServlet" method="post">
            <div class="mb-3">
                <label class="form-label">Số phòng <span class="text-danger">*</span></label>
                <input type="text" class="form-control" name="roomNumber" required 
                       placeholder="VD: A101, B205, C301">
            </div>
            <div class="mb-3">
                <label class="form-label">Tòa nhà <span class="text-danger">*</span></label>
                <select class="form-select" name="buildingId" required>
                    <option value="">Chọn tòa nhà</option>
                    <c:forEach var="building" items="${buildings}">
                        <option value="${building.buildingID}">${building.buildingName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="mb-3">
                <label class="form-label">Tầng <span class="text-danger">*</span></label>
                <input type="number" class="form-control" name="floor" min="1" max="20" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Số người tối đa <span class="text-danger">*</span></label>
                <select class="form-select" name="occupancy" required>
                    <option value="4">4</option>
                    <option value="6">6</option>
                    <option value="8">8</option>
                </select>
            </div>
            <div class="mb-3">
                <label class="form-label">Số người hiện tại</label>
                <input type="number" class="form-control" name="currentOccupants" value="0" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Giá <span class="text-danger">*</span></label>
                <input type="number" step="0.01" class="form-control" name="price" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Trạng thái <span class="text-danger">*</span></label>
                <select class="form-select" name="status" required>
                    <option value="Available">Trống</option>
                    <option value="Occupied">Đã thuê</option>
                    <option value="Maintenance">Bảo trì</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Thêm phòng</button>
            <a href="${pageContext.request.contextPath}/ViewRoomServlet" class="btn btn-secondary">Quay lại</a>
        </form>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>