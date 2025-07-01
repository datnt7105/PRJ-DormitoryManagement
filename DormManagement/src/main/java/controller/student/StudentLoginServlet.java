/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.student;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.StudentDAO;
import model.entity.Students;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author HP
 */
public class StudentLoginServlet extends HttpServlet {
    private StudentDAO studentDAO;
    
    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
    }
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // Chuyển hướng đến trang đăng nhập
        request.getRequestDispatcher("/view/common/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String username = request.getParameter("emailOrUsername");
        String password = request.getParameter("password");
        
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            request.getRequestDispatcher("/view/common/login.jsp").forward(request, response);
            return;
        }
        
        try {
            // Tìm sinh viên theo username hoặc email
            Students student = studentDAO.findStudentByEmailOrUsername(username);
            
            if (student != null) {
                // Kiểm tra mật khẩu
                if (BCrypt.checkpw(password, student.getPassword())) {
                    // Đăng nhập thành công
                    HttpSession session = request.getSession();
                    session.setAttribute("student", student);
                    session.setAttribute("studentId", student.getStudentId());
                    session.setAttribute("userType", "student");
                    
                    // Điều hướng đến dashboard
                    response.sendRedirect(request.getContextPath() + "/view/student/dashboardStudent.jsp");
                } else {
                    request.setAttribute("error", "Mật khẩu không đúng.");
                    request.getRequestDispatcher("/view/common/login.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "Tên đăng nhập hoặc email không tồn tại.");
                request.getRequestDispatcher("/view/common/login.jsp").forward(request, response);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi đăng nhập: " + e.getMessage());
            request.getRequestDispatcher("/view/common/login.jsp").forward(request, response);
        }
    }
}
