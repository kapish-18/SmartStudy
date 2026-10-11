package com.smartstudy.exception;

/**
 * User-defined exception for invalid task operations.
 * Thrown when a task has an invalid deadline (past date) or missing required fields.
 * 
 * Demonstrates: User-defined Exception handling.
 */
public class InvalidTaskException extends Exception {

    public InvalidTaskException(String message) {
        super(message);
    }

    public InvalidTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
