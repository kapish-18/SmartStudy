package com.smartstudy.strategy;

import com.smartstudy.model.Task;

/**
 * Exam-proximity-based priority strategy.
 * Prioritizes tasks whose associated subject has an upcoming exam.
 * Uses the task's existing priority as a base and boosts it.
 */
public class ExamPriorityStrategy extends PriorityStrategy {

    private boolean hasUpcomingExam;

    public ExamPriorityStrategy(boolean hasUpcomingExam) {
        this.hasUpcomingExam = hasUpcomingExam;
    }

    @Override
    public int calculatePriority(Task task) {
        int basePriority = task.getPriority();
        if (hasUpcomingExam) {
            // Boost priority by 2 levels (lower number = higher priority)
            return Math.max(1, basePriority - 2);
        }
        return basePriority;
    }

    @Override
    public String getStrategyName() {
        return "Exam Priority";
    }
}
