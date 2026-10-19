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
            <div style="color: var(--rose); margin-bottom: 16px; font-size: 0.9rem; padding: 10px; background-color: rgba(239, 68, 68, 0.1); border-radius: 6px; border: 1px solid var(--rose);">
                <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>

        <form action="web-login" method="post">
            <div class="form-group">
                <label>Faculty Email</label>
                <input type="email" name="email" class="form-control" value="sharma@smartstudy.edu" placeholder="sharma@smartstudy.edu" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" name="password" class="form-control" value="pass123" placeholder="pass123" required>
            </div>
            <button type="submit" class="btn" style="width: 100%; margin-top: 8px; padding: 12px; font-weight: 600;">Sign In to Portal</button>
            <p style="text-align: center; margin-top: 16px; font-size: 0.8rem; color: var(--text-muted);">
                Faculty credentials prefilled: <strong style="color: var(--primary);">sharma@smartstudy.edu</strong> / <strong style="color: var(--primary);">pass123</strong>
            </p>
        </form>
    </div>
</body>
</html>
