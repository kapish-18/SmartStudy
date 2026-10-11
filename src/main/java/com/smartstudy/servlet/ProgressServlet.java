package com.smartstudy.servlet;

import com.smartstudy.model.Subject;
import com.smartstudy.service.ProgressService;
import com.smartstudy.service.SubjectService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for viewing individual student progress in the web portal.
 */
@WebServlet("/student-progress")
public class ProgressServlet extends HttpServlet {

    private final SubjectService subjectService = new SubjectService();
    private final ProgressService progressService = new ProgressService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentIdParam = req.getParameter("id");
        int studentId = (studentIdParam != null && !studentIdParam.isEmpty()) ? Integer.parseInt(studentIdParam) : 1;

        List<Subject> subjects = subjectService.getSubjectsByStudentId(studentId);
        if (subjects.isEmpty()) {
            subjects = List.of(
                new Subject(1, studentId, "Database Management Systems", "CSE3001", 4, 85.0),
                new Subject(2, studentId, "Design & Analysis of Algorithms", "CSE3002", 4, 90.0),
                new Subject(3, studentId, "Advanced Java Programming", "CSE3003", 3, 95.0)
            );
        }

        req.setAttribute("subjects", subjects);
        req.setAttribute("studentName", "Alex Johnson");
        req.setAttribute("overallProgress", "74.2%");
        req.getRequestDispatcher("/student-progress.jsp").forward(req, resp);
    }
}
