package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.UserProfile.Mode;

public class GameSettingsController {
  @FXML private Button btnMenu;
  @FXML private ToggleButton rbtnAccuracyE;
  @FXML private ToggleButton rbtnAccuracyM;
  @FXML private ToggleButton rbtnAccuracyH;
  @FXML private ToggleButton rbtnWordsE;
  @FXML private ToggleButton rbtnWordsM;
  @FXML private ToggleButton rbtnWordsH;
  @FXML private ToggleButton rbtnWordsMaster;
  @FXML private ToggleButton rbtnTimeE;
  @FXML private ToggleButton rbtnTimeM;
  @FXML private ToggleButton rbtnTimeH;
  @FXML private ToggleButton rbtnTimeMaster;
  @FXML private ToggleButton rbtnConfidenceE;
  @FXML private ToggleButton rbtnConfidenceM;
  @FXML private ToggleButton rbtnConfidenceH;
  @FXML private ToggleButton rbtnConfidenceMaster;
  @FXML private Label lblCurrentMode;
  @FXML private Label lblAccuracyDesc;
  @FXML private Label lblWordsDesc;
  @FXML private Label lblTimeDesc;
  @FXML private Label lblConfidenceDesc;
  @FXML private VBox vboxSettings;

  private UserProfile currentUser = SceneManager.getProfile(SceneManager.getMainUser());

  @FXML
  private void initialize() {

    setAccuracySettings();
    setWordsSettings();
    setTimeSettings();
    setConfidenceSettings();

    // get the previous node and update the current mode label
    setCurrentModeLabel();
    // check if the current mode is Zen, where we disable the visibility of the
    // game settings
    if (currentUser.isZenMode()) {
      vboxSettings.setVisible(false);
    }
  }

  private void setAccuracySettings() {
    // look thru the previously selected ACCURACY settings and
    // set the settings on the new page
    if (currentUser.getAccuracy() == 3) {
      rbtnAccuracyE.setSelected(true);
      lblAccuracyDesc.setText("*your word must be in the top 3");
    } else if (currentUser.getAccuracy() == 2) {
      rbtnAccuracyM.setSelected(true);
      lblAccuracyDesc.setText("*your word must be in the top 2");
    } else {
      rbtnAccuracyH.setSelected(true);
      lblAccuracyDesc.setText("*your word must be the top word");
    }
  }

  private void setWordsSettings() {
    // looks thru the previous settings for WORDS
    if (currentUser.getWordsSettings() == 3) {
      rbtnWordsE.setSelected(true);
      lblWordsDesc.setText("*you will get a word from the easy category");
    } else if (currentUser.getWordsSettings() == 2) {
      rbtnWordsM.setSelected(true);
      lblWordsDesc.setText("*you will get a word from the easy and medium category");
    } else if (currentUser.getWordsSettings() == 1) {
      rbtnWordsH.setSelected(true);
      lblWordsDesc.setText("*you will get a word from the easy, medium and hard category");
    } else {
      rbtnWordsMaster.setSelected(true);
      lblWordsDesc.setText("*true masters play hard words only");
    }
  }

  private void setTimeSettings() {
    // look thru previous settings for TIME
    if (currentUser.getTimeSettings() == 60) {
      rbtnTimeE.setSelected(true);
      lblTimeDesc.setText("*you have 60 seconds to draw");
    } else if (currentUser.getTimeSettings() == 45) {
      rbtnTimeM.setSelected(true);
      lblTimeDesc.setText("*you have 45 seconds to draw");
    } else if (currentUser.getTimeSettings() == 30) {
      rbtnTimeH.setSelected(true);
      lblTimeDesc.setText("*you have 30 seconds to draw");
    } else {
      rbtnTimeMaster.setSelected(true);
      lblTimeDesc.setText("*you have 15 seconds to draw");
    }
  }

