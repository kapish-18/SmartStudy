package com.smartstudy.service;

import com.smartstudy.dao.ExamDAO;
import com.smartstudy.dao.StudySessionDAO;
import com.smartstudy.dao.TaskDAO;
import com.smartstudy.dao.impl.ExamDAOImpl;
import com.smartstudy.dao.impl.StudySessionDAOImpl;
import com.smartstudy.dao.impl.TaskDAOImpl;
import com.smartstudy.model.Exam;
import com.smartstudy.model.Subject;
import com.smartstudy.model.Task;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Calculates academic progress for subjects and overall.
 * Demonstrates: HashSet for unique completed topics.
 */
public class ProgressService {

    private final TaskDAO taskDAO;
    private final ExamDAO examDAO;
    private final StudySessionDAO sessionDAO;

    public ProgressService() {
        this.taskDAO = new TaskDAOImpl();
        this.examDAO = new ExamDAOImpl();
        this.sessionDAO = new StudySessionDAOImpl();
    }

    /**
     * Calculate completion percentage for a subject based on completed tasks.
     */
    public double getSubjectProgress(int subjectId) {
        List<Task> tasks = taskDAO.findBySubjectId(subjectId);
        if (tasks.isEmpty()) return 0.0;

        long completed = tasks.stream().filter(t -> "COMPLETED".equals(t.getStatus())).count();
        return (completed * 100.0) / tasks.size();
    }

    /**
     * Calculate overall progress for a student across all subjects.
     */
    public double getOverallProgress(int studentId) {
        List<Task> tasks = taskDAO.findByStudentId(studentId);
        if (tasks.isEmpty()) return 0.0;

        long completed = tasks.stream().filter(t -> "COMPLETED".equals(t.getStatus())).count();
        return (completed * 100.0) / tasks.size();
    }

    /**
     * Get unique completed topics for a student.
     * Demonstrates: HashSet collection usage.
     */
    public Set<String> getCompletedTopics(int studentId) {
        Set<String> topics = new HashSet<>();
        List<Task> tasks = taskDAO.findByStudentId(studentId);
        for (Task t : tasks) {
            if ("COMPLETED".equals(t.getStatus()) && t.getTitle() != null) {
                topics.add(t.getTitle());
            }
        }
        return topics;
    }

    /**
     * Get total study hours for a student.
     */
    public double getTotalStudyHours(int studentId) {
        int totalMinutes = sessionDAO.getTotalMinutesByStudentId(studentId);
        return totalMinutes / 60.0;
    }

    /**
     * Get study hours for a specific subject.
     */
    public double getSubjectStudyHours(int subjectId) {
        int totalMinutes = sessionDAO.getTotalMinutesBySubjectId(subjectId);
        return totalMinutes / 60.0;
    }

    /**
     * Get average exam marks for a student (only graded exams).
     */
    public double getAverageExamMarks(int studentId) {
        List<Exam> exams = examDAO.findAllByStudentId(studentId);
        double total = 0;
        int count = 0;
        for (Exam e : exams) {
            if (e.getMarksObtained() >= 0) {
                total += (e.getMarksObtained() / e.getTargetMarks()) * 100;
                count++;
            }
        }
        return count > 0 ? total / count : 0.0;
    }

    public double calculateOverallProgress(int studentId) {
        return getOverallProgress(studentId);
    }

    public double calculateTotalStudyHours(int studentId) {
        return getTotalStudyHours(studentId);
    }

    public double calculateSubjectProgress(int subjectId) {
        return getSubjectProgress(subjectId);
    }

    public List<Exam> getUpcomingExams(int studentId) {
        return examDAO.findUpcomingByStudentId(studentId);
    }
}
