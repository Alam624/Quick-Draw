package nz.ac.auckland.se206;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class StatisticsController {

  @FXML private Label lblBestTime;
  @FXML private Label lblBestWord;
  @FXML private PieChart pieChart;
  @FXML private Button btnBack;

  @FXML
  private void initialize() {

    //set the labels to show the relevant information for the current user
    UserProfile user = SceneManager.getProfile(SceneManager.getMainUser());
    if (!(user == null)) {
      lblBestTime.setText(String.valueOf(user.getBestTime()) + "s");
      lblBestWord.setText(user.getBestWord());

      //add and display wins and losses on the piechart
      PieChart.Data slice1 = new PieChart.Data("Wins", user.getWins());
      PieChart.Data slice2 = new PieChart.Data("Losses", user.getLosses());

      pieChart.getData().add(slice1);
      pieChart.getData().add(slice2);
    }
  }

  @FXML 
  private void onBack() {
    btnBack.getScene().setRoot(SceneManager.getUi(SceneManager.AppUi.MENU)); //returns to menu
  }
}