  private void setConfidenceSettings() {
    // look thru previous settings for CONFIDENCE
    if (currentUser.getConfidence() == 1) {
      rbtnConfidenceE.setSelected(true);
      lblConfidenceDesc.setText("*your drawing is at least legible");
    } else if (currentUser.getConfidence() == 10) {
      rbtnConfidenceM.setSelected(true);
      lblConfidenceDesc.setText("*your drawing is getting better...");
    } else if (currentUser.getConfidence() == 25) {
      rbtnConfidenceH.setSelected(true);
      lblConfidenceDesc.setText("*your drawing is pretty good!");
    } else {
      rbtnConfidenceMaster.setSelected(true);
      lblConfidenceDesc.setText("*your drawing is an artwork!");
    }
  }

  @FXML
  private void onSwitchToMenu() {
    Scene sceneBtnIsIn = btnMenu.getScene();
    sceneBtnIsIn.setRoot(SceneManager.getUi(SceneManager.AppUi.MENU));
  }

  @FXML
  private void onSetAccuracyEasy() throws IOException {
    currentUser.setAccuracy(3);
    // update the file
    currentUser.saveData();
    setAccuracySettings();
  }

  @FXML
  private void onSetAccuracyMedium() throws IOException {
    currentUser.setAccuracy(2);
    currentUser.saveData();
    setAccuracySettings();
  }

  @FXML
  private void onSetAccuracyHard() throws IOException {
    currentUser.setAccuracy(1);
    currentUser.saveData();
    setAccuracySettings();
  }

  @FXML
  private void onSetWordsEasy() throws IOException {
    currentUser.setWordsSettings(3);
    currentUser.saveData();
    setWordsSettings();
  }

  @FXML
  private void onSetWordsMedium() throws IOException {
    currentUser.setWordsSettings(2);
    currentUser.saveData();
    setWordsSettings();
  }

  @FXML
  private void onSetWordsHard() throws IOException {
    currentUser.setWordsSettings(1);
    currentUser.saveData();
    setWordsSettings();
  }

  @FXML
  private void onSetWordsMaster() throws IOException {
    currentUser.setWordsSettings(0);
    currentUser.saveData();
    setWordsSettings();
  }

  @FXML
  private void onSetTimeEasy() throws IOException {
    currentUser.setTimeSettings(60);
    currentUser.saveData();
    setTimeSettings();
  }

  @FXML
  private void onSetTimeMedium() throws IOException {
    currentUser.setTimeSettings(45);
    currentUser.saveData();
    setTimeSettings();
  }

  @FXML
  private void onSetTimeHard() throws IOException {
    currentUser.setTimeSettings(30);
    currentUser.saveData();
    setTimeSettings();
  }

  @FXML
  private void onSetTimeMaster() throws IOException {
    currentUser.setTimeSettings(15);
    currentUser.saveData();
    setTimeSettings();
  }

  @FXML
  private void onSetConfidenceEasy() throws IOException {
    currentUser.setConfidence(1);
    currentUser.saveData();
    setConfidenceSettings();
  }

  @FXML
  private void onSetConfidenceMedium() throws IOException {
    currentUser.setConfidence(10);
    currentUser.saveData();
    setConfidenceSettings();
  }

  @FXML
  private void onSetConfidenceHard() throws IOException {
    currentUser.setConfidence(25);
    currentUser.saveData();
    setConfidenceSettings();
  }

  @FXML
  private void onSetConfidenceMaster() throws IOException {
    currentUser.setConfidence(50);
    currentUser.saveData();
    setConfidenceSettings();
  }

  @FXML
  private void onSetModeToZen() throws IOException {
    currentUser.setMode(Mode.ZEN);
    // set the current mode label to zen
    setCurrentModeLabel();
    vboxSettings.setVisible(false);
    currentUser.saveData();
  }

  @FXML
  private void onSetToHidden() throws IOException {
    currentUser.setMode(Mode.HIDDENWORD);
    // set the current mode label to zen
    setCurrentModeLabel();
    vboxSettings.setVisible(true);
    currentUser.saveData();
  }

  @FXML
  private void onSetToNormal() throws IOException {
    currentUser.setMode(Mode.NORMAL);
    // set the current mode label to normal
    setCurrentModeLabel();
    // enable the visibilty of the settings
    vboxSettings.setVisible(true);
    currentUser.saveData();
  }

  private void setCurrentModeLabel() {
    // get the previous node and update the current mode label
    lblCurrentMode.setText("Current Mode: " + currentUser.getMode());
  }
}
