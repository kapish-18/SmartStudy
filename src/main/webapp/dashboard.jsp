<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Faculty Dashboard - SmartStudy</title>
    <link rel="stylesheet" href="css/web-style.css">
</head>
<body>
    <nav class="navbar">
        <a href="web-dashboard" class="brand">⚡ SmartStudy Portal</a>
        <div class="nav-links">
            <a href="web-dashboard" class="active">Dashboard</a>
            <a href="students">Students</a>
            <a href="web-reports">Weekly Reports</a>
            <a href="web-login">Logout</a>
        </div>
    </nav>

    <div class="container">
        <div class="page-header">
            <div style="display: flex; justify-content: space-between; align-items: baseline;">
                <h1>Academic Overview &amp; Batch Analytics</h1>
                <span style="color: var(--primary); font-weight: 600; font-size: 1rem;">
                    <%= session.getAttribute("userName") != null ? session.getAttribute("userName") : "Prof. Sharma" %>
                </span>
            </div>
            <p>Teacher portal for tracking student cohort progress and completion metrics</p>
        </div>

        <div class="stats-grid">
            <div class="card">
                <h3>Cohort Avg. Progress</h3>
                <div class="stat"><%= request.getAttribute("avgProgress") != null ? request.getAttribute("avgProgress") : "0.0" %>%</div>
            </div>
            <div class="card">
                <h3>Total Study Logged</h3>
                <div class="stat" style="color: var(--emerald);"><%= request.getAttribute("totalHours") != null ? request.getAttribute("totalHours") : "0.0" %> hrs</div>
            </div>
            <div class="card">
                <h3>Completed Tasks</h3>
                <div class="stat"><%= request.getAttribute("completedTasks") != null ? request.getAttribute("completedTasks") : "0" %></div>
            </div>
            <div class="card">
                <h3>Active Tasks Pending</h3>
                <div class="stat" style="color: var(--amber);"><%= request.getAttribute("pendingTasks") != null ? request.getAttribute("pendingTasks") : "0" %></div>
            </div>
        </div>

        <div class="card">
            <h3>Quick Actions</h3>
            <p style="margin: 12px 0 16px 0; color: var(--text-muted);">Inspect individual student academic profiles or review system generated weekly reports.</p>
            <a href="students" class="btn" style="margin-right: 12px;">View Student Roster</a>
            <a href="web-reports" class="btn" style="background-color: #334155;">View Weekly Reports</a>
        </div>
    </div>
</body>
</html>
