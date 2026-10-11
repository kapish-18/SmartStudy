package com.smartstudy.strategy;

import com.smartstudy.model.Task;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Deadline-based priority strategy.
 * Tasks closer to their deadline get higher priority (lower score).
 */
public class DeadlinePriorityStrategy extends PriorityStrategy {

    @Override
    public int calculatePriority(Task task) {
        if (task.getDeadline() == null) return 5;
        
        long daysUntilDeadline = ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadline());
        
        if (daysUntilDeadline < 0) return 1;      // Overdue
        if (daysUntilDeadline <= 1) return 1;      // Due today/tomorrow
        if (daysUntilDeadline <= 3) return 2;      // Due within 3 days
        if (daysUntilDeadline <= 7) return 3;      // Due within a week
        if (daysUntilDeadline <= 14) return 4;     // Due within 2 weeks
        return 5;                                   // Far away
    }

    @Override
    public String getStrategyName() {
        return "Deadline Priority";
    }
}
