package com.smartstudy.javafx;

import com.smartstudy.report.StudentReportGenerator;
import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manages background threading for long-running operations.
 * Uses JavaFX Task and ExecutorService so the UI thread stays responsive.
 * 
 * Demonstrates: Multithreading with JavaFX.
 */
public class ThreadManager {

    private final ExecutorService executor;

    public ThreadManager() {
        // Single-thread executor for background tasks
        this.executor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true); // Allow JVM to exit even if this thread is running
            return t;
        });
    }

    /**
     * Generates a weekly report in the background.
     * Returns a JavaFX Task that can be bound to UI progress indicators.
     * 
     * @param studentId   the student's ID
     * @param studentName the student's name
     * @return a JavaFX Task<String> where the result is the generated file name
     */
    public Task<String> generateReportAsync(int studentId, String studentName) {
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                updateMessage("Initializing report generation...");
                updateProgress(0, 100);

                // Simulate some work stages
                updateMessage("Fetching student data...");
                updateProgress(20, 100);
                Thread.sleep(500); // Simulate data fetching delay

                updateMessage("Calculating progress...");
                updateProgress(40, 100);
                Thread.sleep(300);

                updateMessage("Generating report content...");
                updateProgress(60, 100);

                StudentReportGenerator generator = new StudentReportGenerator(studentId, studentName);
                String fileName = generator.generateAndSave();

                updateMessage("Writing report to file...");
                updateProgress(80, 100);
                Thread.sleep(300);

                updateMessage("Report generated successfully!");
                updateProgress(100, 100);

                return fileName;
            }
        };

        executor.submit(task);
        return task;
    }

    /**
     * Shuts down the executor service.
     */
    public void shutdown() {
        executor.shutdownNow();
    }
}
