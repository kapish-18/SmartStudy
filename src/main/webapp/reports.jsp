<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Academic Reports - SmartStudy</title>
    <link rel="stylesheet" href="css/web-style.css">
</head>
<body>
    <nav class="navbar">
        <a href="web-dashboard" class="brand">⚡ SmartStudy Portal</a>
        <div class="nav-links">
            <a href="web-dashboard">Dashboard</a>
            <a href="students">Students</a>
            <a href="web-reports" class="active">Weekly Reports</a>
            <a href="web-login">Logout</a>
        </div>
    </nav>

    <div class="container">
        <div class="page-header">
            <h1>Generated Academic Reports</h1>
            <p>Inspect system-generated weekly TXT progress reports saved to the server disk</p>
        </div>

        <div style="display: grid; grid-template-columns: 320px 1fr; gap: 20px;">
            <div class="card">
                <h3>Available Reports</h3>
                <ul style="list-style: none; margin-top: 12px;">
                    <%
                        List<String> reports = (List<String>) request.getAttribute("reports");
                        String selectedFile = (String) request.getAttribute("selectedFile");
                        if (reports != null && !reports.isEmpty()) {
                            for (String r : reports) {
                                boolean isSelected = r.equals(selectedFile);
                    %>
                    <li style="margin-bottom: 10px; padding: 6px 10px; border-radius: 6px; background-color: <%= isSelected ? "rgba(99, 102, 241, 0.15)" : "transparent" %>; border: 1px solid <%= isSelected ? "var(--primary)" : "transparent" %>;">
                        <a href="web-reports?file=<%= r %>" style="<%= isSelected ? "color: var(--emerald); font-weight: 600;" : "color: var(--primary);" %> text-decoration: none; font-size: 0.9rem;">
                            📄 <%= r %>
                        </a>
                    </li>
                    <%
                            }
                        } else {
                    %>
                    <li style="color: var(--text-muted); font-size: 0.9rem; padding: 12px 0;">No reports found. Generate one from the JavaFX application!</li>
                    <%
                        }
                    %>
                </ul>
            </div>

            <div class="card">
                <h3>Report Content <%= request.getAttribute("selectedFile") != null ? "(" + request.getAttribute("selectedFile") + ")" : "" %></h3>
                <pre style="background: var(--bg-main); border: 1px solid var(--border-color); border-radius: 8px; padding: 16px; margin-top: 12px; white-space: pre-wrap; font-family: 'Consolas', monospace; font-size: 0.85rem; color: #cbd5e1; min-height: 350px; line-height: 1.5;"><%= request.getAttribute("reportContent") != null ? request.getAttribute("reportContent") : "Select a report from the list on the left to view its contents." %></pre>
            </div>
        </div>
    </div>
</body>
</html>
