package com.smartstudy.util;

import java.time.LocalDate;

/**
 * Validation utility class.
 * Provides common validation methods used across services.
 */
public class ValidationUtil {

    /**
     * Checks if a string is null or blank.
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Checks if a deadline is in the past.
     */
    public static boolean isPastDate(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    /**
     * Checks if an email format is roughly valid.
     */
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * Checks if marks are within a valid range (0 to max).
     */
    public static boolean isValidMarks(double marks, double max) {
        return marks >= 0 && marks <= max;
    }

    /**
     * Checks if a priority value is valid (1-5).
     */
    public static boolean isValidPriority(int priority) {
        return priority >= 1 && priority <= 5;
    }
}
