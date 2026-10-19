package com.smartstudy.servlet;

import com.smartstudy.model.Subject;
import com.smartstudy.model.Task;
import com.smartstudy.model.User;
import com.smartstudy.service.ProgressService;
import com.smartstudy.service.SubjectService;
import com.smartstudy.service.TaskService;
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
 * Reads real student profile, subjects, tasks, and analytics from MySQL database.
 */
@WebServlet("/student-progress")
public class ProgressServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final SubjectService subjectService = new SubjectService();
    private final ProgressService progressService = new ProgressService();
    private final TaskService taskService = new TaskService();

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
        String studentEmail = (user != null) ? user.getEmail() : "";

        List<Subject> subjects = subjectService.getSubjectsByStudentId(studentId);
        List<Task> tasks = taskService.getTasksByStudentId(studentId);
        double overall = 0.0;
        double studyHours = 0.0;
        int pendingTasks = 0;
        int completedTasks = 0;

        try {
            overall = progressService.calculateOverallProgress(studentId);
            studyHours = progressService.calculateTotalStudyHours(studentId);
            pendingTasks = taskService.getPendingCount(studentId);
            completedTasks = taskService.getCompletedCount(studentId);
        } catch (Exception ignored) {}

        req.setAttribute("studentId", studentId);
        req.setAttribute("studentName", studentName);
        req.setAttribute("studentEmail", studentEmail);
        req.setAttribute("subjects", subjects);
        req.setAttribute("tasks", tasks);
        req.setAttribute("overallProgress", String.format("%.1f%%", overall));
        req.setAttribute("studyHours", String.format("%.1f", studyHours));
        req.setAttribute("pendingTasks", pendingTasks);
        req.setAttribute("completedTasks", completedTasks);

        req.getRequestDispatcher("/student-progress.jsp").forward(req, resp);
    }
}
