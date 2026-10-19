package com.smartstudy.javafx.controller;

import com.smartstudy.Main;
import com.smartstudy.dao.ExamDAO;
import com.smartstudy.dao.impl.ExamDAOImpl;
import com.smartstudy.model.Exam;
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
 * Controller for Exam Management screen.
 * Records upcoming exams and marks obtained.
 * All data from MySQL database — no hardcoded fallback data.
 */
public class ExamController {

    @FXML private TableView<Exam> examTable;
    @FXML private TableColumn<Exam, Integer> colId;
    @FXML private TableColumn<Exam, String> colName;
    @FXML private TableColumn<Exam, String> colSubject;
    @FXML private TableColumn<Exam, LocalDate> colDate;
    @FXML private TableColumn<Exam, Double> colTargetMarks;
    @FXML private TableColumn<Exam, Double> colMarksObtained;

    @FXML private TextField examNameField;
    @FXML private ComboBox<Subject> subjectComboBox;
    @FXML private DatePicker examDatePicker;
    @FXML private TextField targetMarksField;
    @FXML private TextField marksObtainedField;
    @FXML private Label statusLabel;

    private final ExamDAO examDAO;
    private final SubjectService subjectService;
    private final ObservableList<Exam> examList = FXCollections.observableArrayList();

    public ExamController() {
        this.examDAO = new ExamDAOImpl();
        this.subjectService = new SubjectService();
    }

    @FXML
    public void initialize() {
        if (colId != null) colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colName != null) colName.setCellValueFactory(new PropertyValueFactory<>("examName"));
        if (colSubject != null) colSubject.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        if (colDate != null) colDate.setCellValueFactory(new PropertyValueFactory<>("examDate"));
        if (colTargetMarks != null) colTargetMarks.setCellValueFactory(new PropertyValueFactory<>("targetMarks"));
        if (colMarksObtained != null) colMarksObtained.setCellValueFactory(new PropertyValueFactory<>("marksObtained"));

        if (examDatePicker != null) {
            examDatePicker.setValue(LocalDate.now().plusDays(7));
        }

        loadSubjects();
        loadExams();
    }

    private void loadSubjects() {
        if (subjectComboBox == null) return;
        int studentId = UserSession.getInstance().getCurrentUserId();
        try {
            List<Subject> subjects = subjectService.getSubjectsByStudentId(studentId);
            subjectComboBox.setItems(FXCollections.observableArrayList(subjects));
            if (!subjects.isEmpty()) subjectComboBox.setValue(subjects.get(0));
        } catch (Exception e) {
            showStatus("Could not load subjects from database.", true);
        }
    }

    private void loadExams() {
        int studentId = UserSession.getInstance().getCurrentUserId();
        examList.clear();

        try {
            List<Exam> exams = examDAO.findByStudentId(studentId);
            examList.addAll(exams);
            if (exams.isEmpty()) {
                showStatus("No exams scheduled yet. Add your first exam below!", false);
            }
        } catch (Exception e) {
            showStatus("Database error loading exams: " + e.getMessage(), true);
        }

        if (examTable != null) {
            examTable.setItems(examList);
        }
    }

    @FXML
    private void handleAddExam() {
        String name = examNameField.getText();
        Subject subject = subjectComboBox != null ? subjectComboBox.getValue() : null;
        LocalDate date = examDatePicker != null ? examDatePicker.getValue() : LocalDate.now().plusDays(5);
        String targetStr = targetMarksField.getText();

        if (name == null || name.trim().isEmpty() || subject == null) {
            showStatus("Please enter exam name and select a subject.", true);
            return;
        }

        try {
            double targetMarks = targetStr.isEmpty() ? 50.0 : Double.parseDouble(targetStr.trim());
            Exam exam = new Exam(0, subject.getId(), name.trim(), date, targetMarks, -1);
            exam.setSubjectName(subject.getName());

            int id = examDAO.insert(exam);
            if (id > 0) {
                exam.setId(id);
                examList.add(exam);
                if (examNameField != null) examNameField.clear();
                showStatus("Exam added successfully!", false);
            } else {
                showStatus("Failed to save exam to database.", true);
            }
        } catch (NumberFormatException e) {
            showStatus("Target marks must be numeric.", true);
        } catch (Exception e) {
            showStatus("Error: " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleRecordMarks() {
        Exam selected = examTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus("Select an exam from the table to record marks.", true);
            return;
        }

        String marksStr = marksObtainedField.getText();
        if (marksStr == null || marksStr.trim().isEmpty()) {
            showStatus("Enter marks obtained.", true);
            return;
        }

        try {
            double marks = Double.parseDouble(marksStr.trim());
            boolean updated = examDAO.updateMarksObtained(selected.getId(), marks);
            if (updated) {
                selected.setMarksObtained(marks);
                examTable.refresh();
                marksObtainedField.clear();
                showStatus("Marks recorded (" + marks + ") for " + selected.getExamName(), false);
            } else {
                showStatus("Failed to update marks in database.", true);
            }
        } catch (NumberFormatException e) {
            showStatus("Marks must be numeric.", true);
        }
    }

    private void showStatus(String msg, boolean error) {
        if (statusLabel != null) {
            statusLabel.setStyle(error ? "-fx-text-fill: #EF4444;" : "-fx-text-fill: #10B981;");
            statusLabel.setText(msg);
        }
    }

    @FXML private void navToDashboard() { Main.switchScene("dashboard.fxml"); }
    @FXML private void navToSubjects() { Main.switchScene("subjects.fxml"); }
    @FXML private void navToTasks() { Main.switchScene("tasks.fxml"); }
    @FXML private void navToStudy() { Main.switchScene("study.fxml"); }
    @FXML private void navToProgress() { Main.switchScene("progress.fxml"); }
}
