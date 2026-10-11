package com.smartstudy.service;

import com.smartstudy.collections.TaskPriorityQueue;
import com.smartstudy.dao.ExamDAO;
import com.smartstudy.dao.TaskDAO;
import com.smartstudy.dao.impl.ExamDAOImpl;
import com.smartstudy.dao.impl.TaskDAOImpl;
import com.smartstudy.model.Exam;
import com.smartstudy.model.Task;
import com.smartstudy.strategy.DeadlinePriorityStrategy;
import com.smartstudy.strategy.PriorityStrategy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Rule-based recommendation engine.
 * Analyzes tasks, exams, and progress to generate study recommendations.
 * 
 * Demonstrates: Strategy pattern usage, PriorityQueue, rule-based logic.
 */
public class RecommendationService {

    private final TaskDAO taskDAO;
    private final ExamDAO examDAO;
    private final ProgressService progressService;
    private PriorityStrategy priorityStrategy;

    public RecommendationService() {
        this.taskDAO = new TaskDAOImpl();
        this.examDAO = new ExamDAOImpl();
        this.progressService = new ProgressService();
        this.priorityStrategy = new DeadlinePriorityStrategy(); // default strategy
    }

    /** Switch the priority strategy (demonstrates polymorphism). */
    public void setPriorityStrategy(PriorityStrategy strategy) {
        this.priorityStrategy = strategy;
    }

    /**
     * Generate a list of recommendation strings for the student.
     * Implements the rule-based logic from the project specification.
     */
    public List<String> getRecommendations(int studentId) {
        List<String> recommendations = new ArrayList<>();

        // Rule 1: Exam within 3 days + progress below 60%
        List<Exam> upcomingExams = examDAO.findUpcomingByStudentId(studentId);
        for (Exam exam : upcomingExams) {
            if (exam.isWithinDays(3)) {
                double progress = progressService.getSubjectProgress(exam.getSubjectId());
                if (progress < 60) {
                    recommendations.add("⚠️ HIGH PRIORITY: " + exam.getExamName() +
                        " is in " + ChronoUnit.DAYS.between(LocalDate.now(), exam.getExamDate()) +
                        " days but subject progress is only " + String.format("%.0f%%", progress) +
                        ". Focus on this subject immediately!");
                }
            }
        }

        // Rule 2: Overdue tasks
        List<Task> tasks = taskDAO.findByStudentId(studentId);
        for (Task task : tasks) {
            if (!"COMPLETED".equals(task.getStatus()) && task.getDeadline() != null &&
                task.getDeadline().isBefore(LocalDate.now())) {
                recommendations.add("🔴 OVERDUE: \"" + task.getTitle() +
                    "\" was due on " + task.getDeadline() + ". Complete it ASAP!");
            }
        }

        // Rule 3: Tasks due within 2 days
        for (Task task : tasks) {
            if (!"COMPLETED".equals(task.getStatus()) && task.getDeadline() != null) {
                long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadline());
                if (daysLeft >= 0 && daysLeft <= 2) {
                    recommendations.add("🟡 URGENT: \"" + task.getTitle() +
                        "\" is due in " + daysLeft + " day(s). Start working on it!");
                }
            }
        }

        // Rule 4: Subjects with low progress
        for (Exam exam : upcomingExams) {
            double progress = progressService.getSubjectProgress(exam.getSubjectId());
            if (progress < 40 && exam.getSubjectName() != null) {
                recommendations.add("📚 " + exam.getSubjectName() +
                    " progress is low (" + String.format("%.0f%%", progress) +
                    "). Consider scheduling more study sessions.");
            }
        }

        // Default recommendation
        if (recommendations.isEmpty()) {
            recommendations.add("✅ You're on track! Keep up the good work.");
        }

        return recommendations;
    }

    /**
     * Get the top N most urgent tasks using PriorityQueue and Strategy pattern.
     */
    public List<Task> getTopPriorityTasks(int studentId, int n) {
        List<Task> pendingTasks = taskDAO.findByStudentId(studentId);
        TaskPriorityQueue queue = new TaskPriorityQueue();

        for (Task task : pendingTasks) {
            if (!"COMPLETED".equals(task.getStatus())) {
                // Use the current strategy to recalculate priority
                int calculatedPriority = priorityStrategy.calculatePriority(task);
                task.setPriority(calculatedPriority);
                queue.addTask(task);
            }
        }

        return queue.getTopN(n);
    }

    public List<String> generateRecommendations(int studentId) {
        return getRecommendations(studentId);
    }

    public List<Task> getPrioritizedTasks(int studentId) {
        return getTopPriorityTasks(studentId, 5);
    }
}
