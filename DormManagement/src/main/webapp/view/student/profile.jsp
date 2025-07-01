<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Student Profile - Quản Lý Ký Túc Xá FPT</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            body {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                padding: 20px;
            }
            .profile-container {
                background: rgba(255, 255, 255, 0.95);
                border-radius: 15px;
                box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
                padding: 30px;
                margin: 20px auto;
                max-width: 800px;
            }
            .error-message {
                color: red;
                text-align: center;
                padding: 10px;
            }
            .btn-group .btn {
                margin-right: 10px;
            }
        </style>
    </head>
    <body>
        <div class="profile-container">
            <h2><i class="fas fa-user me-2"></i>Thông tin cá nhân</h2>
            <c:if test="${not empty formErrorMessage}">
                <div class="error-message">${formErrorMessage}</div>
            </c:if>

            <div class="btn-group mb-4">
                <a href="${pageContext.request.contextPath}/student/profile?action=edit" class="btn btn-warning">Sửa thông tin</a>
                <a href="${pageContext.request.contextPath}/student/profile?action=addParent" class="btn btn-success">Thêm thông tin phụ huynh</a>
            </div>

            <c:choose>
                <c:when test="${param.action == null || param.action == 'view'}">
                    <!-- View Mode -->
                    <h4>Thông tin sinh viên</h4>
                    <p><strong>Họ và tên:</strong> <c:out value="${student.fullName}" default="N/A"/></p>
                    <p><strong>Email:</strong> <c:out value="${student.email}" default="N/A"/></p>
                    <p><strong>Ngày sinh:</strong> <fmt:formatDate value="${student.dob}" pattern="yyyy-MM-dd"/>
                    <p><strong>Giới tính:</strong> <c:out value="${student.gender}" default="N/A"/></p>
                    <p><strong>Số điện thoại:</strong> <c:out value="${student.phone}" default="N/A"/></p>
                    <p><strong>Địa chỉ:</strong> <c:out value="${student.address}" default="N/A"/></p>
                    <p><strong>Trạng thái phòng:</strong> <c:out value="${student.statusRoom}" default="N/A"/></p>

                    <h4>Thông tin phụ huynh</h4>
                    <c:choose>
                        <c:when test="${not empty parents}">
                            <c:forEach var="parent" items="${parents}">
                                <div class="card mb-3">
                                    <div class="card-body">
                                        <p><strong>Tên phụ huynh:</strong> <c:out value="${parent.parentName}" default="N/A"/></p>
                                        <p><strong>Số điện thoại:</strong> <c:out value="${parent.phone}" default="N/A"/></p>
                                        <p><strong>Mối quan hệ:</strong> <c:out value="${parent.relationship}" default="N/A"/></p>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <p>Chưa có thông tin phụ huynh.</p>
                        </c:otherwise>
                    </c:choose>
                </c:when>

                <c:when test="${param.action == 'edit'}">
                    <!-- Edit Mode -->
                    <form action="${pageContext.request.contextPath}/student/profile?action=update" method="POST">
                        <div class="mb-3">
                            <label for="fullName" class="form-label">Họ và tên</label>
                            <input type="text" class="form-control" id="fullName" name="fullName" value="<c:out value='${student.fullName}'/>" required>
                        </div>
                        <div class="mb-3">
                            <label for="gender" class="form-label">Giới tính</label>
                            <select class="form-select" id="gender" name="gender" required>
                                <option value="" <c:if test="${empty student.gender}">selected</c:if>>Chọn giới tính</option>
                                <option value="Nam" <c:if test="${student.gender == 'Nam'}">selected</c:if>>Nam</option>
                                <option value="Nữ" <c:if test="${student.gender == 'Nữ'}">selected</c:if>>Nữ</option>
                                <option value="Khác" <c:if test="${student.gender == 'Khác'}">selected</c:if>>Khác</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="address" class="form-label">Địa chỉ</label>
                                <input type="text" class="form-control" id="address" name="address" value="<c:out value='${student.address}'/>">
                        </div>
                        <div class="mb-3">
                            <label for="phone" class="form-label">Số điện thoại</label>
                            <input type="text" class="form-control" id="phone" name="phone" value="<c:out value='${student.phone}'/>">
                        </div>
                        <div class="mb-3">
                            <label for="dob" class="form-label">Ngày sinh</label>
                            <input type="date" class="form-control" id="dob" name="dob" value="<c:out value='${student.dob}'/>">
                        </div>

                        <h4>Thông tin phụ huynh</h4>
                        <c:forEach var="parent" items="${parents}">
                            <div class="card mb-3">
                                <div class="card-body">
                                    <input type="hidden" name="parentId" value="${parent.studentParentId}">
                                    <div class="mb-3">
                                        <label for="parentName_${parent.studentParentId}" class="form-label">Tên phụ huynh</label>
                                        <input type="text" class="form-control" id="parentName_${parent.studentParentId}" name="parentName_${parent.studentParentId}" value="<c:out value='${parent.parentName}'/>" required>
                                    </div>
                                    <div class="mb-3">
                                        <label for="parentPhone_${parent.studentParentId}" class="form-label">Số điện thoại</label>
                                        <input type="text" class="form-control" id="parentPhone_${parent.studentParentId}" name="parentPhone_${parent.studentParentId}" value="<c:out value='${parent.phone}'/>">
                                    </div>
                                    <div class="mb-3">
                                        <label for="relationship_${parent.studentParentId}" class="form-label">Mối quan hệ</label>
                                        <input type="text" class="form-control" id="relationship_${parent.studentParentId}" name="relationship_${parent.studentParentId}" value="<c:out value='${parent.relationship}'/>" required>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <button type="submit" class="btn btn-primary">Cập nhật</button>
                    </form>
                </c:when>

                <c:when test="${param.action == 'addParent'}">
                    <!-- Add Parent Mode -->
                    <form action="${pageContext.request.contextPath}/student/profile?action=add" method="POST">
                        <h4>Thêm thông tin phụ huynh</h4>
                        <div class="mb-3">
                            <label for="parentName" class="form-label">Tên phụ huynh</label>
                            <input type="text" class="form-control" id="parentName" name="parentName" required>
                        </div>
                        <div class="mb-3">
                            <label for="parentPhone" class="form-label">Số điện thoại</label>
                            <input type="text" class="form-control" id="parentPhone" name="parentPhone">
                        </div>
                        <div class="mb-3">
                            <label for="relationship" class="form-label">Mối quan hệ</label>
                            <input type="text" class="form-control" id="relationship" name="relationship" required>
                        </div>
                        <button type="submit" class="btn btn-success">Thêm phụ huynh</button>
                    </form>
                </c:when>
            </c:choose>

            <a href="${pageContext.request.contextPath}/view/student/dashboardStudent.jsp" class="btn btn-secondary mt-3">Quay lại Dashboard</a>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>