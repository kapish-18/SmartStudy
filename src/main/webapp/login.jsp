<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>SmartStudy Web Portal - Login</title>
    <link rel="stylesheet" href="css/web-style.css">
</head>
<body>
    <div class="login-box">
        <h2 style="margin-bottom: 8px;">⚡ SmartStudy</h2>
        <p style="color: var(--text-muted); margin-bottom: 24px; font-size: 0.9rem;">Teacher / Administrator Web Portal</p>

        <% if (request.getAttribute("errorMessage") != null) { %>
            <div style="color: var(--rose); margin-bottom: 16px; font-size: 0.9rem;">
                <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>

        <form action="web-login" method="post">
            <div class="form-group">
                <label>Faculty Email</label>
                <input type="email" name="email" class="form-control" value="sharma@smartstudy.edu" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" class="form-control" value="pass123" required>
            </div>
            <button type="submit" class="btn" style="width: 100%; margin-top: 8px;">Sign In to Portal</button>
        </form>
    </div>
</body>
</html>
