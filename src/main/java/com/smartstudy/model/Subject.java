package com.smartstudy.model;

/**
 * Subject domain model class.
 * Represents an academic subject a student is enrolled in.
 */
public class Subject {
    private int id;
    private int studentId;
    private String name;
    private String courseCode;
    private int credits;
    private double targetMarks;

    public Subject() {}

    public Subject(int id, int studentId, String name, String courseCode, int credits, double targetMarks) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.courseCode = courseCode;
        this.credits = credits;
        this.targetMarks = targetMarks;
    }

    // --- Getters and Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public double getTargetMarks() { return targetMarks; }
    public void setTargetMarks(double targetMarks) { this.targetMarks = targetMarks; }

    @Override
    public String toString() {
        return name + " (" + courseCode + ")";
    }
}
