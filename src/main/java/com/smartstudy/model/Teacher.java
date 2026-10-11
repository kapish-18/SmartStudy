package com.smartstudy.model;

/**
 * Teacher class — extends User.
 * Demonstrates: Inheritance and Method Overriding.
 */
public class Teacher extends User {

    public Teacher() {
        this.role = "TEACHER";
    }

    public Teacher(int id, String name, String email, String password) {
        super(id, name, email, password, "TEACHER");
    }

    @Override
    public String getDashboardType() {
        return "Teacher Dashboard";
    }

    @Override
    public String getWelcomeMessage() {
        return "Welcome, " + name + ". View your students' progress below.";
    }

    @Override
    public String toString() {
        return "Teacher{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}
