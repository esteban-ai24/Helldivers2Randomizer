package edu.utsa.cs3443.helldivers2randomizer;

import edu.utsa.cs3443.helldivers2randomizer.Model.LoadOut;
import edu.utsa.cs3443.helldivers2randomizer.Model.LoadOutService;
import edu.utsa.cs3443.helldivers2randomizer.Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SaveScreenController {

    @FXML private Button save1Button;
    @FXML private Button save2Button;
    @FXML private Button save3Button;
    @FXML private Button createNewButton;
    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;

    private Stage stage;
    private boolean saveMode;
    private LoadOutService loadOutService;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Called by RandomizerScreenController before navigating here
    public void setLoadOutService(LoadOutService loadOutService) {
        this.loadOutService = loadOutService;
    }

    public void setSaveMode(boolean saveMode) {
        this.saveMode = saveMode;
        refresh();
    }

    @FXML
    private void initialize() {
        loadOutService = new LoadOutService();
        LoadOutService.loadStratagemNames();
        refresh();
    }

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

    @FXML private void onSave1Click() { handleSlot("save1"); }
    @FXML private void onSave2Click() { handleSlot("save2"); }
    @FXML private void onSave3Click() { handleSlot("save3"); }

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
