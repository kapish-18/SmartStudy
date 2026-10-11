package com.smartstudy.servlet;

import com.smartstudy.model.Student;
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
 */
@WebServlet("/students")
public class StudentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Alex Johnson", "alex@smartstudy.edu", "pass123", "2024-CSE-042"));
        students.add(new Student(2, "Rohan Patel", "rohan@smartstudy.edu", "pass123", "2024-CSE-089"));
        students.add(new Student(3, "Sneha Reddy", "sneha@smartstudy.edu", "pass123", "2024-CSE-112"));

        req.setAttribute("students", students);
        req.getRequestDispatcher("/students.jsp").forward(req, resp);
    }
}
