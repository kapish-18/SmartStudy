package com.smartstudy.model;

import java.time.LocalDate;

/**
 * Task domain model class.
 * Represents a study task or academic to-do item.
 */
public class Task implements Comparable<Task> {
    private int id;
    private int studentId;
    private int subjectId;
    private String subjectName; // transient display field
    private String title;
    private String description;
    private int priority; // 1 = highest, 5 = lowest
    private LocalDate deadline;
    private String status; // PENDING, IN_PROGRESS, COMPLETED
    private int estimatedMinutes;

    public Task() {}

    public Task(int id, int studentId, int subjectId, String title, String description,
                int priority, LocalDate deadline, String status, int estimatedMinutes) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
        this.estimatedMinutes = estimatedMinutes;
    }

    /**
     * Natural ordering: by priority ascending (1 is highest priority),
     * then by deadline ascending (earlier deadline first).
     */
    @Override
    public int compareTo(Task other) {
        int cmp = Integer.compare(this.priority, other.priority);
        if (cmp != 0) return cmp;
        if (this.deadline != null && other.deadline != null) {
            return this.deadline.compareTo(other.deadline);
        }
        return 0;
    }

    // --- Getters and Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getEstimatedMinutes() { return estimatedMinutes; }
    public void setEstimatedMinutes(int estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }

    @Override
    public String toString() {
        return title + " [" + status + "] (Priority: " + priority + ")";
    }
}
