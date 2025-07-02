<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="model.dao.RoomDAO, model.dao.BuildingDAO, model.dao.DBContext, model.entity.Room, model.entity.RoomType, model.entity.Building, java.util.*, java.sql.SQLException" %>

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">

<!-- Bootstrap JS - LOAD FIRST -->
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

<%
    List<Room> rooms = new ArrayList<>();
    List<RoomType> roomTypes = new ArrayList<>();
    List<Building> buildings = new ArrayList<>();
    String errorMessage = null;
    
    try {
        RoomDAO roomDAO = new RoomDAO();
        DBContext dbContext = new DBContext();
        String imagePath = application.getRealPath("/images");
        BuildingDAO buildingDAO = new BuildingDAO(dbContext.getConnection(), imagePath);
        
        rooms = roomDAO.getAllRooms();
        roomTypes = roomDAO.getAllRoomTypes();
        buildings = buildingDAO.getAllBuildings();
        
    } catch (SQLException e) {
        errorMessage = "Database Error: " + e.getMessage();
        e.printStackTrace();
    } catch (Exception e) {
        errorMessage = "General Error: " + e.getMessage();
        e.printStackTrace();
    }
    
    request.setAttribute("rooms", rooms);
    request.setAttribute("roomTypes", roomTypes);
    request.setAttribute("buildings", buildings);
%>

