package edu.utsa.cs3443.helldivers2randomizer;

import edu.utsa.cs3443.helldivers2randomizer.Model.LoadOutService;
import edu.utsa.cs3443.helldivers2randomizer.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for the login screen.
 * This class handles:
 * <ul>
 *     <li>User authentication (login validation)</li>
 *     <li>Navigating to the registration screen</li>
 *     <li>Navigating to the main application after successful login</li>
 *     <li>Initializing user session state</li>
 * </ul>
 */
public class LoginScreenController {

    /** Input field for username */
    @FXML private TextField usernameTextField;

    /** Input field for password */
    @FXML private TextField passwordTextField;

    /** Label used to display login error or status messages */
    @FXML private Label loginResultLabel;

    /** Primary application stage used for navigation */
    private Stage stage;

    /**
     * Handles login button click event.
     * Validates input fields, checks user credentials, and logs the user in
     * if authentication succeeds. Otherwise displays an error message.
     */
    @FXML
    public void onLoginButtonClick() {
        String username = usernameTextField.getText().trim();
        String password = passwordTextField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            loginResultLabel.setText("Invalid username and/or password!");
            return;
        }

        User foundUser = User.signIn(username, password);
        if (foundUser == null) {
            loginResultLabel.setText("Invalid username and/or password!");
            return;
        }
        login(foundUser);
    }

    /**
     * Navigates to the registration screen when the register button is clicked.
     * Loads the registration FXML and swaps the current scene root.
     * @param event the action event triggered by the button
     */
    @FXML
    public void onRegisterButtonClick(ActionEvent event) {
        // Ensure stage is set via the event if it hasn't been injected yet
        if (this.stage == null) {
            this.stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/utsa/cs3443/helldivers2randomizer/Layouts/register-screen.fxml"));
            Parent root = fxmlLoader.load();

            RegisterScreenController register = fxmlLoader.getController();
            register.setStage(stage);

            // KEY CHANGE: Swap root, not scene.
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            loginResultLabel.setText("Error loading register screen.");
        }
    }

    /**
     * Sets the primary stage for this controller.
     * @param stage the main application stage
     */
    public void setStage(Stage stage){
        this.stage = stage;
    }

    /**
     * Handles successful login logic.
     * Sets the current user session, loads saved loadouts,
     * and navigates to the save/load screen.
     * @param user the authenticated user
     */
    private void login(User user){
        User.setCurrentUser(user);
        LoadOutService.setActiveLoadout(null);
        LoadOutService.loadCurrentUserLoadouts();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/utsa/cs3443/helldivers2randomizer/Layouts/save-screen.fxml"));
            Parent root = fxmlLoader.load();

            SaveScreenController saveScreen = fxmlLoader.getController();
            saveScreen.setStage(stage);
            saveScreen.setSaveMode(false);

            // KEY CHANGE: Keep it maximized and consistent by swapping root
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            loginResultLabel.setText("Error loading save screen.");
        }
    }
}