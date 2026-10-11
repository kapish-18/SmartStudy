package com.smartstudy.strategy;

import com.smartstudy.model.Task;

/**
 * Abstract strategy for calculating task priority scores.
 * Demonstrates: Polymorphism through the Strategy design pattern.
 * 
 * Different implementations calculate priority differently
 * (deadline-based, exam-based, etc.).
 */
public abstract class PriorityStrategy {

    /**
     * Calculate a priority score for the given task.
     * Lower score = higher priority (more urgent).
     * 
     * @param task the task to evaluate
     * @return priority score (1 = most urgent)
     */
    public abstract int calculatePriority(Task task);

    /**
     * Returns a human-readable name for this strategy.
     */
    public abstract String getStrategyName();
}
