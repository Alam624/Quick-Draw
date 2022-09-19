package nz.ac.auckland.se206;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class UserProfile {

  // fields that define the data stored for each user
  private String name;
  private int wins;
  private int loss;
  private ArrayList<String> words;
  private String best_name;
  private int best_time;

  public UserProfile(String name) {
    // add underscore to names with spaces
    this.name = name.replace(" ", "_");
    this.wins = 0;
    this.loss = 0;
    this.words = new ArrayList<String>();
    this.best_name = "NIL";
    this.best_time = 60;
  }

  public void saveData() throws IOException {
    // create the file in the data folder
    File file = new File("src/main/resources/data/users", name + ".txt");
    file.createNewFile();

    // write into the file with all its details
    writeData(file);
  }

  /**
   * This method to updates the field details in the specified file. Must be done after every game
   * to update the local data.
   *
   * @param file The file object that we will write on
   * @throws IOException
   */
  public void writeData(File file) throws IOException {
    // write into the file with all its details
    // file writer is set to false so that it overwrites when it is called again
    BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), false));
    writer.write(wins + "\n");
    writer.write(loss + "\n");
    writer.write(words.toString() + "\n");
    writer.write(best_name + "\n");
    writer.write(best_time + "\n");

    writer.close();
  }

  public void readDataFromFile(File file) throws NumberFormatException, IOException {
    // reads data from existing file and updating the userprofile instance fields
    BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));

    // reads each line and parses them to the appropriate format
    this.wins = Integer.valueOf(reader.readLine());
    this.loss = Integer.valueOf(reader.readLine());
    // input is parsed so that the remaining string is "A,B,C"
    // this is then separated via the commas and made into elements of an intermediate array
    // convert this array to ArrayList format
    this.words =
        new ArrayList<String>(
            Arrays.asList(
                reader.readLine().replace("[", "").replace("]", "").replace(" ", "").split(",")));
    this.best_name = reader.readLine();
    this.best_time = Integer.valueOf(reader.readLine());
    reader.close();
  }

  // below are all the simple getter/setter and increment methods
  public void addWin() {
    this.wins += 1;
  }

  public void addLoss() {
    this.loss += 1;
  }

  public ArrayList<String> getWords() {
    return this.words;
  }

  public void addWord(String word) {
    this.words.add(word);
  }

  public int getBestTime() {
    return this.best_time;
  }

  public String getBestWord() {
    return this.best_name;
  }

  public void setBestTime(int time) {
    this.best_time = time;
  }

  public void setBestWord(String word) {
    this.best_name = word;
  }

  public int getWins() {
    return this.wins;
  }

  public int getLosses() {
    return this.loss;
  }
}
