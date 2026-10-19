package com.smartstudy.javafx.controller;

import com.smartstudy.Main;
import com.smartstudy.model.Subject;
import com.smartstudy.service.SubjectService;
import com.smartstudy.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Controller for Subject Management screen.
 * Demonstrates Collections (HashMap lookup via SubjectService) and CRUD operations.
 * All data is fetched from and persisted to the MySQL database.
 */
public class SubjectController {

    @FXML private TableView<Subject> subjectTable;
    @FXML private TableColumn<Subject, Integer> colId;
    @FXML private TableColumn<Subject, String> colName;
    @FXML private TableColumn<Subject, String> colCode;
    @FXML private TableColumn<Subject, Integer> colCredits;
    @FXML private TableColumn<Subject, Double> colTargetMarks;

    @FXML private TextField nameField;
    @FXML private TextField codeField;
    @FXML private TextField creditsField;
    @FXML private TextField targetMarksField;
    @FXML private Label statusLabel;

    private final SubjectService subjectService;
    private final ObservableList<Subject> subjectList = FXCollections.observableArrayList();

    public SubjectController() {
        this.subjectService = new SubjectService();
    }

    @FXML
    public void initialize() {
        if (colId != null) colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colName != null) colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (colCode != null) colCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        if (colCredits != null) colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));
        if (colTargetMarks != null) colTargetMarks.setCellValueFactory(new PropertyValueFactory<>("targetMarks"));

        loadSubjects();
    }

    private void loadSubjects() {
        int studentId = UserSession.getInstance().getCurrentUserId();
        subjectList.clear();

        try {
            List<Subject> subjects = subjectService.getSubjectsByStudentId(studentId);
            subjectList.addAll(subjects);
            if (subjects.isEmpty()) {
                showStatus("No subjects found. Add your first subject below!", false);
            }
        } catch (Exception e) {
            showStatus("Database error: " + e.getMessage(), true);
        }

        if (subjectTable != null) {
            subjectTable.setItems(subjectList);
        }
    }

    @FXML
    private void handleAddSubject() {
        String name = nameField.getText();
        String code = codeField.getText();
        String creditsStr = creditsField.getText();
        String targetStr = targetMarksField.getText();

        if (name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty()) {
            showStatus("Please enter subject name and code.", true);
            return;
        }

        try {
            int credits = creditsStr.isEmpty() ? 3 : Integer.parseInt(creditsStr.trim());
            double targetMarks = targetStr.isEmpty() ? 80.0 : Double.parseDouble(targetStr.trim());

            int studentId = UserSession.getInstance().getCurrentUserId();
            Subject newSubject = new Subject(0, studentId, name.trim(), code.trim(), credits, targetMarks);

            int generatedId = subjectService.addSubject(newSubject);
            if (generatedId > 0) {
                newSubject.setId(generatedId);
                subjectList.add(newSubject);
                clearFields();
                showStatus("Subject '" + name + "' added successfully!", false);
            } else {
                showStatus("Failed to add subject. Check database connection.", true);
            }
        } catch (NumberFormatException e) {
            showStatus("Credits must be integer and Target Marks numeric.", true);
        } catch (Exception e) {
            showStatus("Error adding subject: " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleDeleteSubject() {
        Subject selected = subjectTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus("Select a subject from the table to delete.", true);
            return;
        }

        boolean deleted = subjectService.deleteSubject(selected.getId());
        if (deleted) {
            subjectList.remove(selected);
            showStatus("Subject '" + selected.getName() + "' deleted.", false);
        } else {
            showStatus("Failed to delete subject from database.", true);
        }
    }

    private void clearFields() {
        if (nameField != null) nameField.clear();
        if (codeField != null) codeField.clear();
        if (creditsField != null) creditsField.clear();
        if (targetMarksField != null) targetMarksField.clear();
    }

    private void showStatus(String msg, boolean error) {
        if (statusLabel != null) {
            statusLabel.setStyle(error ? "-fx-text-fill: #EF4444;" : "-fx-text-fill: #10B981;");
            statusLabel.setText(msg);
        }
    }

    @FXML private void navToDashboard() { Main.switchScene("dashboard.fxml"); }
    @FXML private void navToTasks() { Main.switchScene("tasks.fxml"); }
    @FXML private void navToExams() { Main.switchScene("exams.fxml"); }
    @FXML private void navToStudy() { Main.switchScene("study.fxml"); }
    @FXML private void navToProgress() { Main.switchScene("progress.fxml"); }
}
