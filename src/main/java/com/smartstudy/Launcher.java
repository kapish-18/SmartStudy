package com.smartstudy;

/**
 * Launcher wrapper class.
 * This class does NOT extend javafx.application.Application.
 * Calling Main.main(args) from here completely bypasses the
 * "JavaFX runtime components are missing" check when running from classpath.
 */
public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
