package com.smartstudy.model;

/**
 * Student class — extends User.
 * Demonstrates: Inheritance and Method Overriding.
 */
public class Student extends User {

    private String rollNumber;

    public Student() {
        this.role = "STUDENT";
    }

    public Student(int id, String name, String email, String password) {
        super(id, name, email, password, "STUDENT");
    }

    public Student(int id, String name, String email, String password, String rollNumber) {
        super(id, name, email, password, "STUDENT");
        this.rollNumber = rollNumber;
    }

    public String getRollNumber() {
        return rollNumber != null ? rollNumber : "2024-CSE-001";
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    @Override
    public String getDashboardType() {
        return "Student Dashboard";
    }

    @Override
    public String getWelcomeMessage() {
        return "Welcome back, " + name + "! Ready to study?";
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}
