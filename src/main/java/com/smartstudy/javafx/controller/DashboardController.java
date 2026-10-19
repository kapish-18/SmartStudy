package com.smartstudy.javafx.controller;

import com.smartstudy.Main;
import com.smartstudy.javafx.ThreadManager;
import com.smartstudy.model.Exam;
import com.smartstudy.model.Task;
import com.smartstudy.service.ProgressService;
import com.smartstudy.service.RecommendationService;
import com.smartstudy.service.TaskService;
import com.smartstudy.util.FileUtil;
import com.smartstudy.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for the Main Student Dashboard.
 * Integrates service layers, recommendations, and background multithreading report generation.
 * All data is fetched from the MySQL database — no hardcoded fallback values.
 */
public class DashboardController {

    @FXML private Label studentNameLabel;
    @FXML private Label currentDateLabel;
    @FXML private Label overallProgressLabel;
    @FXML private Label totalStudyHoursLabel;
    @FXML private Label pendingTasksCountLabel;
    @FXML private Label upcomingExamsCountLabel;

    @FXML private VBox recommendationContainer;
    @FXML private ListView<String> urgentTasksListView;

    // Report Generation UI (Multithreading demonstration)
    @FXML private ProgressBar reportProgressBar;
    @FXML private Label reportStatusLabel;
    @FXML private Button generateReportBtn;
    @FXML private TextArea reportPreviewArea;

    private final ProgressService progressService;
    private final RecommendationService recommendationService;
    private final TaskService taskService;
    private final ThreadManager threadManager;

    public DashboardController() {
        this.progressService = new ProgressService();
        this.recommendationService = new RecommendationService();
        this.taskService = new TaskService();
        this.threadManager = new ThreadManager();
    }

    @FXML
    public void initialize() {
        // Redirect to login if not authenticated
        if (!UserSession.getInstance().isLoggedIn()) {
            Main.switchScene("login.fxml");
            return;
        }

        int studentId = UserSession.getInstance().getCurrentUserId();
        String studentName = UserSession.getInstance().getCurrentUserName();

        // 1. Setup Student Header
        if (studentNameLabel != null) {
            studentNameLabel.setText("Welcome back, " + studentName + "!");
        }
        if (currentDateLabel != null) {
            currentDateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        }

        // 2. Load Metric Counters
        loadMetrics(studentId);

        // 3. Load Rule-Based Recommendations
        loadRecommendations(studentId);

        // 4. Load Prioritized Urgent Tasks (PriorityQueue demonstration)
        loadUrgentTasks(studentId);

        // Hide progress bar initially
        if (reportProgressBar != null) {
            reportProgressBar.setVisible(false);
        }
        if (reportStatusLabel != null) {
            reportStatusLabel.setText("");
        }
    }

    private void loadMetrics(int studentId) {
        try {
            double overallProgress = progressService.calculateOverallProgress(studentId);
            if (overallProgressLabel != null) {
                overallProgressLabel.setText(String.format("%.1f%%", overallProgress));
            }

            double studyHours = progressService.calculateTotalStudyHours(studentId);
            if (totalStudyHoursLabel != null) {
                totalStudyHoursLabel.setText(String.format("%.1f hrs", studyHours));
            }

            List<Task> pending = taskService.getPendingTasks(studentId);
            if (pendingTasksCountLabel != null) {
                pendingTasksCountLabel.setText(String.valueOf(pending.size()));
            }

            List<Exam> upcoming = progressService.getUpcomingExams(studentId);
            if (upcomingExamsCountLabel != null) {
                upcomingExamsCountLabel.setText(String.valueOf(upcoming.size()));
            }
        } catch (Exception e) {
            System.err.println("Error loading dashboard metrics: " + e.getMessage());
            if (overallProgressLabel != null) overallProgressLabel.setText("N/A");
            if (totalStudyHoursLabel != null) totalStudyHoursLabel.setText("N/A");
            if (pendingTasksCountLabel != null) pendingTasksCountLabel.setText("N/A");
            if (upcomingExamsCountLabel != null) upcomingExamsCountLabel.setText("N/A");
        }
    }

    private void loadRecommendations(int studentId) {
        if (recommendationContainer == null) return;
        recommendationContainer.getChildren().clear();

        try {
            List<String> recommendations = recommendationService.generateRecommendations(studentId);

            if (recommendations.isEmpty()) {
                Label noRec = new Label("All subjects on track! Keep up the great consistency.");
                noRec.getStyleClass().add("text-muted");
                recommendationContainer.getChildren().add(noRec);
            } else {
                for (String rec : recommendations) {
                    Label recCard = new Label("💡 " + rec);
                    recCard.setWrapText(true);
                    recCard.getStyleClass().add("recommendation-badge");
                    recommendationContainer.getChildren().add(recCard);
                }
            }
        } catch (Exception e) {
            Label errorLbl = new Label("Could not load recommendations. Check database connection.");
            errorLbl.getStyleClass().add("text-muted");
            recommendationContainer.getChildren().add(errorLbl);
        }
    }

