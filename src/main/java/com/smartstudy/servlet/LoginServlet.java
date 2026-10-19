package com.smartstudy.servlet;

import com.smartstudy.exception.InvalidLoginException;
import com.smartstudy.model.Teacher;
import com.smartstudy.model.User;
import com.smartstudy.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for Teacher/Admin Web Portal authentication.
 * Includes built-in demo fallback credentials for offline presentations.
 */
@WebServlet("/web-login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    // Demo fallback credentials (used when MySQL is unavailable)
    private static final String DEMO_EMAIL = "sharma@smartstudy.edu";
    private static final String DEMO_PASSWORD = "pass123";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            // Try database authentication first
            User user = userService.login(email, password);
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userName", user.getName());
            resp.sendRedirect(req.getContextPath() + "/web-dashboard");
        } catch (Exception e) {
            // Database unavailable or credentials didn't match DB — try demo fallback
            if (DEMO_EMAIL.equalsIgnoreCase(email != null ? email.trim() : "")
                    && DEMO_PASSWORD.equals(password)) {
                // Demo fallback: create a mock Teacher object
                Teacher demoTeacher = new Teacher();
                demoTeacher.setId(2);
                demoTeacher.setName("Prof. Sharma");
                demoTeacher.setEmail(DEMO_EMAIL);
                demoTeacher.setRole("TEACHER");

                HttpSession session = req.getSession();
                session.setAttribute("user", demoTeacher);
                session.setAttribute("userName", "Prof. Sharma (Demo Mode)");
                resp.sendRedirect(req.getContextPath() + "/web-dashboard");
            } else {
                req.setAttribute("errorMessage", "Invalid email or password.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        }
    }
}
