package edu.utsa.cs3443.helldivers2randomizer;

import edu.utsa.cs3443.helldivers2randomizer.Model.LoadOut;
import edu.utsa.cs3443.helldivers2randomizer.Model.LoadOutService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for the Save/Load screen in the application.
 * This controller manages:
 * <ul>
 *     <li>Displaying available save slots</li>
 *     <li>Saving the current loadout to a slot</li>
 *     <li>Loading an existing loadout from a slot</li>
 *     <li>Creating a new randomized loadout</li>
 *     <li>Navigation back to the randomizer screen</li>
 * </ul>
 *
 * The controller operates in two modes:
 * <ul>
 *     <li><b>Save Mode:</b> Allows overwriting save slots</li>
 *     <li><b>Load Mode:</b> Allows selecting and loading existing slots</li>
 * </ul>
 */
public class SaveScreenController {

    /** Button for save slot 1 */
    @FXML private Button save1Button;

    /** Button for save slot 2 */
    @FXML private Button save2Button;

    /** Button for save slot 3 */
    @FXML private Button save3Button;

    /** Main title label */
    @FXML private Label titleLabel;

    /** Subtitle/instruction label */
    @FXML private Label subtitleLabel;

    /** Primary application stage used for navigation */
    private Stage stage;

    /** Indicates whether the screen is in save mode (true) or load mode (false) */
    private boolean saveMode;

    /** Service used for loadout creation, saving, and loading */
    private LoadOutService loadOutService;

    /** Handles click event for Save Slot 1 */
    @FXML private void onSave1Click() { handleSlot("save1"); }

    /** Handles click event for Save Slot 2 */
    @FXML private void onSave2Click() { handleSlot("save2"); }

    /** Handles click event for Save Slot 3 */
    @FXML private void onSave3Click() { handleSlot("save3"); }

    /**
     * Handles the "Create New" button action.
     * In save mode: overwrites slot 1.
     * In load mode: creates a new random loadout and navigates forward.
     */
    @FXML
    private void onCreateNewClick() {
        if (saveMode) {
            handleSlot("save1");
            return;
        }
        LoadOut fresh = loadOutService.createRandomLoadout("Loadout");
        if(fresh == null){
            System.out.println("loadout creation failed.");
            return;
        }
        LoadOutService.setActiveLoadout(fresh);
        openRandomizer();
    }

    /**
     * Initializes the controller after FXML loading.
     * Loads stratagem data and prepares the UI.
     */
    @FXML
    private void initialize() {
        loadOutService = new LoadOutService();
        LoadOutService.loadStratagemNames();
        refresh();
    }

    /**
     * Sets the main application stage.
     * @param stage the primary stage used for scene navigation
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Injects the LoadOutService used by this controller.
     * Typically called before navigating to this screen.
     * @param loadOutService the service instance to use
     */
    public void setLoadOutService(LoadOutService loadOutService) {
        this.loadOutService = loadOutService;
    }

    /**
     * Sets the current mode (save or load) and refreshes the UI.
     * @param saveMode true for save mode, false for load mode
     */
    public void setSaveMode(boolean saveMode) {
        this.saveMode = saveMode;
        refresh();
    }


    /**
     * Refreshes all UI elements based on the current mode and slot data.
     * Updates labels and button states.
     */
    private void refresh() {
        if (titleLabel != null) {
            titleLabel.setText(saveMode ? "SELECT SAVE SLOT" : "SELECT LOADOUT");
        }
        if (subtitleLabel != null) {
            subtitleLabel.setText(saveMode
                    ? "Pick a slot to overwrite with your current loadout"
                    : "Choose an occupied slot to load, or start a new run");
        }
        updateButton(save1Button, "save1", "Save 1");
        updateButton(save2Button, "save2", "Save 2");
        updateButton(save3Button, "save3", "Save 3");
    }

    /**
     * Updates a save slot button's text, style, and enabled state
     * based on whether the slot contains a saved loadout.
     * @param button the button to update
     * @param slot the slot identifier (e.g., "save1")
     * @param baseText the base label text for the button
     */
    private void updateButton(Button button, String slot, String baseText) {
        if (button == null) return;
        LoadOut slotLoadout = LoadOutService.loadSlot(slot);
        if (slotLoadout == null) {
            button.setText(baseText + "  [EMPTY]");
            button.getStyleClass().remove("save-slot-occupied");
            if (!button.getStyleClass().contains("save-slot-empty")) button.getStyleClass().add("save-slot-empty");
            button.setDisable(!saveMode);
        } else {
            button.setText(baseText + "  [" + slotLoadout.getName() + "]");
            button.getStyleClass().remove("save-slot-empty");
            if (!button.getStyleClass().contains("save-slot-occupied")) button.getStyleClass().add("save-slot-occupied");
            button.setDisable(false);
        }
    }

    /**
     * Handles saving or loading a slot depending on the current mode.
     * @param slot the slot identifier
     */
    private void handleSlot(String slot) {
        if (saveMode) {
            LoadOut active = LoadOutService.getActiveLoadout();
            if (active == null) return;
            LoadOutService.saveSlot(slot, active);
            LoadOutService.setActiveLoadout(active);
            openRandomizer();
            return;
        }

        LoadOut slotLoadout = LoadOutService.loadSlot(slot);
        if (slotLoadout != null) {
            LoadOutService.setActiveLoadout(slotLoadout);
        } else {
            LoadOutService.setActiveLoadout(null);
        }
        openRandomizer();
    }

    /**
     * Navigates to the Randomizer screen and passes required dependencies.
     * Loads the FXML, sets the controller dependencies, and updates the stage scene.
     * @throws RuntimeException if the FXML cannot be loaded
     */
    private void openRandomizer() {
        if (stage == null){
            System.out.println("ERROR: stage is null - cannot navigate");
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/edu/utsa/cs3443/helldivers2randomizer/Layouts/randomizer-screen.fxml"));
            Parent root = fxmlLoader.load();

            RandomizerScreenController controller = fxmlLoader.getController();
            controller.setMainStage(stage);
            controller.setLoadOutService(loadOutService);

            stage.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