    private void loadUrgentTasks(int studentId) {
        if (urgentTasksListView == null) return;
        urgentTasksListView.getItems().clear();

        try {
            List<Task> prioritized = recommendationService.getPrioritizedTasks(studentId);
            if (prioritized.isEmpty()) {
                urgentTasksListView.getItems().add("No pending tasks. Great job!");
            } else {
                for (Task t : prioritized) {
                    urgentTasksListView.getItems().add(
                        String.format("[%s] %s (Due: %s, Est: %d mins)", 
                            t.getPriority(), t.getTitle(), t.getDeadline(), t.getEstimatedMinutes())
                    );
                }
            }
        } catch (Exception e) {
            urgentTasksListView.getItems().add("Could not load tasks. Check database connection.");
        }
    }

    /**
     * Multithreading Demo: Generates weekly text report on a background thread.
     */
    @FXML
    private void handleGenerateReportAsync() {
        int studentId = UserSession.getInstance().getCurrentUserId();
        String studentName = UserSession.getInstance().getCurrentUserName();

        if (reportProgressBar != null) {
            reportProgressBar.setVisible(true);
            reportProgressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        }
        if (reportStatusLabel != null) {
            reportStatusLabel.setStyle("-fx-text-fill: #3B82F6;");
            reportStatusLabel.setText("Starting background report generator...");
        }
        if (generateReportBtn != null) {
            generateReportBtn.setDisable(true);
        }

        javafx.concurrent.Task<String> backgroundTask = threadManager.generateReportAsync(studentId, studentName);

        // Bind progress and message to UI labels
        if (reportProgressBar != null) {
            reportProgressBar.progressProperty().bind(backgroundTask.progressProperty());
        }
        if (reportStatusLabel != null) {
            reportStatusLabel.textProperty().bind(backgroundTask.messageProperty());
        }

        backgroundTask.setOnSucceeded(e -> {
            String fileName = backgroundTask.getValue();
            if (generateReportBtn != null) generateReportBtn.setDisable(false);
            if (reportStatusLabel != null) {
                reportStatusLabel.textProperty().unbind();
                reportStatusLabel.setStyle("-fx-text-fill: #10B981;");
                reportStatusLabel.setText("Report generated: " + fileName);
            }
            // Read generated report and display
            try {
                String content = FileUtil.readReport(fileName);
                if (reportPreviewArea != null) {
                    reportPreviewArea.setText(content);
                }
            } catch (IOException ex) {
                System.err.println("Failed to read report: " + ex.getMessage());
            }
        });

        backgroundTask.setOnFailed(e -> {
            if (generateReportBtn != null) generateReportBtn.setDisable(false);
            if (reportStatusLabel != null) {
                reportStatusLabel.textProperty().unbind();
                reportStatusLabel.setStyle("-fx-text-fill: #EF4444;");
                reportStatusLabel.setText("Failed to generate report: " + backgroundTask.getException().getMessage());
            }
        });
    }

    /**
     * Demonstrates Java File I/O: Reads previous reports from disk.
     */
    @FXML
    private void handleViewPreviousReport() {
        List<String> reports = FileUtil.listReports();
        if (reports.isEmpty()) {
            if (reportPreviewArea != null) {
                reportPreviewArea.setText("No previous reports found in the reports/ directory.\nClick 'Generate Weekly Report' to create one!");
            }
            return;
        }

        String latest = reports.get(reports.size() - 1);
        try {
            String content = FileUtil.readReport(latest);
            if (reportPreviewArea != null) {
                reportPreviewArea.setText("=== Previously Saved Report (" + latest + ") ===\n\n" + content);
            }
            if (reportStatusLabel != null) {
                reportStatusLabel.setStyle("-fx-text-fill: #10B981;");
                reportStatusLabel.setText("Loaded report from file: " + latest);
            }
        } catch (IOException e) {
            if (reportPreviewArea != null) {
                reportPreviewArea.setText("Error reading file: " + e.getMessage());
            }
        }
    }

    // Navigation handlers
    @FXML private void navToDashboard() { Main.switchScene("dashboard.fxml"); }
    @FXML private void navToSubjects() { Main.switchScene("subjects.fxml"); }
    @FXML private void navToTasks() { Main.switchScene("tasks.fxml"); }
    @FXML private void navToExams() { Main.switchScene("exams.fxml"); }
    @FXML private void navToStudy() { Main.switchScene("study.fxml"); }
    @FXML private void navToProgress() { Main.switchScene("progress.fxml"); }
    @FXML private void handleLogout() {
        UserSession.getInstance().logout();
        Main.switchScene("login.fxml");
    }
}
