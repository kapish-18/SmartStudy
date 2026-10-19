package com.smartstudy.servlet;

import com.smartstudy.model.User;
import com.smartstudy.service.ProgressService;
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
 * Servlet for Teacher/Admin Web Portal Dashboard.
 * Computes live cohort-wide academic analytics across all enrolled students from MySQL.
 */
@WebServlet("/web-dashboard")
public class DashboardServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final ProgressService progressService = new ProgressService();
    private final TaskService taskService = new TaskService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<User> students = userService.getUsersByRole("STUDENT");

            double totalProgressSum = 0.0;
            double totalHoursSum = 0.0;
            int totalPending = 0;
            int totalCompleted = 0;

            if (!students.isEmpty()) {
                for (User s : students) {
                    try {
                        totalProgressSum += progressService.calculateOverallProgress(s.getId());
                        totalHoursSum += progressService.calculateTotalStudyHours(s.getId());
                        totalPending += taskService.getPendingCount(s.getId());
                        totalCompleted += taskService.getCompletedCount(s.getId());
                    } catch (Exception ignored) {}
                }
                double avgProgress = totalProgressSum / students.size();
                req.setAttribute("avgProgress", String.format("%.1f", avgProgress));
                req.setAttribute("totalHours", String.format("%.1f", totalHoursSum));
                req.setAttribute("pendingTasks", totalPending);
                req.setAttribute("completedTasks", totalCompleted);
            } else {
                req.setAttribute("avgProgress", "0.0");
                req.setAttribute("totalHours", "0.0");
                req.setAttribute("pendingTasks", 0);
                req.setAttribute("completedTasks", 0);
            }
        } catch (Exception e) {
            req.setAttribute("avgProgress", "0.0");
            req.setAttribute("totalHours", "0.0");
            req.setAttribute("pendingTasks", 0);
            req.setAttribute("completedTasks", 0);
        }

        req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
    }
}
