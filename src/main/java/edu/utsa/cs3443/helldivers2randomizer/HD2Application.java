package edu.utsa.cs3443.helldivers2randomizer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main entry point for the Helldivers 2 Randomizer application.
 * This class is responsible for:
 * <ul>
 *     <li>Launching the JavaFX application</li>
 *     <li>Loading the initial login screen</li>
 *     <li>Setting up the primary stage and scene</li>
 * </ul>
 */
public class HD2Application extends Application {

/**
 * Initializes and displays the primary stage of the application.
 * Loads the login screen FXML, configures the scene, applies styling,
 * and passes the stage to the login controller.
 * @param stage the primary stage provided by the JavaFX runtime
 * @throws IOException if the FXML file cannot be loaded
 */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HD2Application.class.getResource(
                "/edu/utsa/cs3443/helldivers2randomizer/Layouts/login-screen.fxml"));

        // Create the initial scene once.
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.web("#0b0c0d"));

        LoginScreenController controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setTitle("HD2 RANDOMIZER");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    /**
     * Main method that launches the JavaFX application.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch();
    }
}