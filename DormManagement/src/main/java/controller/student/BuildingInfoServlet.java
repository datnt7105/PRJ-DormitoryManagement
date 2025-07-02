package controller.student;

import model.dao.BuildingDAO;
import model.entity.Building;
import model.dao.DBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BuildingInfoServlet extends HttpServlet {

    private BuildingDAO buildingDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = new DBContext().getConnection();
            String imageBasePath = getServletContext().getRealPath("/images");
            buildingDAO = new BuildingDAO(conn, imageBasePath);
        } catch (SQLException e) {
            throw new ServletException("Không thể khởi tạo kết nối cơ sở dữ liệu", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra xem sinh viên đã đăng nhập chưa
        HttpSession session = request.getSession(false);
        Integer studentId = (session != null) ? (Integer) session.getAttribute("studentId") : null;

        if (studentId == null || studentId <= 0) {
            response.sendRedirect(request.getContextPath() + "/view/common/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("view".equals(action)) {
                
//                int buildingId = Integer.parseInt(request.getParameter("buildingID"));
//                Building building = buildingDAO.getBuildingById(buildingId);
                String buildingIdParam = request.getParameter("buildingID");

                if (buildingIdParam == null || buildingIdParam.trim().isEmpty()) {
                    request.setAttribute("error", "Thiếu mã tòa nhà.");
                    request.getRequestDispatcher("/view/student/BuildingInfo.jsp").forward(request, response);
                    return;
                }

                int buildingId;
                try {
                    buildingId = Integer.parseInt(buildingIdParam.trim());
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Mã tòa nhà không hợp lệ.");
                    request.getRequestDispatcher("/view/student/BuildingInfo.jsp").forward(request, response);
                    return;
                }
                
                Building building = buildingDAO.getBuildingById(buildingId);
                if(building != null ){
                    request.setAttribute("building", building);
                    request.getRequestDispatcher("/view/student/BuildingInfoDetailed.jsp").forward(request, response);
                }else{
                    request.setAttribute("error", "Không tìm thấy tòa nhà ");
                    request.getRequestDispatcher("/view/student/BuildingInfo.jsp").forward(request, response);
                }

            } else {
                // Hiển thị danh sách tất cả các tòa nhà
                List<Building> buildings = buildingDAO.getAllBuildings();
                request.setAttribute("buildings", buildings != null ? buildings : List.of());
                request.getRequestDispatcher("/view/student/BuildingInfo.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi khi lấy dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/view/student/BuildingInfo.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet hiển thị thông tin tòa nhà cho sinh viên";
    }
}
