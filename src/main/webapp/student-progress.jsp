<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.smartstudy.model.Subject" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Progress - SmartStudy</title>
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
            <h1>Academic Progress: <%= request.getAttribute("studentName") %></h1>
            <p>Overall Completion: <strong style="color: var(--primary);"><%= request.getAttribute("overallProgress") %></strong></p>
        </div>

        <div class="card" style="margin-bottom: 24px;">
            <h3>Enrolled Subject Breakdown</h3>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Subject Name</th>
                        <th>Course Code</th>
                        <th>Credits</th>
                        <th>Target Marks</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Subject> subjects = (List<Subject>) request.getAttribute("subjects");
                        if (subjects != null) {
                            for (Subject sub : subjects) {
                    %>
                    <tr>
                        <td><strong><%= sub.getName() %></strong></td>
                        <td><%= sub.getCourseCode() %></td>
                        <td><%= sub.getCredits() %></td>
                        <td><%= sub.getTargetMarks() %>%</td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>

        <a href="students" class="btn" style="background-color: #334155;">&larr; Back to Student Roster</a>
    </div>
</body>
</html>
