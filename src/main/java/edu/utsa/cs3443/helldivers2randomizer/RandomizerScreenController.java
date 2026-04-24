package edu.utsa.cs3443.helldivers2randomizer;

import edu.utsa.cs3443.helldivers2randomizer.Model.LoadOut;
import edu.utsa.cs3443.helldivers2randomizer.Model.LoadOutService;
import edu.utsa.cs3443.helldivers2randomizer.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RandomizerScreenController {
    @FXML private StackPane rootPane;
    @FXML private ImageView backgroundImageView;
    @FXML private Label welcomeText;
    @FXML private ImageView myImage1;
    @FXML private ImageView myImage2;
    @FXML private ImageView myImage3;
    @FXML private ImageView myImage4;

    private Stage mainStage;
    private LoadOut currentLoadout;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    private void initialize() {
        backgroundImageView.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(rootPane.heightProperty());
        if (User.getCurrentUser() != null && welcomeText != null) {
            welcomeText.setText("Welcome, " + User.getCurrentUser().getUsername());
        }

        currentLoadout = HD2Application.getActiveLoadout();
        if (currentLoadout != null) {
            showLoadout(currentLoadout);
        } else {
            hideImages();
        }
    }

    @FXML
    public void rerollButtonClick(ActionEvent event) {
        if (currentLoadout == null) {
            currentLoadout = LoadOutService.createRandomLoadout("Loadout");
        } else {
            LoadOutService.rerollLoadout(currentLoadout);
        }
        showLoadout(currentLoadout);
        HD2Application.setActiveLoadout(currentLoadout);
    }

    @FXML
    private void saveButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/edu/utsa/cs3443/helldivers2randomizer/Layouts/save-screen.fxml"));
            Parent root = loader.load();

            SaveScreenController controller = loader.getController();
            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            controller.setStage(currentStage);
            controller.setSaveMode(true);

            currentStage.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void showLoadout(LoadOut load) {
        if (load == null) {
            hideImages();
            return;
        }
        setImageFromPath(myImage1, load.getImage1Path());
        setImageFromPath(myImage2, load.getImage2Path());
        setImageFromPath(myImage3, load.getImage3Path());
        setImageFromPath(myImage4, load.getImage4Path());
    }

    private void setImageFromPath(ImageView imageView, String path) {
        if (path == null || path.isBlank()) {
            imageView.setImage(null);
            imageView.setVisible(false);
            return;
        }
        imageView.setImage(new Image(getClass().getResourceAsStream(path)));
        imageView.setVisible(true);
    }

    private void hideImages() {
        myImage1.setVisible(false);
        myImage2.setVisible(false);
        myImage3.setVisible(false);
        myImage4.setVisible(false);
    }

    @FXML
    public void logoutclick(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/utsa/cs3443/helldivers2randomizer/Layouts/login-screen.fxml"));
            Parent root = fxmlLoader.load();
            LoginScreenController login = fxmlLoader.getController();
            if (login != null) {
                login.setStage(mainStage);
            }
            mainStage.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void onViewSavedClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/utsa/cs3443/helldivers2randomizer/Layouts/save-screen.fxml"));
            Parent root = fxmlLoader.load();

            SaveScreenController saveScreen = fxmlLoader.getController();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            saveScreen.setStage(stage);
            saveScreen.setSaveMode(false);

            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
