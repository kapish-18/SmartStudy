package com.smartstudy.dao;

import com.smartstudy.model.User;

import java.util.List;

/**
 * DAO interface for User operations.
 * Defines database operations without exposing SQL to controllers.
 */
public interface UserDAO {

    /**
     * Authenticates a user by email and password.
     * @return the User object if found, null otherwise
     */
    User authenticate(String email, String password);

    /**
     * Finds a user by their ID.
     */
    User findById(int id);

    /**
     * Finds a user by their email.
     */
    User findByEmail(String email);

    /**
     * Finds all users with a specific role (e.g. "STUDENT" or "TEACHER").
     */
    List<User> findByRole(String role);

    /**
     * Inserts a new user into the database.
     * @return the generated user ID
     */
    int insert(User user);

    /**
     * Updates an existing user.
     */
    boolean update(User user);
}
