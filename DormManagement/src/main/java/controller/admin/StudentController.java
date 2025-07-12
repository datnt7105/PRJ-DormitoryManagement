package controller.admin;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import model.dao.StudentDAO;
import model.entity.Students;
import model.service.StudentService;

@WebServlet("/student-manager")
public class StudentController extends HttpServlet {
    private StudentDAO studentDAO;
    private StudentService studentService;

    @Override
    public void init() throws ServletException {
        studentDAO = new StudentDAO();
        studentService = new StudentService();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String formType = request.getParameter("formType");
        try {
            if ("add".equals(formType)) {
                Students s = extractStudentFromRequest(request, false);
                boolean success = studentService.add(s);
                if (success) {
                    request.getSession().setAttribute("message", "Thêm sinh viên thành công!");
                    response.sendRedirect("student-manager");
                } else {
                    request.setAttribute("error", "Thêm sinh viên thất bại! Vui lòng kiểm tra lại dữ liệu.");
                    request.getRequestDispatcher("/view/admin/addStudent.jsp").forward(request, response);
                }
                return;
            } else if ("edit".equals(formType)) {
                Students s = extractStudentFromRequest(request, true);
                boolean success = studentService.update(s);
                if (success) {
                    request.getSession().setAttribute("message", "Cập nhật sinh viên thành công!");
                } else {
                    request.getSession().setAttribute("error", "Cập nhật thất bại! Vui lòng kiểm tra lại dữ liệu.");
                }
                response.sendRedirect("student-manager");
            } else {
                // Nếu không có formType, forward về danh sách
                listStudents(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/view/admin/Students.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("addForm".equals(action)) {
                showAddForm(request, response);
            } else if ("editForm".equals(action)) {
                showEditForm(request, response);
            } else if ("delete".equals(action)) {
                deleteStudent(request, response);
            } else {
                listStudents(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/view/admin/Students.jsp").forward(request, response);
        }
    }

    // Các method private cho từng action
    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("formType", "add");
        request.getRequestDispatcher("/view/admin/addStudent.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Students student = studentDAO.getById(id);
        request.setAttribute("studentEdit", student);
        request.setAttribute("formType", "edit");
        request.getRequestDispatcher("/view/admin/Students.jsp").forward(request, response);
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        studentDAO.delete(id);
        request.getSession().setAttribute("message", "Xóa sinh viên thành công!");
        response.sendRedirect("student-manager");
    }

    private void listStudents(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword") != null ? request.getParameter("keyword") : "";
        String gender = request.getParameter("gender") != null ? request.getParameter("gender") : "";
        List<Students> students = studentDAO.getAll(keyword, gender); // lấy theo tìm kiếm
        if (!keyword.isEmpty() || !gender.isEmpty()) {
            request.setAttribute("message", "Tìm kiếm thành công!");
        }
        request.setAttribute("students", students);
        request.getRequestDispatcher("/view/admin/Students.jsp").forward(request, response);
    }

    /**
     * Lấy dữ liệu sinh viên từ request, nếu isEdit=true thì lấy cả studentId và statusRoom
     */
    private Students extractStudentFromRequest(HttpServletRequest request, boolean isEdit) {
        Students s = new Students();
        if (isEdit) {
            s.setStudentId(Integer.parseInt(request.getParameter("studentId")));
            s.setStatusRoom(request.getParameter("statusRoom"));
        } else {
            s.setStatusRoom("Active");
        }
        s.setUsername(request.getParameter("username"));
        s.setPassword(request.getParameter("password"));
        s.setEmail(request.getParameter("email"));
        s.setFullName(request.getParameter("fullName"));
        s.setDob(Date.valueOf(request.getParameter("dob")));
        s.setGender(request.getParameter("gender"));
        s.setPhone(request.getParameter("phone"));
        s.setAddress(request.getParameter("address"));
        return s;
    }
} 

    