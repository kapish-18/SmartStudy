package com.smartstudy.util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * File handling utility class.
 * Handles writing and reading weekly academic report text files across
 * both the standalone desktop client and the Tomcat web application.
 * 
 * Demonstrates: Java File I/O (BufferedReader, BufferedWriter, Path, Files).
 */
public class FileUtil {

    /**
     * Resolves candidate directories where reports may exist or should be written.
     * Ensures compatibility when running from JavaFX or from Tomcat/Eclipse.
     */
    public static List<Path> getCandidateDirectories() {
        List<Path> dirs = new ArrayList<>();

        // 1. Explicit system property if set
        String custom = System.getProperty("smartstudy.reports.dir");
        if (custom != null && !custom.trim().isEmpty()) {
            dirs.add(Paths.get(custom.trim()).toAbsolutePath().normalize());
        }

        // 2. Project workspace location on the user's desktop
        String userHome = System.getProperty("user.home");
        if (userHome != null) {
            Path projectReports = Paths.get(userHome, "Desktop", "vit projects", "SmartStudy", "reports").toAbsolutePath().normalize();
            dirs.add(projectReports);

            // User home global shared directory
            Path sharedReports = Paths.get(userHome, ".smartstudy", "reports").toAbsolutePath().normalize();
            dirs.add(sharedReports);
        }

        // 3. Current working directory ./reports
        dirs.add(Paths.get("reports").toAbsolutePath().normalize());

        // 4. Check parent directories in case running inside server or subfolder
        try {
            Path current = Paths.get(".").toAbsolutePath().normalize();
            for (int i = 0; i < 4 && current != null; i++) {
                Path check = current.resolve("reports");
                if (!dirs.contains(check)) {
                    dirs.add(check);
                }
                current = current.getParent();
            }
        } catch (Exception ignored) {}

        return dirs;
    }

    /**
     * Returns the primary directory to write new reports to.
     */
    public static Path getPrimaryReportsDirectory() {
        String userHome = System.getProperty("user.home");
        if (userHome != null) {
            Path projectReports = Paths.get(userHome, "Desktop", "vit projects", "SmartStudy", "reports");
            try {
                if (!Files.exists(projectReports)) {
                    Files.createDirectories(projectReports);
                }
                return projectReports.toAbsolutePath().normalize();
            } catch (Exception ignored) {}
        }

        Path local = Paths.get("reports").toAbsolutePath().normalize();
        try {
            if (!Files.exists(local)) {
                Files.createDirectories(local);
            }
        } catch (Exception ignored) {}
        return local;
    }

    /**
     * Writes content to a text file.
     * 
     * @param fileName the name of the file (e.g., "weekly_report_2026_10_19.txt")
     * @param content  the text content to write
     * @throws IOException if writing fails
     */
    public static void writeReport(String fileName, String content) throws IOException {
        Path primaryDir = getPrimaryReportsDirectory();
        Path filePath = primaryDir.resolve(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write(content);
        }
        System.out.println("Report written to: " + filePath.toAbsolutePath());

        // Also mirror to global user.home/.smartstudy/reports for multi-process access
        String userHome = System.getProperty("user.home");
        if (userHome != null) {
            try {
                Path shared = Paths.get(userHome, ".smartstudy", "reports");
                if (!Files.exists(shared)) {
                    Files.createDirectories(shared);
                }
                Path sharedFile = shared.resolve(fileName);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(sharedFile.toFile()))) {
                    writer.write(content);
                }
            } catch (Exception ignored) {}
        }
    }

    /**
     * Reads and returns the content of a report file by searching all candidate directories.
     * 
     * @param fileName the name of the file to read
     * @return the file content as a string
     * @throws IOException if reading fails or file not found
     */
    public static String readReport(String fileName) throws IOException {
        Path targetPath = null;
        for (Path dir : getCandidateDirectories()) {
            Path candidate = dir.resolve(fileName);
            if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
                targetPath = candidate;
                break;
            }
        }

        if (targetPath == null) {
            throw new FileNotFoundException("Report file '" + fileName + "' not found in any candidate directory.");
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(targetPath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    /**
     * Lists all available report files from all candidate directories without duplicates.
     * 
     * @return list of report file names
     */
    public static List<String> listReports() {
        Set<String> reports = new LinkedHashSet<>();
        for (Path dir : getCandidateDirectories()) {
            if (Files.exists(dir) && Files.isDirectory(dir)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt")) {
                    for (Path entry : stream) {
                        reports.add(entry.getFileName().toString());
                    }
                } catch (IOException e) {
                    System.err.println("Error listing reports in " + dir + ": " + e.getMessage());
                }
            }
        }
        return new ArrayList<>(reports);
    }
}
