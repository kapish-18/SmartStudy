package com.smartstudy.collections;

import com.smartstudy.model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Custom wrapper around Java's PriorityQueue for tasks.
 * Keeps the most urgent tasks at the front.
 * 
 * Demonstrates: Meaningful use of Java Collections (PriorityQueue).
 */
public class TaskPriorityQueue {

    private final PriorityQueue<Task> queue;

    public TaskPriorityQueue() {
        // Task implements Comparable — lower priority number = higher urgency
        this.queue = new PriorityQueue<>();
    }

    /** Add a task to the queue. */
    public void addTask(Task task) {
        if (task != null) {
            queue.offer(task);
        }
    }

    /** Add multiple tasks to the queue. */
    public void addAll(List<Task> tasks) {
        for (Task t : tasks) {
            addTask(t);
        }
    }

    /** Get and remove the most urgent task. */
    public Task pollMostUrgent() {
        return queue.poll();
    }

    /** Peek at the most urgent task without removing it. */
    public Task peekMostUrgent() {
        return queue.peek();
    }

    /** Get the top N most urgent tasks (removes them from the queue). */
    public List<Task> getTopN(int n) {
        List<Task> top = new ArrayList<>();
        for (int i = 0; i < n && !queue.isEmpty(); i++) {
            top.add(queue.poll());
        }
        return top;
    }

    /** Get all tasks as a sorted list (drains the queue). */
    public List<Task> getAllSorted() {
        List<Task> sorted = new ArrayList<>();
        while (!queue.isEmpty()) {
            sorted.add(queue.poll());
        }
        return sorted;
    }

    public int size() { return queue.size(); }
    public boolean isEmpty() { return queue.isEmpty(); }
    public void clear() { queue.clear(); }
}
