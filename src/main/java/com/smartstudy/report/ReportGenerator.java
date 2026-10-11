package com.smartstudy.report;

/**
 * Abstract base class for report generation.
 * Demonstrates: Abstract class design.
 */
public abstract class ReportGenerator {

    protected int studentId;

    public ReportGenerator(int studentId) {
        this.studentId = studentId;
    }

    /** Generate the full report content as a string. */
    public abstract String generateContent();

    /** Get the default file name for this report. */
    public abstract String getFileName();

    /** Template method: generate and save the report. */
    public String generateAndSave() throws Exception {
        String content = generateContent();
        String fileName = getFileName();
        com.smartstudy.util.FileUtil.writeReport(fileName, content);
        return fileName;
    }
}
