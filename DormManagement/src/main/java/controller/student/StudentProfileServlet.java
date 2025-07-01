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
import model.entity.StudentParent;
import java.util.List;


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
            if (action == null || action.equals("view")) {
                // View profile
                List<StudentParent> parents = studentDAO.getParentsByStudentID(student.getStudentId());
                request.setAttribute("parents", parents);
                request.getRequestDispatcher("/view/student/profile.jsp").forward(request, response);
            } else if (action.equals("edit")) {
                // Edit profile form
                List<StudentParent> parents = studentDAO.getParentsByStudentID(student.getStudentId());
                request.setAttribute("parents", parents);
                request.getRequestDispatcher("/view/student/profile.jsp").forward(request, response);
            } else if (action.equals("addParent")) {
                // Add parent form
                request.getRequestDispatcher("/view/student/profile.jsp").forward(request, response);
            } else if (action.equals("update")) {
                // Update student and parent info
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

                // Update parents
                String[] parentIds = request.getParameterValues("parentId");
                if (parentIds != null) {
                    for (String parentId : parentIds) {
                        String parentName = request.getParameter("parentName_" + parentId);
                        String parentPhone = request.getParameter("parentPhone_" + parentId);
                        String relationship = request.getParameter("relationship_" + parentId);

                        if (parentPhone != null && !parentPhone.isEmpty() && studentDAO.isParentPhoneExists(parentPhone, Integer.parseInt(parentId))) {
                            request.setAttribute("formErrorMessage", "Số điện thoại của phụ huynh đã tồn tại.");
                            request.getRequestDispatcher("/view/student/profile.jsp").forward(request, response);
                            return;
                        }

                        StudentParent parent = new StudentParent();
                        parent.setStudentParentId(Integer.parseInt(parentId));
                        parent.setStudentId(student.getStudentId());
                        parent.setParentName(parentName);
                        parent.setPhone(parentPhone);
                        parent.setRelationship(relationship);
                        studentDAO.updateParent(parent);
                    }
                }

                response.sendRedirect(request.getContextPath() + "/student/profile?action=view");
            } else if (action.equals("add")) {
                // Add new parent
                String parentName = request.getParameter("parentName");
                String parentPhone = request.getParameter("parentPhone");
                String relationship = request.getParameter("relationship");

                if (parentPhone != null && !parentPhone.isEmpty() && studentDAO.isParentPhoneExists(parentPhone, 0)) {
                    request.setAttribute("formErrorMessage", "Số điện thoại của phụ huynh đã tồn tại.");
                    request.getRequestDispatcher("/view/student/profile.jsp").forward(request, response);
                    return;
                }

                StudentParent parent = new StudentParent();
                parent.setStudentId(student.getStudentId());
                parent.setParentName(parentName);
                parent.setPhone(parentPhone);
                parent.setRelationship(relationship);
                studentDAO.insertParent(parent);

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