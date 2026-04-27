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

/**
 * Controller for the user registration screen.
 * This controller handles:
 * <ul>
 *     <li>User input validation (username and password fields)</li>
 *     <li>Creating a new user account</li>
 *     <li>Displaying registration status messages</li>
 *     <li>Redirecting to the login screen upon successful registration</li>
 * </ul>
 */
public class RegisterScreenController {

    /** Text field for entering the username */
    @FXML private TextField usernameTextField;

    /** Text field for entering the password */
    @FXML private TextField passwordTextField;

    /** Text field for confirming the password */
    @FXML private TextField confirmPasswordTextField;

    /** Label used to display registration results or error messages */
    @FXML private Label registerResultLabel;

    /** Primary stage used for navigation */
    private Stage stage;

    /**
     * Handles the register button click event.
     * Validates user input, ensures passwords match, and attempts
     * to create a new user account. Displays appropriate messages
     * based on the result.
     * @param event the action event triggered by the register button
     */
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

    /**
     * Sets the main application stage.
     * @param stage the primary stage for scene navigation
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Redirects the user to the login screen after registration.
     * Loads the login screen FXML, passes the stage to the next controller,
     * and replaces the current scene root to maintain window state.
     * @param actionEvent the event used to retrieve the current stage if not already set
     */
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