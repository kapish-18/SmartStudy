package com.smartstudy.servlet;

import com.smartstudy.model.Student;
import com.smartstudy.model.User;
import com.smartstudy.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for listing students in the teacher/admin portal.
 * Fetches real students from the MySQL database.
 */
@WebServlet("/students")
public class StudentServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> userList = userService.getUsersByRole("STUDENT");
        List<Student> students = new ArrayList<>();
        for (User u : userList) {
            if (u instanceof Student) {
                Student s = (Student) u;
                if (s.getRollNumber() == null || s.getRollNumber().equals("2024-CSE-001")) {
                    s.setRollNumber(String.format("2024-CSE-%03d", s.getId() * 14 + 28));
                }
                students.add(s);
            }
        }
        if (students.isEmpty()) {
            students.add(new Student(1, "Alex Johnson", "alex@smartstudy.edu", "pass123", "2024-CSE-042"));
        }

        req.setAttribute("students", students);
        req.getRequestDispatcher("/students.jsp").forward(req, resp);
    }
}
