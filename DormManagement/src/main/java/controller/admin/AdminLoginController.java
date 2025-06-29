package controller.admin;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.service.AdminService;
import model.entity.Admin;

public class AdminLoginController extends HttpServlet {

    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        adminService = new AdminService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String emailOrUsername = request.getParameter("emailOrUsername");
        String password = request.getParameter("password");

        if (emailOrUsername == null || emailOrUsername.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email/Username và mật khẩu là bắt buộc.");
            request.getRequestDispatcher("/view/common/login.jsp").forward(request, response);
            return;
        }

        try {
            // Đăng nhập mặc định nếu là admin/admin123
           
            if (emailOrUsername.equals("admin") && password.equals("admin123")) {
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setFullName("Quản trị viên mặc định");
                admin.setEmail("admin@example.com");
                request.getSession().setAttribute("admin", admin);
                request.getSession().setAttribute("adminID", admin.getAdminId()); // lưu adminId vào session
                response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp");
                return;
            }

            Admin admin = adminService.authenticateAdmin(emailOrUsername, password);
            if (admin != null) {
                request.getSession().setAttribute("admin", admin);
                request.getSession().setAttribute("adminID", admin.getAdminId());
                response.sendRedirect(request.getContextPath() + "/view/admin/dashboard.jsp");
            } else {
                request.setAttribute("error", "Email/Username hoặc mật khẩu không hợp lệ.");
                request.getRequestDispatcher("/view/common/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/view/common/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/view/common/login.jsp").forward(request, response);
    }
}
