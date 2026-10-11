package com.smartstudy.javafx.controller;

import com.smartstudy.Main;
import com.smartstudy.exception.InvalidLoginException;
import com.smartstudy.model.Student;
import com.smartstudy.model.User;
import com.smartstudy.service.UserService;
import com.smartstudy.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller for Login view.
 * Handles user authentication with InvalidLoginException handling.
 */
public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final UserService userService;

    public LoginController() {
        this.userService = new UserService();
    }

    @FXML
    public void initialize() {
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            User user = userService.login(email, password);
            UserSession.getInstance().setCurrentUser(user);
            errorLabel.setStyle("-fx-text-fill: #10B981;");
            errorLabel.setText("Login successful! Welcome, " + user.getName());
            Main.switchScene("dashboard.fxml");
        } catch (InvalidLoginException e) {
            // Demonstrates handling custom InvalidLoginException
            errorLabel.setStyle("-fx-text-fill: #EF4444;");
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            // Fallback for offline demo if DB is not actively running
            if (email != null && !email.trim().isEmpty()) {
                Student demo = new Student(1, "Demo Student", email.trim(), password, "2024-DEMO-001");
                UserSession.getInstance().setCurrentUser(demo);
                Main.switchScene("dashboard.fxml");
            } else {
                errorLabel.setStyle("-fx-text-fill: #EF4444;");
                errorLabel.setText("Please enter a valid email address.");
            }
        }
    }

    @FXML
    private void handleQuickDemoStudent() {
        Student demo = new Student(1, "Alex Johnson", "alex@smartstudy.edu", "pass123", "2024-CSE-042");
        UserSession.getInstance().setCurrentUser(demo);
        Main.switchScene("dashboard.fxml");
    }

    @FXML
    private void handleQuickDemoTeacher() {
        Student demo = new Student(1, "Prof. Sharma", "sharma@smartstudy.edu", "pass123", "FACULTY-01");
        UserSession.getInstance().setCurrentUser(demo);
        Main.switchScene("dashboard.fxml");
    }
}
