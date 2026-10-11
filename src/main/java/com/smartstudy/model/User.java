package com.smartstudy.model;

/**
 * Abstract base class for all users in the SmartStudy system.
 * Demonstrates: Abstraction and Inheritance.
 * 
 * Student and Teacher extend this class and override getDashboardType().
 */
public abstract class User {
    protected int id;
    protected String name;
    protected String email;
    protected String password;
    protected String role;

    // Default constructor
    public User() {}

    // Parameterized constructor
    public User(int id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Abstract method — each subclass returns its own dashboard type.
     * Demonstrates runtime polymorphism.
     */
    public abstract String getDashboardType();

    /**
     * Abstract method — each user role has a different welcome message.
     */
    public abstract String getWelcomeMessage();

    // --- Getters and Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "', role='" + role + "'}";
    }
}
