package edu.utsa.cs3443.helldivers2randomizer;

import edu.utsa.cs3443.helldivers2randomizer.Model.LoadOut;
import edu.utsa.cs3443.helldivers2randomizer.Model.LoadOutService;
import edu.utsa.cs3443.helldivers2randomizer.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for the main Randomizer screen.
 * This controller is responsible for:
 * <ul>
 *     <li>Displaying the current loadout and its images</li>
 *     <li>Generating and rerolling loadouts</li>
 *     <li>Navigating to save/load screens</li>
 *     <li>Handling user logout</li>
 * </ul>
 * It interacts with {@link LoadOutService} to manage loadout state
 * and uses JavaFX for UI rendering and navigation.
 */
public class RandomizerScreenController {

    /** Root layout container */
    @FXML private StackPane rootPane;

    /** Background image view */
    @FXML private ImageView backgroundImageView;

    /** Label displaying welcome message */
    @FXML private Label welcomeText;

    /** Image views for the four stratagems */
    @FXML private ImageView myImage1;
    @FXML private ImageView myImage2;
    @FXML private ImageView myImage3;
    @FXML private ImageView myImage4;

    /** Main application stage used for navigation */
    private Stage mainStage;

    /** Currently displayed loadout */
    private LoadOut currentLoadout;

    /** Service used for loadout management */
    private static LoadOutService loadOutService = new LoadOutService();

    /**
     * Initializes the controller after FXML loading.
     * Binds UI elements, sets welcome text, and loads the active loadout.
     */
    @FXML
    private void initialize() {
        backgroundImageView.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(rootPane.heightProperty());

        if (User.getCurrentUser() != null && welcomeText != null) {
            welcomeText.setText("Welcome, " + User.getCurrentUser().getUsername());
        }

        currentLoadout = LoadOutService.getActiveLoadout();
        if (currentLoadout != null) {
            showLoadout(currentLoadout);
        } else {
            hideImages();
        }
    }

    /**
     * Handles the reroll button click.
     * Generates a new loadout if none exists, or rerolls the current one.
     * Updates the UI and persists it as the active loadout.
     * @param event the action event triggered by the button
     */
    @FXML
    public void rerollButtonClick(ActionEvent event) {
        if (currentLoadout == null) {
            currentLoadout = LoadOutService.createRandomLoadout("Loadout");
        } else{
            LoadOutService.rerollLoadout(currentLoadout);
        }
        showLoadout(currentLoadout);
        LoadOutService.setActiveLoadout(currentLoadout);
    }

    /**
     * Handles navigation to the Save screen in save mode.
     * @param event the action event triggered by the button
     * @throws RuntimeException if the FXML cannot be loaded
     */
    @FXML
    private void saveButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/edu/utsa/cs3443/helldivers2randomizer/Layouts/save-screen.fxml"));
            Parent root = loader.load();

            SaveScreenController controller = loader.getController();
            Stage currentStage = (Stage) rootPane.getScene().getWindow();

            controller.setStage(currentStage);
            controller.setLoadOutService(loadOutService);//passes loaded instance
            controller.setSaveMode(true);

            currentStage.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Handles user logout and navigates back to the login screen.
     * @param actionEvent the action event triggered by the logout button
     * @throws RuntimeException if the FXML cannot be loaded
     */
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
            System.out.println("Logout successful...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Navigates to the Save screen in load mode to view saved loadouts.
     * @param event the action event triggered by the button
     */
    @FXML
    public void onViewSavedClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/utsa/cs3443/helldivers2randomizer/Layouts/save-screen.fxml"));
            Parent root = fxmlLoader.load();

            SaveScreenController saveScreen = fxmlLoader.getController();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            saveScreen.setStage(stage);
            saveScreen.setLoadOutService(loadOutService);
            saveScreen.setSaveMode(false);

            stage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the main application stage.
     * @param mainStage the primary stage for scene switching
     */
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    /**
     * Sets the LoadOutService instance used by this controller.
     * Allows sharing state across controllers.
     * @param service the LoadOutService instance
     */
    public void setLoadOutService(LoadOutService service) {
        this.loadOutService = service;
        // Re-load stratagems only if the list is empty (i.e. fresh instance wasn't used)
    }

    /**
     * Displays a loadout by setting images for each stratagem.
     * @param load the LoadOut to display
     */
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

    /**
     * Sets an image on an ImageView from a resource path.
     * @param imageView the ImageView to update
     * @param path the resource path of the image
     */
    private void setImageFromPath(ImageView imageView, String path) {
        if (path == null || path.isBlank()) {
            imageView.setImage(null);
            imageView.setVisible(false);
            return;
        }
        imageView.setImage(new Image(getClass().getResourceAsStream(path)));
        imageView.setVisible(true);
    }

    /**
     * Hides all stratagem images from the UI.
     */
    private void hideImages() {
        myImage1.setVisible(false);
        myImage2.setVisible(false);
        myImage3.setVisible(false);
        myImage4.setVisible(false);
    }
}