<div class="content-section active">
    <!-- Toast Container -->
    <div class="toast-container">
        <!-- Toast will be created by JavaScript -->
    </div>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>Quản Lý Phòng</h2>
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addRoomModal">
            <i class="bi bi-plus-lg"></i> Thêm Phòng Mới
        </button>
    </div>

    <!-- Error Display -->
    <% if (errorMessage != null) { %>
        <div class="alert alert-danger" role="alert">
            <h4 class="alert-heading">Lỗi kết nối!</h4>
            <p><%= errorMessage %></p>
        </div>
    <% } %>

    <!-- Statistics Cards -->
    <div class="row g-3 mb-4">
        <div class="col-md-3">
            <div class="card bg-primary text-white">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <div>
                            <h6 class="card-title">Tổng số phòng</h6>
                            <h3 class="mb-0"><%= rooms.size() %></h3>
                        </div>
                        <div class="align-self-center">
                            <i class="bi bi-door-open fs-2"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-success text-white">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <div>
                            <h6 class="card-title">Phòng trống</h6>
                            <h3 class="mb-0">
                                <%
                                    int availableCount = 0;
                                    for (Room room : rooms) {
                                        if ("Available".equals(room.getStatus())) {
                                            availableCount++;
                                        }
                                    }
                                    out.print(availableCount);
                                %>
                            </h3>
                        </div>
                        <div class="align-self-center">
                            <i class="bi bi-check-circle fs-2"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-warning text-white">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <div>
                            <h6 class="card-title">Phòng đã thuê</h6>
                            <h3 class="mb-0">
                                <%
                                    int occupiedCount = 0;
                                    for (Room room : rooms) {
                                        if ("Occupied".equals(room.getStatus())) {
                                            occupiedCount++;
                                        }
                                    }
                                    out.print(occupiedCount);
                                %>
                            </h3>
                        </div>
                        <div class="align-self-center">
                            <i class="bi bi-people fs-2"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-danger text-white">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <div>
                            <h6 class="card-title">Đang bảo trì</h6>
                            <h3 class="mb-0">
                                <%
                                    int maintenanceCount = 0;
                                    for (Room room : rooms) {
                                        if ("Maintenance".equals(room.getStatus())) {
                                            maintenanceCount++;
                                        }
                                    }
                                    out.print(maintenanceCount);
                                %>
                            </h3>
                        </div>
                        <div class="align-self-center">
                            <i class="bi bi-tools fs-2"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Filter and Search -->
    <div class="card mb-4">
        <div class="card-body">
            <div class="row g-3">
                <div class="col-md-3">
                    <label class="form-label">Tòa nhà</label>
                    <select class="form-select" id="filterBuilding">
                        <option value="">Tất cả tòa nhà</option>
                        <% for (Building building : buildings) { %>
                            <option value="<%= building.getBuildingID() %>"><%= building.getBuildingName() %></option>
                        <% } %>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Trạng thái</label>
                    <select class="form-select" id="filterStatus">
                        <option value="">Tất cả trạng thái</option>
                        <option value="Available">Trống</option>
                        <option value="Occupied">Đã thuê</option>
                        <option value="Maintenance">Bảo trì</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Loại phòng</label>
                    <select class="form-select" id="filterRoomType">
                        <option value="">Tất cả loại phòng</option>
                        <% for (RoomType roomType : roomTypes) { %>
                            <option value="<%= roomType.getRoomTypeId() %>"><%= roomType.getRoomTypeName() %></option>
                        <% } %>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Tìm kiếm</label>
                    <input type="text" class="form-control" id="searchRoom" placeholder="Nhập số phòng...">
                </div>
            </div>
        </div>
    </div>

    <!-- Rooms Table -->
    <div class="card">
        <div class="card-body">
            <% if (rooms.size() > 0) { %>
                <div class="table-responsive">
                    <table class="table table-hover" id="roomsTable">
                        <thead class="table-dark">
                            <tr>
                                <th>STT</th>
                                <th>Số phòng</th>
                                <th>Tòa nhà</th>
                                <th>Tầng</th>
                                <th>Loại phòng</th>
                                <th>Sức chứa</th>
                                <th>Giá</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                int stt = 1;
                                for (Room room : rooms) {
                            %>
                                <tr data-building="<%= room.getBuildingId() %>" data-status="<%= room.getStatus() %>" data-roomtype="<%= room.getRoomTypeId() %>">
                                    <td><%= stt++ %></td>
                                    <td><strong><%= room.getRoomNumber() %></strong></td>
                                    <td><%= room.getBuildingName() != null ? room.getBuildingName() : "N/A" %></td>
                                    <td>Tầng <%= room.getFloor() %></td>
                                    <td><%= room.getRoomTypeName() != null ? room.getRoomTypeName() : "N/A" %></td>
                                    <td><%= room.getOccupancy() %> người</td>
                                    <td><%= String.format("%,d", (int)room.getPrice()) %> VNĐ</td>
                                    <td>
                                        <% 
                                            String status = room.getStatus();
                                            String badgeClass = "secondary";
                                            String statusText = status;
                                            
                                            if ("Available".equals(status)) {
                                                badgeClass = "success";
                                                statusText = "Trống";
                                            } else if ("Occupied".equals(status)) {
                                                badgeClass = "warning";
                                                statusText = "Đã thuê";
                                            } else if ("Maintenance".equals(status)) {
                                                badgeClass = "danger";
                                                statusText = "Bảo trì";
                                            }
                                        %>
                                        <span class="badge bg-<%= badgeClass %>"><%= statusText %></span>
                                    </td>
                                    <td>
                                        <div class="btn-group" role="group">
                                            <button type="button" class="btn btn-sm btn-outline-primary" 
                                                    onclick="editRoom(<%= room.getRoomId() %>)" title="Chỉnh sửa">
                                                <i class="bi bi-pencil"></i>
                                            </button>
                                            <button type="button" class="btn btn-sm btn-outline-danger" 
                                                    onclick="deleteRoom(<%= room.getRoomId() %>, '<%= room.getRoomNumber() %>')" title="Xóa">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } else { %>
                <div class="text-center py-5">
                    <i class="bi bi-inbox display-1 text-muted"></i>
                    <h3 class="text-muted">Chưa có dữ liệu phòng</h3>
                    <p class="text-muted">Vui lòng chạy script tạo dữ liệu mẫu trong database.</p>
                    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addRoomModal">
                        <i class="bi bi-plus-lg"></i> Thêm Phòng Đầu Tiên
                    </button>
                </div>
            <% } %>
        </div>
    </div>
</div>

<!-- Add Room Modal -->
<div class="modal fade" id="addRoomModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Thêm Phòng Mới</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/RoomController" method="post">
                <input type="hidden" name="action" value="insert">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Số phòng <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" name="roomNumber" required 
                               placeholder="VD: A101, B205, C301">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Tòa nhà <span class="text-danger">*</span></label>
                        <select class="form-select" name="buildingId" required>
                            <option value="">Chọn tòa nhà</option>
                            <% for (Building building : buildings) { %>
                                <option value="<%= building.getBuildingID() %>"><%= building.getBuildingName() %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Loại phòng <span class="text-danger">*</span></label>
                        <select class="form-select" name="roomTypeId" required>
                            <option value="">Chọn loại phòng</option>
                            <% for (RoomType roomType : roomTypes) { %>
                                <option value="<%= roomType.getRoomTypeId() %>">
                                    <%= roomType.getRoomTypeName() %> - <%= roomType.getOccupancy() %> người - <%= String.format("%,d", (int)roomType.getPrice()) %> VNĐ
                                </option>
                            <% } %>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Tầng <span class="text-danger">*</span></label>
                        <input type="number" class="form-control" name="floor" min="1" max="20" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Trạng thái <span class="text-danger">*</span></label>
                        <select class="form-select" name="status" required>
                            <option value="Available">Trống</option>
                            <option value="Occupied">Đã thuê</option>
                            <option value="Maintenance">Bảo trì</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Thêm phòng</button>
                </div>
            </form>
        </div>
    </div>
</div>

