package com.smartstudy.service;

import com.smartstudy.dao.TaskDAO;
import com.smartstudy.dao.impl.TaskDAOImpl;
import com.smartstudy.exception.InvalidTaskException;
import com.smartstudy.model.Task;
import com.smartstudy.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for task management.
 * Demonstrates: InvalidTaskException usage, ArrayList collection.
 */
public class TaskService {

    private final TaskDAO taskDAO;

    public TaskService() {
        this.taskDAO = new TaskDAOImpl();
    }

    public List<Task> getTasksByStudentId(int studentId) {
        return taskDAO.findByStudentId(studentId);
    }

    public List<Task> getTasksBySubjectId(int subjectId) {
        return taskDAO.findBySubjectId(subjectId);
    }

    /**
     * Adds a new task with validation.
     * Throws InvalidTaskException if the deadline is in the past or title is empty.
     */
    public int addTask(Task task) throws InvalidTaskException {
        if (ValidationUtil.isNullOrEmpty(task.getTitle())) {
            throw new InvalidTaskException("Task title cannot be empty.");
        }
        if (task.getDeadline() != null && ValidationUtil.isPastDate(task.getDeadline())) {
            throw new InvalidTaskException("Task deadline cannot be in the past: " + task.getDeadline());
        }
        if (!ValidationUtil.isValidPriority(task.getPriority())) {
            throw new InvalidTaskException("Priority must be between 1 and 5. Got: " + task.getPriority());
        }
        if (task.getStatus() == null) {
            task.setStatus("PENDING");
        }
        return taskDAO.insert(task);
    }

    public boolean updateTask(Task task) throws InvalidTaskException {
        if (ValidationUtil.isNullOrEmpty(task.getTitle())) {
            throw new InvalidTaskException("Task title cannot be empty.");
        }
        return taskDAO.update(task);
    }

    public boolean markComplete(int taskId) {
        return taskDAO.updateStatus(taskId, "COMPLETED");
    }

    public boolean deleteTask(int id) {
        return taskDAO.delete(id);
    }

    /** Get only pending/in-progress tasks. */
    public List<Task> getPendingTasks(int studentId) {
        List<Task> all = taskDAO.findByStudentId(studentId);
        List<Task> pending = new ArrayList<>();
        for (Task t : all) {
            if (!"COMPLETED".equals(t.getStatus())) {
                pending.add(t);
            }
        }
        return pending;
    }

    public int getCompletedCount(int studentId) {
        return taskDAO.countByStudentIdAndStatus(studentId, "COMPLETED");
    }

    public int getPendingCount(int studentId) {
        return taskDAO.countByStudentIdAndStatus(studentId, "PENDING") +
               taskDAO.countByStudentIdAndStatus(studentId, "IN_PROGRESS");
    }
}
