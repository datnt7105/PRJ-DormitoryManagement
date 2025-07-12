package controller.student;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.StudentDAO;
import model.entity.Students;
import java.util.List;


@WebServlet("/student/profile")
public class StudentProfileServlet extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("student") == null) {
            response.sendRedirect(request.getContextPath() + "/view/common/login.jsp");
            return;
        }

        Students student = (Students) session.getAttribute("student");
        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("view") || action.equals("edit")) {
                // Chỉ forward đến trang profile, không xử lý phụ huynh
                request.getRequestDispatcher("/view/student/profile.jsp").forward(request, response);
            } else if (action.equals("update")) {
                // Update student info
                String fullName = request.getParameter("fullName");
                String gender = request.getParameter("gender");
                String address = request.getParameter("address");
                String phone = request.getParameter("phone");
                String dob = request.getParameter("dob");

                student.setFullName(fullName);
                student.setGender(gender);
                student.setAddress(address);
                student.setPhone(phone);
                if (dob != null && !dob.isEmpty()) {
                    student.setDob(Date.valueOf(dob));
                }

                // Validate phone
                if (phone != null && !phone.isEmpty() && studentDAO.isPhoneExists(phone)) {
                    request.setAttribute("formErrorMessage", "Số điện thoại đã tồn tại.");
                    request.getRequestDispatcher("/view/student/profile.jsp").forward(request, response);
                    return;
                }

                studentDAO.updateStudentProfile(student);
                session.setAttribute("student", student);
                response.sendRedirect(request.getContextPath() + "/student/profile?action=view");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("formErrorMessage", "Lỗi khi xử lý thông tin: " + e.getMessage());
            request.getRequestDispatcher("/view/student/profile.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles student profile operations";
    }
}