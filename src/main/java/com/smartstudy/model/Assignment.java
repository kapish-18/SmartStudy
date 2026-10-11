package com.smartstudy.model;

import java.time.LocalDate;

/**
 * Assignment domain model class.
 * Represents a graded assignment for a subject.
 */
public class Assignment {
    private int id;
    private int subjectId;
    private String subjectName; // transient display field
    private String title;
    private LocalDate deadline;
    private String status; // PENDING, IN_PROGRESS, COMPLETED
    private double marks; // -1 means not yet graded

    public Assignment() {}

    public Assignment(int id, int subjectId, String title, LocalDate deadline,
                      String status, double marks) {
        this.id = id;
        this.subjectId = subjectId;
        this.title = title;
        this.deadline = deadline;
        this.status = status;
        this.marks = marks;
    }

    public boolean isOverdue() {
        return deadline != null && deadline.isBefore(LocalDate.now()) && !"COMPLETED".equals(status);
    }

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }

    @Override
    public String toString() {
        return title + " [" + status + "]";
    }
}
