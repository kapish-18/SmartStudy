package com.smartstudy.javafx.controller;

import com.smartstudy.Main;
import com.smartstudy.exception.InvalidLoginException;
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
 * All authentication goes through UserService → UserDAO → MySQL database.
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
            errorLabel.setStyle("-fx-text-fill: #EF4444;");
            errorLabel.setText("Database connection error. Ensure MySQL is running.");
        }
    }

    /**
     * Quick demo login using real DB credentials.
     * Authenticates alex@smartstudy.edu / pass123 from the database.
     */
    @FXML
    private void handleQuickDemoStudent() {
        try {
            User user = userService.login("alex@smartstudy.edu", "pass123");
            UserSession.getInstance().setCurrentUser(user);
            Main.switchScene("dashboard.fxml");
        } catch (Exception e) {
            if (errorLabel != null) {
                errorLabel.setStyle("-fx-text-fill: #EF4444;");
                errorLabel.setText("Demo login failed. Ensure MySQL is running and schema.sql has been executed.");
            }
        }
    }

    /**
     * Quick demo login for teacher role using real DB credentials.
     * Authenticates sharma@smartstudy.edu / pass123 from the database.
     */
    @FXML
    private void handleQuickDemoTeacher() {
        try {
            User user = userService.login("sharma@smartstudy.edu", "pass123");
            UserSession.getInstance().setCurrentUser(user);
            Main.switchScene("dashboard.fxml");
        } catch (Exception e) {
            if (errorLabel != null) {
                errorLabel.setStyle("-fx-text-fill: #EF4444;");
                errorLabel.setText("Demo login failed. Ensure MySQL is running and schema.sql has been executed.");
            }
        }
    }
}
