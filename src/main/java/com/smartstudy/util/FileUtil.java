package com.smartstudy.util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File handling utility class.
 * Handles writing and reading weekly academic report text files.
 * 
 * Demonstrates: Java File I/O.
 */
public class FileUtil {

    private static final String REPORTS_DIR = "reports";

    /**
     * Writes content to a text file. Creates the reports directory if it doesn't exist.
     * 
     * @param fileName the name of the file (e.g., "weekly_report_2026_10_06.txt")
     * @param content  the text content to write
     * @throws IOException if writing fails
     */
    public static void writeReport(String fileName, String content) throws IOException {
        Path reportsPath = Paths.get(REPORTS_DIR);
        if (!Files.exists(reportsPath)) {
            Files.createDirectories(reportsPath);
        }

        Path filePath = reportsPath.resolve(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write(content);
        }
        System.out.println("Report written to: " + filePath.toAbsolutePath());
    }

    /**
     * Reads and returns the content of a report file.
     * 
     * @param fileName the name of the file to read
     * @return the file content as a string
     * @throws IOException if reading fails or file not found
     */
    public static String readReport(String fileName) throws IOException {
        Path filePath = Paths.get(REPORTS_DIR, fileName);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Report not found: " + filePath.toAbsolutePath());
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    /**
     * Lists all available report files in the reports directory.
     * 
     * @return list of report file names
     */
    public static List<String> listReports() {
        List<String> reports = new ArrayList<>();
        Path reportsPath = Paths.get(REPORTS_DIR);

        if (Files.exists(reportsPath) && Files.isDirectory(reportsPath)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(reportsPath, "*.txt")) {
                for (Path entry : stream) {
                    reports.add(entry.getFileName().toString());
                }
            } catch (IOException e) {
                System.err.println("Error listing reports: " + e.getMessage());
            }
        }
        return reports;
    }
}
