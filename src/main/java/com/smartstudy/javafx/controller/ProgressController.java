package com.smartstudy.javafx.controller;

import com.smartstudy.Main;
import com.smartstudy.model.Subject;
import com.smartstudy.service.ProgressService;
import com.smartstudy.service.SubjectService;
import com.smartstudy.util.UserSession;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Controller for Progress Analytics view.
 * Demonstrates: HashSet collection for unique completed topics, progress calculations.
 * All metrics derived directly from MySQL database without hardcoded fallbacks.
 */
public class ProgressController {

    @FXML private Label overallProgressValueLabel;
    @FXML private ProgressBar overallProgressBar;
    @FXML private Label totalHoursValueLabel;
    @FXML private Label uniqueTopicsCountLabel;
    @FXML private VBox subjectProgressContainer;

    private final ProgressService progressService;
    private final SubjectService subjectService;

    public ProgressController() {
        this.progressService = new ProgressService();
        this.subjectService = new SubjectService();
    }

    @FXML
    public void initialize() {
        if (!UserSession.getInstance().isLoggedIn()) {
            Main.switchScene("login.fxml");
            return;
        }
        loadProgressData();
    }

    private void loadProgressData() {
        int studentId = UserSession.getInstance().getCurrentUserId();

        double overall = 0.0;
        double studyHours = 0.0;
        try {
            overall = progressService.calculateOverallProgress(studentId);
            studyHours = progressService.calculateTotalStudyHours(studentId);
        } catch (Exception e) {
            // Keep at 0.0 if unable to calculate
        }

        if (overallProgressValueLabel != null) {
            overallProgressValueLabel.setText(String.format("%.1f%%", overall));
        }
        if (overallProgressBar != null) {
            overallProgressBar.setProgress(Math.min(1.0, overall / 100.0));
        }
        if (totalHoursValueLabel != null) {
            totalHoursValueLabel.setText(String.format("%.1f Hours", studyHours));
        }

        // Demonstrate HashSet usage for unique topics studied
        Set<String> uniqueTopics = progressService.getCompletedTopics(studentId);
        if (uniqueTopicsCountLabel != null) {
            uniqueTopicsCountLabel.setText(uniqueTopics.size() + " Unique Topics Studied");
        }

        loadSubjectProgressCards(studentId);
    }

    private void loadSubjectProgressCards(int studentId) {
        if (subjectProgressContainer == null) return;
        subjectProgressContainer.getChildren().clear();

        List<Subject> subjects = new ArrayList<>();
        try {
            subjects = subjectService.getSubjectsByStudentId(studentId);
        } catch (Exception e) {
            // Error loading subjects
        }

        if (subjects.isEmpty()) {
            Label emptyLbl = new Label("No subjects enrolled yet. Add subjects in the Subjects tab to see progress here.");
            emptyLbl.getStyleClass().add("text-muted");
            emptyLbl.setPadding(new Insets(16));
            subjectProgressContainer.getChildren().add(emptyLbl);
            return;
        }

        for (Subject sub : subjects) {
            double prog = 0.0;
            double hours = 0.0;
            try {
                prog = progressService.calculateSubjectProgress(sub.getId());
                hours = progressService.getSubjectStudyHours(sub.getId());
            } catch (Exception ignored) {}

            VBox card = new VBox(8);
            card.getStyleClass().add("card");
            card.setPadding(new Insets(12, 16, 12, 16));

            HBox header = new HBox(12);
            Label nameLbl = new Label(sub.getName() + " (" + sub.getCourseCode() + ")");
            nameLbl.getStyleClass().add("card-title");
            HBox.setHgrow(nameLbl, Priority.ALWAYS);

            Label progVal = new Label(String.format("%.1f%% (Target: %.0f%%)", prog, sub.getTargetMarks()));
            progVal.getStyleClass().add("stat-value-sm");
            header.getChildren().addAll(nameLbl, progVal);

            ProgressBar pb = new ProgressBar(Math.min(1.0, prog / 100.0));
            pb.setMaxWidth(Double.MAX_VALUE);
            pb.setPrefHeight(12);

            Label meta = new Label(String.format("Credits: %d  |  Study Hours Logged: %.1f hrs", sub.getCredits(), hours));
            meta.getStyleClass().add("text-muted");

            card.getChildren().addAll(header, pb, meta);
            subjectProgressContainer.getChildren().add(card);
        }
    }

    @FXML private void navToDashboard() { Main.switchScene("dashboard.fxml"); }
    @FXML private void navToSubjects() { Main.switchScene("subjects.fxml"); }
    @FXML private void navToTasks() { Main.switchScene("tasks.fxml"); }
    @FXML private void navToExams() { Main.switchScene("exams.fxml"); }
    @FXML private void navToStudy() { Main.switchScene("study.fxml"); }
}
