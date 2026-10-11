package com.smartstudy.exception;

/**
 * User-defined exception for invalid login attempts.
 * Thrown when credentials are incorrect or user is not found.
 */
public class InvalidLoginException extends Exception {

    public InvalidLoginException(String message) {
        super(message);
    }

    public InvalidLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
