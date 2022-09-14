package nz.ac.auckland.se206;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class StatisticsController {

  @FXML private Label lblBestTime;
  @FXML private Label lblBestWord;
  @FXML private PieChart pieChart;

  @FXML
  private void initialize() {

    UserProfile user = SceneManager.getProfile(SceneManager.getMainUser());
    if (!(user == null)) {
      lblBestTime.setText(String.valueOf(user.getBestTime()) + "s");
      lblBestWord.setText(user.getBestWord());

      PieChart.Data slice1 = new PieChart.Data("Wins", user.getWins());
      PieChart.Data slice2 = new PieChart.Data("Losses", user.getLosses());

      pieChart.getData().add(slice1);
      pieChart.getData().add(slice2);
    }
  }
}
