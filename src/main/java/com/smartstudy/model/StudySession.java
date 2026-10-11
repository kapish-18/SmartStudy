package com.smartstudy.model;

import java.time.LocalDate;

/**
 * StudySession domain model class.
 * Represents a recorded study session for a subject/topic.
 */
public class StudySession {
    private int id;
    private int studentId;
    private int subjectId;
    private String subjectName; // transient display field
    private String topic;
    private int durationMinutes;
    private LocalDate sessionDate;

    public StudySession() {}

    public StudySession(int id, int studentId, int subjectId, String topic,
                        int durationMinutes, LocalDate sessionDate) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.topic = topic;
        this.durationMinutes = durationMinutes;
        this.sessionDate = sessionDate;
    }

    /**
     * Returns duration formatted as "Xh Ym".
     */
    public String getFormattedDuration() {
        int hours = durationMinutes / 60;
        int mins = durationMinutes % 60;
        if (hours > 0) {
            return hours + "h " + mins + "m";
        }
        return mins + "m";
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

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }

    @Override
    public String toString() {
        return topic + " (" + getFormattedDuration() + ") on " + sessionDate;
    }
}
