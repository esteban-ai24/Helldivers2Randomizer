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

public class LoginScreenController {

    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private Label loginResultLabel;

    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private void initialize(){}

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

    private void login(User user){
        User.setCurrentUser(user);
        HD2Application.setActiveLoadout(null);
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