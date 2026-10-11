package com.smartstudy.servlet;

import com.smartstudy.util.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for viewing generated reports in the teacher/admin portal.
 */
@WebServlet("/web-reports")
public class ReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> reportFiles = FileUtil.listReports();
        req.setAttribute("reports", reportFiles);

        String selectedReport = req.getParameter("file");
        if (selectedReport != null && !selectedReport.trim().isEmpty()) {
            try {
                String content = FileUtil.readReport(selectedReport.trim());
                req.setAttribute("selectedFile", selectedReport);
                req.setAttribute("reportContent", content);
            } catch (IOException e) {
                req.setAttribute("reportContent", "Error reading report: " + e.getMessage());
            }
        }

        req.getRequestDispatcher("/reports.jsp").forward(req, resp);
    }
}
