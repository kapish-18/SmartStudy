package com.smartstudy.javafx.controller;

import com.smartstudy.Main;
import com.smartstudy.exception.InvalidTaskException;
import com.smartstudy.model.Subject;
import com.smartstudy.model.Task;
import com.smartstudy.service.SubjectService;
import com.smartstudy.service.TaskService;
import com.smartstudy.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for Task Management.
 * All data from MySQL database. Demonstrates custom InvalidTaskException handling.
 */
public class TaskController {

    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, Integer> colId;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, Integer> colPriority;
    @FXML private TableColumn<Task, LocalDate> colDeadline;
    @FXML private TableColumn<Task, String> colStatus;
    @FXML private TableColumn<Task, Integer> colEstMinutes;

    @FXML private TextField titleField;
    @FXML private TextField descField;
    @FXML private ComboBox<Subject> subjectComboBox;
    @FXML private ComboBox<Integer> priorityComboBox;
    @FXML private DatePicker deadlinePicker;
    @FXML private TextField estimatedMinutesField;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private Label statusLabel;

    private final TaskService taskService;
    private final SubjectService subjectService;
    private final ObservableList<Task> taskList = FXCollections.observableArrayList();

    public TaskController() {
        this.taskService = new TaskService();
        this.subjectService = new SubjectService();
    }

    @FXML
    public void initialize() {
        if (colId != null) colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colTitle != null) colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        if (colPriority != null) colPriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
        if (colDeadline != null) colDeadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        if (colStatus != null) colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        if (colEstMinutes != null) colEstMinutes.setCellValueFactory(new PropertyValueFactory<>("estimatedMinutes"));

        if (priorityComboBox != null) {
            priorityComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
            priorityComboBox.setValue(3);
        }

        if (filterComboBox != null) {
            filterComboBox.setItems(FXCollections.observableArrayList("All Tasks", "Pending Only", "Completed"));
            filterComboBox.setValue("All Tasks");
            filterComboBox.setOnAction(e -> applyFilter());
        }

        if (deadlinePicker != null) {
            deadlinePicker.setValue(LocalDate.now().plusDays(3));
        }

        loadSubjects();
        loadTasks();
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
            showStatus("Could not load subjects from database.", true);
        }
    }

    private void loadTasks() {
        int studentId = UserSession.getInstance().getCurrentUserId();
        taskList.clear();

        try {
            List<Task> tasks = taskService.getTasksByStudentId(studentId);
            taskList.addAll(tasks);
            if (tasks.isEmpty()) {
                showStatus("No tasks yet. Add your first task below!", false);
            }
        } catch (Exception e) {
            showStatus("Database error loading tasks: " + e.getMessage(), true);
        }

        if (taskTable != null) {
            taskTable.setItems(taskList);
        }
    }

    @FXML
    private void handleAddTask() {
        String title = titleField.getText();
        String desc = descField.getText();
        LocalDate deadline = deadlinePicker != null ? deadlinePicker.getValue() : null;
        Integer priority = priorityComboBox != null ? priorityComboBox.getValue() : 3;
        Subject selectedSubject = subjectComboBox != null ? subjectComboBox.getValue() : null;
        int subjectId = selectedSubject != null ? selectedSubject.getId() : 0;
        int studentId = UserSession.getInstance().getCurrentUserId();

        int estMinutes = 60;
        if (estimatedMinutesField != null && !estimatedMinutesField.getText().trim().isEmpty()) {
            try {
                estMinutes = Integer.parseInt(estimatedMinutesField.getText().trim());
            } catch (NumberFormatException e) {
                showStatus("Estimated minutes must be a valid number.", true);
                return;
            }
        }

        Task newTask = new Task(0, studentId, subjectId, title, desc, 
                                priority != null ? priority : 3, deadline, "PENDING", estMinutes);

        try {
            // This triggers custom InvalidTaskException if title is empty or deadline is in past
            int generatedId = taskService.addTask(newTask);
            if (generatedId > 0) {
                newTask.setId(generatedId);
                taskList.add(newTask);
                clearFields();
                showStatus("Task added successfully!", false);
            } else {
                showStatus("Failed to save task to database.", true);
            }
        } catch (InvalidTaskException e) {
            // Demonstrates handling user-defined exception!
            showStatus("[InvalidTaskException] " + e.getMessage(), true);
        } catch (Exception e) {
            showStatus("Error: " + e.getMessage(), true);
        }
    }

    /**
     * Intentionally tests custom InvalidTaskException with an invalid past deadline.
     */
    @FXML
    private void handleTestInvalidDeadlineException() {
        int studentId = UserSession.getInstance().getCurrentUserId();
        Task invalidTask = new Task(0, studentId, 1, "Past Due Task Demo", "Testing validation", 4, 
                                    LocalDate.now().minusDays(5), "PENDING", 45);
        try {
            taskService.addTask(invalidTask);
        } catch (InvalidTaskException e) {
            showStatus("Verified Custom Exception: [InvalidTaskException] " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleMarkComplete() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus("Select a task from the table to complete.", true);
            return;
        }

        boolean updated = taskService.markComplete(selected.getId());
        if (updated) {
            selected.setStatus("COMPLETED");
            taskTable.refresh();
            showStatus("Task '" + selected.getTitle() + "' marked as COMPLETED!", false);
        } else {
            showStatus("Failed to update task status in database.", true);
        }
    }

    @FXML
    private void handleDeleteTask() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus("Select a task from the table to delete.", true);
            return;
        }

        boolean deleted = taskService.deleteTask(selected.getId());
        if (deleted) {
            taskList.remove(selected);
            showStatus("Task deleted.", false);
        } else {
            showStatus("Failed to delete task from database.", true);
        }
    }

    private void applyFilter() {
        if (filterComboBox == null) return;
        String filter = filterComboBox.getValue();

        if ("Pending Only".equals(filter)) {
            taskTable.setItems(taskList.filtered(t -> !"COMPLETED".equalsIgnoreCase(t.getStatus())));
        } else if ("Completed".equals(filter)) {
            taskTable.setItems(taskList.filtered(t -> "COMPLETED".equalsIgnoreCase(t.getStatus())));
        } else {
            taskTable.setItems(taskList);
        }
    }

    private void clearFields() {
        if (titleField != null) titleField.clear();
        if (descField != null) descField.clear();
        if (estimatedMinutesField != null) estimatedMinutesField.clear();
    }

    private void showStatus(String msg, boolean error) {
        if (statusLabel != null) {
            statusLabel.setStyle(error ? "-fx-text-fill: #EF4444;" : "-fx-text-fill: #10B981;");
            statusLabel.setText(msg);
        }
    }

    @FXML private void navToDashboard() { Main.switchScene("dashboard.fxml"); }
    @FXML private void navToSubjects() { Main.switchScene("subjects.fxml"); }
    @FXML private void navToExams() { Main.switchScene("exams.fxml"); }
    @FXML private void navToStudy() { Main.switchScene("study.fxml"); }
    @FXML private void navToProgress() { Main.switchScene("progress.fxml"); }
}
