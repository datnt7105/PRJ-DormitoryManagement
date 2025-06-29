package controller.common;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.service.AdminService;
import model.entity.Admin;

public class InitServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(InitServlet.class.getName());
    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        try {
            LOGGER.log(Level.INFO, "InitServlet: Bắt đầu khởi tạo...");
            adminService = new AdminService();

            boolean adminExists = adminService.checkIfAdminExists();
            LOGGER.log(Level.INFO, "InitServlet: Admin đã tồn tại? {0}", adminExists);

            if (!adminExists) {
                LOGGER.log(Level.INFO, "InitServlet: Chưa có admin, đang tạo tài khoản mặc định...");

                Admin defaultAdmin = new Admin();
                defaultAdmin.setUsername("admin");
                defaultAdmin.setPassword("admin123"); // password sẽ được hash
                defaultAdmin.setFullName("Default Admin");
                defaultAdmin.setEmail("admin@example.com");

                int adminID = adminService.createAdmin(defaultAdmin);
                if(adminID != -1){
                    defaultAdmin.setAdminId(adminID);// references adminID to Admin
                    LOGGER.log(Level.INFO,"InitServlet: created an defaulted admin account:"
                            + "username=admin, password = admin123, adminID={0}", adminID);
                }else{
                    LOGGER.log(Level.INFO, "InitServlet: can't created defaulted admin account");
                    throw new ServletException("Can not create an defaulted account!!!");
                }
                defaultAdmin.setAdminId(adminID); 

                LOGGER.log(Level.INFO, "InitServlet: Đã tạo tài khoản admin mặc định: username=admin, password=admin123");
            } else {
                LOGGER.log(Level.INFO, "InitServlet: Đã có tài khoản admin trong database.");
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "InitServlet: Lỗi khi tạo tài khoản admin: {0}", e.getMessage());
            throw new ServletException("Không thể tạo tài khoản admin mặc định", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().write("Khởi tạo ứng dụng hoàn tất.");
    }
}
