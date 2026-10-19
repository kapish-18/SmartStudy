package com.smartstudy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the SmartStudy JavaFX application.
 */
public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root, 1000, 650);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        stage.setTitle("SmartStudy - Intelligent Academic Planning");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    /**
     * Navigate to a different FXML screen while preserving window size,
     * position, maximized state, and fullscreen mode.
     */
    public static void switchScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/fxml/" + fxmlFile));
            Scene currentScene = primaryStage != null ? primaryStage.getScene() : null;
            if (currentScene == null) {
                Scene scene = new Scene(root, 1000, 650);
                scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
                if (primaryStage != null) {
                    primaryStage.setScene(scene);
                }
            } else {
                // Preserves fullscreen, maximized state, and window size seamlessly
                currentScene.setRoot(root);
                if (currentScene.getStylesheets().isEmpty()) {
                    currentScene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
                }
            }
        } catch (Exception e) {
            System.err.println("Error switching scene to " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get the primary stage (for dialogs, etc.).
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
