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
import model.dao.AdminDAO;

public class AdminLoginController extends HttpServlet {

    private AdminService adminService;
    private AdminDAO adminDAO;

    @Override
    public void init() throws ServletException {
        adminService = new AdminService();
        adminDAO = new AdminDAO();
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
            // Kiểm tra tài khoản admin trong cơ sở dữ liệu
            Admin admin = adminService.authenticateAdmin(emailOrUsername, password);
            if (admin == null && emailOrUsername.equals("admin") && password.equals("admin123")) {
                // Nếu tài khoản mặc định chưa tồn tại, tạo mới
                if (!adminService.checkIfAdminExists()) {
                    adminDAO.createDefaultAdmin("admin", "admin123");
                }
                // Lấy thông tin admin từ cơ sở dữ liệu
                admin = adminDAO.findAdminByEmailOrUsername("admin");
                if (admin == null || !org.mindrot.jbcrypt.BCrypt.checkpw("admin123", admin.getPassword())) {
                    request.setAttribute("error", "Lỗi khi xác thực tài khoản admin mặc định.");
                    request.getRequestDispatcher("/view/common/login.jsp").forward(request, response);
                    return;
                }
            }

            if (admin != null) {
                request.getSession().setAttribute("admin", admin);
                request.getSession().setAttribute("adminId", admin.getAdminId());
                System.out.println("AdminLoginController: Đăng nhập thành công, adminId = " + admin.getAdminId());
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