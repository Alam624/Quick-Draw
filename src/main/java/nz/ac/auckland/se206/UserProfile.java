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
  // design choice for adding a separate arraylist for zen mode:
  // since this has a different purpose to speed drawing, users
  // should be given to draw ALL words in zen mode but should
  // not be repeated AND not added to the list of words
  private ArrayList<String> zenWords;
  private String bestName;
  private int bestTime;
  private int rounds;
  private int zenRounds;

  // fields for game settings; possible values 3-easy, 2-medium, 1-hard, 0-master
  private int accuracy;
  private int wordsSettings;
  // possible values: 60-easy, 45-medium, 30-hard, 15-master
  private int timeSettings;
  // possible values: 1-easy, 10-medium, 25-hard, 50-master
  private int confidence;

  // fields for the different modes
  public static enum Mode {
    ZEN,
    NORMAL,
    HIDDENWORD
  }

  private Mode mode;

  public UserProfile(String name) {
    // add underscore to names with spaces
    this.name = name.replace(" ", "_");
    this.wins = 0;
    this.loss = 0;
    this.words = new ArrayList<String>();
    this.zenWords = new ArrayList<String>();
    this.bestName = "NIL";
    // default best time is -1 which is recognised as no best time
    this.bestTime = -1;
    this.rounds = 0;
    this.zenRounds = 0;

    // default should be easy
    this.accuracy = 3;
    this.wordsSettings = 3;
    this.timeSettings = 60;
    this.confidence = 1;

    // default mode should be normal
    this.mode = Mode.NORMAL;
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
    writer.write(zenWords.toString() + "\n");
    writer.write(bestName + "\n");
    writer.write(bestTime + "\n");
    writer.write(rounds + "\n");
    writer.write(zenRounds + "\n");
    writer.write(accuracy + "\n");
    writer.write(wordsSettings + "\n");
    writer.write(timeSettings + "\n");
    writer.write(confidence + "\n");
    writer.write(mode + "\n");

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
    this.zenWords =
        new ArrayList<String>(
            Arrays.asList(
                reader.readLine().replace("[", "").replace("]", "").replace(" ", "").split(",")));
    this.bestName = reader.readLine();
    this.bestTime = Integer.valueOf(reader.readLine());
    this.rounds = Integer.valueOf(reader.readLine());
    this.zenRounds = Integer.valueOf(reader.readLine());
    this.accuracy = Integer.valueOf(reader.readLine());
    this.wordsSettings = Integer.valueOf(reader.readLine());
    this.timeSettings = Integer.valueOf(reader.readLine());
    this.confidence = Integer.valueOf(reader.readLine());
    this.mode = Mode.valueOf(reader.readLine());

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
    return this.bestTime;
  }

  public String getBestWord() {
    return this.bestName;
  }

  public void setBestTime(int time) {
    this.bestTime = time;
  }

  public void setBestWord(String word) {
    this.bestName = word;
  }

  public int getWins() {
    return this.wins;
  }

  public int getLosses() {
    return this.loss;
  }

  public void addRound() {
    this.rounds++;
  }

  public void newRound() {
    this.words = new ArrayList<String>();
    addRound();
  }

  /*SETTERS AND GETTERS for GAME SETTINGS*/
  public int getAccuracy() {
    return this.accuracy;
  }

  public int getWordsSettings() {
    return this.wordsSettings;
  }

  public int getTimeSettings() {
    return this.timeSettings;
  }

  public int getConfidence() {
    return this.confidence;
  }

  public void setAccuracy(int accuracy) {
    this.accuracy = accuracy;
  }

  public void setWordsSettings(int wordsSettings) {
    this.wordsSettings = wordsSettings;
  }

  public void setTimeSettings(int timeSettings) {
    this.timeSettings = timeSettings;
  }

  public void setConfidence(int confidence) {
    this.confidence = confidence;
  }

  public Mode getMode() {
    return this.mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }

  /**
   * This method returns a boolean if the mode is Zen. Created since Zen Mode will have a big impact
   * on the Canvas page.
   *
   * @return if the current mode is Zen
   */
  public boolean isZenMode() {
    return this.mode == Mode.ZEN;
  }

  public boolean isHiddenMode() {
    return this.mode == Mode.HIDDENWORD;
  }

  public ArrayList<String> getZenWords() {
    return this.zenWords;
  }

  public void addZenWords(String word) {
    this.zenWords.add(word);
  }

  public void newZenRound() {
    this.zenWords = new ArrayList<String>();
    this.zenRounds++;
  }

  public String getName() {
    return this.name;
  }
}
