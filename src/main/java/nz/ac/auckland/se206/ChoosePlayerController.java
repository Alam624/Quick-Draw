package nz.ac.auckland.se206;

import java.io.File;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ChoosePlayerController {

  // used to access the name to be added to the list in other controllers
  private static String name = ""; // default an empty string

  public static void setName(String playerName) {
    name = playerName;
  }

  public static String getName() {
    return name;
  }

  @FXML private Button btnCreatePlayer;
  @FXML private Button btnOk;
  @FXML private Button btnCancel;
  @FXML private Button btnDelete;
  @FXML private ListView<String> lstvPlayers;

  /**
   * This is the entry method when the Choose Player instance is first loaded into the stage. Reads
   * the existing user data/files and sets the list view in the page.
   *
   * @throws NumberFormatException If inappropriate string format is trying to convert to int
   * @throws IOException If errors occur when reading the files
   */
  @FXML
  private void initialize() throws NumberFormatException, IOException {

    // load the list of players by firstly getting the list of files
    File folder = new File("src/main/resources/data/users");
    if (!folder.exists()) {
      folder.mkdir();
    }
    File[] files = folder.listFiles();

    // for each file name, add it into the list
    for (File file : files) {
      if (!file.isHidden()) { // ignores any hidden files
        String userName = file.getName().replace("_", " ").replace(".txt", "");
        lstvPlayers.getItems().add(userName);

        // load the file data via UserProfile constructor
        UserProfile user = new UserProfile(userName);
        user.readDataFromFile(file);
        // add this to the profile map
        SceneManager.storeProfile(userName, user);
      }
    }

    // during initialise where the main user is null, cancel must always be disabled
    btnCancel.setDisable(true);
    btnCancel.setVisible(false);
    // disable property depends whether or not there is a selected item
    btnOk
        .disableProperty()
        .bind(Bindings.isEmpty(lstvPlayers.getSelectionModel().getSelectedItems()));
    btnDelete
        .disableProperty()
        .bind(Bindings.isEmpty(lstvPlayers.getSelectionModel().getSelectedItems()));
  }

  /**
   * This method is executed when the plus button is clicked. It creates a secondary stage and loads
   * the create player root. When the secondary stage is closed, this checks for the name of the
   * created user and adds it into the list IF the user name is not an empty string.
   *
   * @throws IOException If errors occur when trying to load the fxml file
   */
  @FXML
  private void onCreate() throws IOException {
    // creating the create player pop up
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/fxml/" + "createplayer" + ".fxml"));
    Parent root = loader.load();

    Stage stage = SceneManager.setModal(btnCreatePlayer, root);

    CreatePlayerController controller = loader.getController();
    controller.setStage(stage);
    stage.showAndWait(); // ensures the pop up is interacted first

    // add the user name to the list
    if (!name.isEmpty()) {
      lstvPlayers.getItems().add(name);
    }
    name = ""; // reset name variable
  }

  /**
   * This method is executed when the minus sign is clicked. This will delete the selected item from
   * the list view and delete its corresponding user file.
   */
  @FXML
  private void onDelete() {
    // only start deleting when an item is selected
    if (!lstvPlayers.getSelectionModel().isEmpty()) {
      // delete the user instance from hash map
      SceneManager.deleteProfile(lstvPlayers.getSelectionModel().getSelectedItem());
      // delete the file
      File file =
          new File(
              "src/main/resources/data/users",
              lstvPlayers.getSelectionModel().getSelectedItem().replace(" ", "_") + ".txt");
      file.delete();
      // remove the name from list view
      lstvPlayers.getItems().remove(lstvPlayers.getSelectionModel().getSelectedIndex());

      // check if the main user is not in the list, if deleted, disable cancel btn
      if (!lstvPlayers.getItems().contains(SceneManager.getMainUser())) {
        btnCancel.setDisable(true);
      }
    }
  }

  /**
   * This method passes the chosen user profile to the SceneManager and create the UserProfile
   * instance.
   *
   * @throws IOException If errors occur when trying to load the fxml file
   */
  @FXML
  private void onChoose() throws IOException {
    // get the name of the selected user
    String userName = lstvPlayers.getSelectionModel().getSelectedItem();
    if (userName != null) { // prevents the case when user did not select anything from the list

      // check if previous userName is the same as the selected one
      if (!SceneManager.getMainUser().equalsIgnoreCase(userName)) {
        // if they are not the same user, remove the CANVAS key from the sceneMap
        // and reset the menu
        SceneManager.deleteCanvas(); // we cannot load others players canvas
        SceneManager.setMainUser(userName); // set user befpre initialising menu
        SceneManager.storeUi(SceneManager.AppUi.MENU, App.loadFxml("menu"));
      }

      btnCancel.setDisable(false);
      btnCancel.setVisible(true);
      // switch to the main menu aka onCancel
      onCancel();
    }
  }

  /**
   * This is executed when the cross button is clicked, this will load the menu of the previously
   * selected user.
   */
  @FXML
  private void onCancel() {
    btnCancel.getScene().setRoot(SceneManager.getUi(SceneManager.AppUi.MENU));
  }
}
