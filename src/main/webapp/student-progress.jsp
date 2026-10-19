<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.smartstudy.model.Subject" %>
<%@ page import="com.smartstudy.model.Task" %>
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
            <div style="display: flex; justify-content: space-between; align-items: baseline;">
                <h1>Academic Profile: <%= request.getAttribute("studentName") %></h1>
                <span style="color: var(--text-muted); font-size: 0.95rem;"><%= request.getAttribute("studentEmail") %></span>
            </div>
            <p>Individual student academic progress, registered courses, and assigned coursework</p>
        </div>

        <div class="stats-grid">
            <div class="card">
                <h3>Overall Completion</h3>
                <div class="stat"><%= request.getAttribute("overallProgress") %></div>
            </div>
            <div class="card">
                <h3>Total Study Logged</h3>
                <div class="stat" style="color: var(--emerald);"><%= request.getAttribute("studyHours") %> hrs</div>
            </div>
            <div class="card">
                <h3>Active Tasks Pending</h3>
                <div class="stat" style="color: var(--amber);"><%= request.getAttribute("pendingTasks") %></div>
            </div>
            <div class="card">
                <h3>Completed Tasks</h3>
                <div class="stat"><%= request.getAttribute("completedTasks") %></div>
            </div>
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
                        if (subjects != null && !subjects.isEmpty()) {
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
                        } else {
                    %>
                    <tr>
                        <td colspan="4" style="text-align: center; color: var(--text-muted); padding: 16px;">No enrolled subjects found for this student.</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <div class="card" style="margin-bottom: 24px;">
            <h3>Tasks &amp; Coursework</h3>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Task Title</th>
                        <th>Priority</th>
                        <th>Deadline</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Task> tasks = (List<Task>) request.getAttribute("tasks");
                        if (tasks != null && !tasks.isEmpty()) {
                            for (Task t : tasks) {
                                String statusColor = "COMPLETED".equalsIgnoreCase(t.getStatus()) ? "var(--emerald)" : "var(--amber)";
                    %>
                    <tr>
                        <td><strong><%= t.getTitle() %></strong></td>
                        <td>Priority <%= t.getPriority() %></td>
                        <td><%= t.getDeadline() != null ? t.getDeadline().toString() : "No deadline" %></td>
                        <td><span style="color: <%= statusColor %>; font-weight: 600;"><%= t.getStatus() %></span></td>
                    </tr>
                    <%
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="4" style="text-align: center; color: var(--text-muted); padding: 16px;">No tasks assigned or created yet.</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <a href="students" class="btn" style="background-color: #334155;">&larr; Back to Student Roster</a>
    </div>
</body>
</html>
