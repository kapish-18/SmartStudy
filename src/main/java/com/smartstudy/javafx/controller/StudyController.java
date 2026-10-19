package com.smartstudy.javafx.controller;

import com.smartstudy.Main;
import com.smartstudy.dao.StudySessionDAO;
import com.smartstudy.dao.impl.StudySessionDAOImpl;
import com.smartstudy.model.StudySession;
import com.smartstudy.model.Subject;
import com.smartstudy.service.SubjectService;
import com.smartstudy.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for Study Session Tracker.
 * Records study duration, topics, and calculates study time.
 * Real MySQL database integration with no hardcoded fallback data.
 */
public class StudyController {

    @FXML private TableView<StudySession> sessionTable;
    @FXML private TableColumn<StudySession, Integer> colId;
    @FXML private TableColumn<StudySession, String> colSubject;
    @FXML private TableColumn<StudySession, String> colTopic;
    @FXML private TableColumn<StudySession, Integer> colDuration;
    @FXML private TableColumn<StudySession, LocalDate> colDate;

    @FXML private ComboBox<Subject> subjectComboBox;
    @FXML private TextField topicField;
    @FXML private TextField durationField;
    @FXML private DatePicker sessionDatePicker;
    @FXML private Label totalHoursLabel;
    @FXML private Label statusLabel;

    private final StudySessionDAO sessionDAO;
    private final SubjectService subjectService;
    private final ObservableList<StudySession> sessionList = FXCollections.observableArrayList();

    public StudyController() {
        this.sessionDAO = new StudySessionDAOImpl();
        this.subjectService = new SubjectService();
    }

    @FXML
    public void initialize() {
        if (!UserSession.getInstance().isLoggedIn()) {
            Main.switchScene("login.fxml");
            return;
        }

        if (colId != null) colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colSubject != null) colSubject.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        if (colTopic != null) colTopic.setCellValueFactory(new PropertyValueFactory<>("topic"));
        if (colDuration != null) colDuration.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
        if (colDate != null) colDate.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));

        if (sessionDatePicker != null) {
            sessionDatePicker.setValue(LocalDate.now());
        }

        loadSubjects();
        loadSessions();
    }

    private void loadSubjects() {
        if (subjectComboBox == null) return;
        int studentId = UserSession.getInstance().getCurrentUserId();
        try {
            List<Subject> subjects = subjectService.getSubjectsByStudentId(studentId);
            subjectComboBox.setItems(FXCollections.observableArrayList(subjects));
            if (!subjects.isEmpty()) {
                subjectComboBox.setValue(subjects.get(0));
            }
        } catch (Exception e) {
            showStatus("Database error loading subjects: " + e.getMessage(), true);
        }
    }

    private void loadSessions() {
        int studentId = UserSession.getInstance().getCurrentUserId();
        sessionList.clear();

        try {
            List<StudySession> sessions = sessionDAO.findByStudentId(studentId);
            sessionList.addAll(sessions);
            if (sessions.isEmpty()) {
                showStatus("No study sessions recorded yet. Log your first session below!", false);
            }
        } catch (Exception e) {
            showStatus("Database error loading study sessions: " + e.getMessage(), true);
        }

        if (sessionTable != null) {
            sessionTable.setItems(sessionList);
        }

        updateTotalHours();
    }

    private void updateTotalHours() {
        int totalMinutes = 0;
        for (StudySession s : sessionList) {
            totalMinutes += s.getDurationMinutes();
        }
        double hours = totalMinutes / 60.0;
        if (totalHoursLabel != null) {
            totalHoursLabel.setText(String.format("Total Recorded Study Time: %.1f hours (%d mins)", hours, totalMinutes));
        }
    }

    @FXML
    private void handleRecordSession() {
        Subject subject = subjectComboBox != null ? subjectComboBox.getValue() : null;
        String topic = topicField != null ? topicField.getText() : null;
        String durationStr = durationField != null ? durationField.getText() : null;
        LocalDate date = sessionDatePicker != null ? sessionDatePicker.getValue() : LocalDate.now();

        if (subject == null || topic == null || topic.trim().isEmpty() || durationStr == null || durationStr.trim().isEmpty()) {
            showStatus("Please select a subject and enter topic and duration.", true);
            return;
        }

        try {
            int duration = Integer.parseInt(durationStr.trim());
            if (duration <= 0) {
                showStatus("Duration must be a positive number of minutes.", true);
                return;
            }

            int studentId = UserSession.getInstance().getCurrentUserId();
            StudySession session = new StudySession(0, studentId, subject.getId(), topic.trim(), duration, date);
            session.setSubjectName(subject.getName());

            int id = sessionDAO.insert(session);
            if (id > 0) {
                session.setId(id);
                sessionList.add(session);
                if (topicField != null) topicField.clear();
                if (durationField != null) durationField.clear();
                updateTotalHours();
                showStatus("Study session recorded successfully! (" + duration + " mins)", false);
            } else {
                showStatus("Failed to save study session to database.", true);
            }
        } catch (NumberFormatException e) {
            showStatus("Duration must be a valid positive integer in minutes.", true);
        } catch (Exception e) {
            showStatus("Error saving session: " + e.getMessage(), true);
        }
    }

    @FXML private void setPomodoro() { if (durationField != null) durationField.setText("25"); }
    @FXML private void setHour() { if (durationField != null) durationField.setText("60"); }
    @FXML private void setHourAndHalf() { if (durationField != null) durationField.setText("90"); }

    private void showStatus(String msg, boolean error) {
        if (statusLabel != null) {
            statusLabel.setStyle(error ? "-fx-text-fill: #EF4444;" : "-fx-text-fill: #10B981;");
            statusLabel.setText(msg);
        }
    }

    @FXML private void navToDashboard() { Main.switchScene("dashboard.fxml"); }
    @FXML private void navToSubjects() { Main.switchScene("subjects.fxml"); }
    @FXML private void navToTasks() { Main.switchScene("tasks.fxml"); }
    @FXML private void navToExams() { Main.switchScene("exams.fxml"); }
    @FXML private void navToProgress() { Main.switchScene("progress.fxml"); }
}
