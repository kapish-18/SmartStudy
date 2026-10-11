<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.smartstudy.model.Student" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Roster - SmartStudy</title>
    <link rel="stylesheet" href="css/web-style.css">
</head>
<body>
    <nav class="navbar">
        <a href="web-dashboard" class="brand">⚡ SmartStudy Portal</a>
        <div class="nav-links">
            <a href="web-dashboard">Dashboard</a>
            <a href="students" class="active">Students</a>
            <a href="web-reports">Weekly Reports</a>
            <a href="web-login">Logout</a>
        </div>
    </nav>

    <div class="container">
        <div class="page-header">
            <h1>Enrolled Students</h1>
            <p>Select a student to view course progress, logged study sessions, and recommendations</p>
        </div>

        <table class="data-table">
            <thead>
                <tr>
                    <th>Roll Number</th>
                    <th>Full Name</th>
                    <th>Institutional Email</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Student> students = (List<Student>) request.getAttribute("students");
                    if (students != null) {
                        for (Student s : students) {
                %>
                <tr>
                    <td><%= s.getRollNumber() %></td>
                    <td><strong><%= s.getName() %></strong></td>
                    <td><%= s.getEmail() %></td>
                    <td>
                        <a href="student-progress?id=<%= s.getId() %>" class="btn" style="padding: 6px 12px; font-size: 0.8rem;">View Progress</a>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
