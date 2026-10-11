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
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        stage.setTitle("SmartStudy - Intelligent Academic Planning");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    /**
     * Navigate to a different FXML screen.
     */
    public static void switchScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/fxml/" + fxmlFile));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
            primaryStage.setScene(scene);
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
