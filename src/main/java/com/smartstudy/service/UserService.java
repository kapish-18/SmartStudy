package com.smartstudy.service;

import com.smartstudy.dao.UserDAO;
import com.smartstudy.dao.impl.UserDAOImpl;
import com.smartstudy.exception.InvalidLoginException;
import com.smartstudy.model.User;

/**
 * Business logic for user authentication and management.
 */
public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Authenticates a user. Throws InvalidLoginException on failure.
     */
    public User login(String email, String password) throws InvalidLoginException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidLoginException("Email cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidLoginException("Password cannot be empty.");
        }

        User user = userDAO.authenticate(email.trim(), password);
        if (user == null) {
            throw new InvalidLoginException("Invalid email or password.");
        }
        return user;
    }

    public User findById(int id) {
        return userDAO.findById(id);
    }

    public java.util.List<User> getUsersByRole(String role) {
        return userDAO.findByRole(role);
    }

    public int register(User user) {
        return userDAO.insert(user);
    }
}
