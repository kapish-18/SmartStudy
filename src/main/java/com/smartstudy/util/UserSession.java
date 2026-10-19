package com.smartstudy.util;

import com.smartstudy.model.User;

/**
 * Singleton holder for current user session state in the JavaFX client.
 * Keeps track of who is logged in.
 */
public class UserSession {

    private static UserSession instance;
    private User currentUser;

    private UserSession() {
        // No default user — requires proper authentication
        this.currentUser = null;
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
        return currentUser != null ? currentUser.getId() : -1;
    }

    public String getCurrentUserName() {
        return currentUser != null ? currentUser.getName() : "Guest";
    }

    public String getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : "STUDENT";
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        this.currentUser = null;
    }
}
