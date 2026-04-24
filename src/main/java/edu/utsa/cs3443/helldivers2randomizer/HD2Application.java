package edu.utsa.cs3443.helldivers2randomizer;

import edu.utsa.cs3443.helldivers2randomizer.Model.LoadOut;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class HD2Application extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HD2Application.class.getResource(
                "/edu/utsa/cs3443/helldivers2randomizer/Layouts/login-screen.fxml"));

        // Create the initial scene once.
        // We don't hardcode 1920x1080 so it adapts to any screen.
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.web("#0b0c0d"));

        LoginScreenController controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setTitle("HD2 RANDOMIZER");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}