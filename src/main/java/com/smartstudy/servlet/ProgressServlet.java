package com.smartstudy.servlet;

import com.smartstudy.model.Subject;
import com.smartstudy.model.User;
import com.smartstudy.service.ProgressService;
import com.smartstudy.service.SubjectService;
import com.smartstudy.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for viewing individual student progress in the web portal.
 * Reads real student profile, subjects, and analytics from MySQL database.
 */
@WebServlet("/student-progress")
public class ProgressServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final SubjectService subjectService = new SubjectService();
    private final ProgressService progressService = new ProgressService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentIdParam = req.getParameter("id");
        int studentId = 1;
        try {
            if (studentIdParam != null && !studentIdParam.trim().isEmpty()) {
                studentId = Integer.parseInt(studentIdParam.trim());
            }
        } catch (NumberFormatException ignored) {}

        User user = userService.findById(studentId);
        String studentName = (user != null) ? user.getName() : "Student #" + studentId;

        List<Subject> subjects = subjectService.getSubjectsByStudentId(studentId);
        double overall = 0.0;
        try {
            overall = progressService.calculateOverallProgress(studentId);
        } catch (Exception ignored) {}

        req.setAttribute("subjects", subjects);
        req.setAttribute("studentName", studentName);
        req.setAttribute("overallProgress", String.format("%.1f%%", overall));
        req.getRequestDispatcher("/student-progress.jsp").forward(req, resp);
    }
}
