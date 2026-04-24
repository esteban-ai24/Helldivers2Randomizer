package edu.utsa.cs3443.helldivers2randomizer;

import edu.utsa.cs3443.helldivers2randomizer.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterScreenController {

    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private TextField confirmPasswordTextField;
    @FXML private Label registerResultLabel;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void onRegisterButtonClick(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();

        if (username == null || username.isBlank() || password == null || password.isBlank() || confirmPassword == null || confirmPassword.isBlank()) {
            registerResultLabel.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            registerResultLabel.setText("Passwords do not match!");
            return;
        }

        User newUser = User.createUserFile(username.trim(), password);

        if (newUser != null) {
            registerResultLabel.setText("Registration Successful!");
            User.setCurrentUser(newUser);
            handleLoginRedirect(event);
        } else {
            registerResultLabel.setText("User already exists or error occurred.");
        }
    }

    public void handleLoginRedirect(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/utsa/cs3443/helldivers2randomizer/Layouts/login-screen.fxml"));
            Parent root = fxmlLoader.load();

            // Get the stage from the existing scene
            if (stage == null) {
                stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            }

            LoginScreenController loginController = fxmlLoader.getController();
            if (loginController != null) {
                loginController.setStage(stage);
            }

            // Swapping the root keeps the window maximized and prevents size jumping
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}