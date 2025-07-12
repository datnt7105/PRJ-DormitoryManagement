package controller.common;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.service.UserService;
import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String cccd = request.getParameter("cccd");
        String phone = request.getParameter("phone");
        String dob = request.getParameter("dob"); // Lấy ngày sinh từ form
        String gender = request.getParameter("gender"); // Lấy giới tính từ form

        try {
            boolean success = userService.registerUser(username, email, password, fullName, cccd, phone, dob, gender);
            if (success) {
                // Đăng ký thành công, chuyển sang dashboard student
                response.sendRedirect(request.getContextPath() + "/view/student/dashboardStudent.jsp");
            } else {
                // Đăng ký thất bại, có thể do trùng username/email
                request.setAttribute("error", "Đăng ký thất bại! Tài khoản, email hoặc số điện thoại đã tồn tại.");
                request.getRequestDispatcher("view/common/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("view/common/register.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/view/common/register.jsp");
    }
} 