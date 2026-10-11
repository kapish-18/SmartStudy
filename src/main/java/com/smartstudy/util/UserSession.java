package com.smartstudy.util;

import com.smartstudy.model.Student;
import com.smartstudy.model.User;

/**
 * Singleton holder for current user session state in the JavaFX client.
 * Keeps track of who is logged in and provides default fallback student
 * for quick testing/prototype evaluation.
 */
public class UserSession {

    private static UserSession instance;
    private User currentUser;

    private UserSession() {
        // Initialize with default sample student for convenient testing
        Student defaultStudent = new Student(1, "Alex Johnson", "alex@smartstudy.edu", "pass123", "2024-CSE-042");
        this.currentUser = defaultStudent;
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public int getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : 1;
    }

    public String getCurrentUserName() {
        return currentUser != null ? currentUser.getName() : "Student";
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        Student defaultStudent = new Student(1, "Alex Johnson", "alex@smartstudy.edu", "pass123", "2024-CSE-042");
        this.currentUser = defaultStudent;
    }
}
