package com.smartstudy.model;

import java.time.LocalDate;

/**
 * Exam domain model class.
 * Represents an upcoming or completed exam for a subject.
 */
public class Exam {
    private int id;
    private int subjectId;
    private String subjectName; // transient display field
    private String examName;
    private LocalDate examDate;
    private double targetMarks;
    private double marksObtained; // -1 means not yet taken

    public Exam() {}

    public Exam(int id, int subjectId, String examName, LocalDate examDate,
                double targetMarks, double marksObtained) {
        this.id = id;
        this.subjectId = subjectId;
        this.examName = examName;
        this.examDate = examDate;
        this.targetMarks = targetMarks;
        this.marksObtained = marksObtained;
    }

    /**
     * Check if the exam is upcoming (not yet taken).
     */
    public boolean isUpcoming() {
        return marksObtained < 0 && examDate != null && !examDate.isBefore(LocalDate.now());
    }

    /**
     * Check if exam is within the given number of days from today.
     */
    public boolean isWithinDays(int days) {
        if (examDate == null) return false;
        LocalDate cutoff = LocalDate.now().plusDays(days);
        return !examDate.isBefore(LocalDate.now()) && !examDate.isAfter(cutoff);
    }

    // --- Getters and Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSubjectId() { return subjectId; }
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getExamName() { return examName; }
    public void setExamName(String examName) { this.examName = examName; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public double getTargetMarks() { return targetMarks; }
    public void setTargetMarks(double targetMarks) { this.targetMarks = targetMarks; }

    public double getMarksObtained() { return marksObtained; }
    public void setMarksObtained(double marksObtained) { this.marksObtained = marksObtained; }

    @Override
    public String toString() {
        return examName + " (" + examDate + ")";
    }
}
