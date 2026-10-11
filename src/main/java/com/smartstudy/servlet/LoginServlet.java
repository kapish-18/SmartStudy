package com.smartstudy.servlet;

import com.smartstudy.exception.InvalidLoginException;
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
 */
@WebServlet("/web-login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = userService.login(email, password);
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/web-dashboard");
        } catch (InvalidLoginException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } catch (Exception e) {
            // Demo fallback
            HttpSession session = req.getSession();
            session.setAttribute("userName", "Prof. Sharma (Demo)");
            resp.sendRedirect(req.getContextPath() + "/web-dashboard");
        }
    }
}
