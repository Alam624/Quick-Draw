package nz.ac.auckland.se206.ml;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.BufferedImageFactory;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;

/**
 * This class is responsible for querying the DL model to get the predictions. Code partially
 * adapted from https://github.com/deepjavalibrary/djl-demo.
 */
public class DoodlePrediction {
  /**
   * Prints the top K predictions of a given image under test.
   *
   * @param args BMP file to predict and the number of top K predictions to print.
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model or image cannot be found on the file system.
   * @throws TranslateException If there is an error in reading the input/output of the DL model.
   */
  public static void main(final String[] args)
      throws ModelException, IOException, TranslateException {
    if (args.length != 2) {
      throw new IllegalArgumentException(
          "You are not providing the correct arguments. You need to provide the path of the file"
              + " and the number of top K predictions to print.");
    }

    printPredictions(
        new DoodlePrediction().getPredictions(new File(args[0]), Integer.parseInt(args[1])));
  }

  /**
   * Prints the list of predictions (first element is the predictions in the top 10, and the rest of
   * the predictions are in the second element of the list).
   *
   * @param predictions The list of predictions to print.
   */
  public static void printPredictions(final List<Classifications.Classification> predictions) {
    List<String> predictionStrings = getPredictionString(predictions, 10);

    System.out.println(predictionStrings);
  }

  /**
   * Builds string from the 1st to xth predictions and a string from xth to the size of the
   * predictions list. EDIT: Changed so that it can be used to check the status of the word in the
   * list of predictions
   *
   * @param predictions The list of predictions
   * @param topK separator of the string
   * @return a list of string consisting of the two strings of the prediction list
   */
  public static List<String> getPredictionString(
      final List<Classifications.Classification> predictions, int topK) {

    // initialise the string variable and the integer representing
    // the placement of the classification in the list
    StringBuilder sb = new StringBuilder();
    List<String> strList = new ArrayList<String>();

    // for each classification, append to the string its name
    for (int i = 0; i < topK; i++) {
      sb.append(predictions.get(i).getClassName().replace("_", " "))
          .append(System.lineSeparator())
          .append(System.lineSeparator());
    }
    strList.add(sb.toString());
    sb = new StringBuilder();

    // second list from xth to prediction size
    for (int i = topK; i < predictions.size(); i++) {
      sb.append(predictions.get(i).getClassName().replace("_", " "))
          .append(System.lineSeparator())
          .append(System.lineSeparator());
    }
    strList.add(sb.toString());

    return strList;
  }

  private final ZooModel<Image, Classifications> model;

  /**
   * Constructs the doodle prediction model by loading it from a file.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   */
  public DoodlePrediction() throws ModelException, IOException {
    final ImageClassificationTranslator translator =
        ImageClassificationTranslator.builder()
            .addTransform(new ToTensor())
            .optFlag(Image.Flag.GRAYSCALE)
            .optApplySoftmax(true)
            .build();

    final Criteria<Image, Classifications> criteria =
        Criteria.builder()
            .setTypes(Image.class, Classifications.class)
            // This will not work if the application runs from a JAR.
            .optModelUrls("src/main/resources/ml/doodle_mobilenet.zip")
            .optOption("mapLocation", "true")
            .optTranslator(translator)
            .build();

    model = ModelZoo.loadModel(criteria);
  }

  /**
   * Predicts the categories of the input image, returning the top K predictions. EDIT: Changes have
   * been made so that the img isn't greyscale inverted, this is because, the canvas has been made
   * black instead of white.
   *
   * @param bufImg BufferedImage file to classify.
   * @param k The number of classes to return.
   * @return List of classification results and their confidence level.
   * @throws TranslateException If there is an error in reading the input/output of the DL model.
   */
  public List<Classifications.Classification> getPredictions(BufferedImage bufImg, final int k)
      throws TranslateException {

    // The model requires the image to be 65x65 pixels.
    bufImg =
        Scalr.resize(
            bufImg, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, 65, 65, Scalr.OP_ANTIALIAS);

    final Classifications classifications =
        model.newPredictor().predict(new BufferedImageFactory().fromImage(bufImg));

    return classifications.topK(k);
  }

  /**
   * Predicts the categories of the input image, returning the top K predictions.
   *
   * @param image BMP image file to classify.
   * @param k The number of classes to return.
   * @return List of classification results and their confidence level.
   * @throws IOException If the image is not found on the filesystem.
   * @throws TranslateException If there is an error in reading the input/output of the DL model.
   */
  public List<Classifications.Classification> getPredictions(final File image, final int k)
      throws IOException, TranslateException {
    if (!image.exists()) {
      throw new FileNotFoundException("The file " + image.getAbsolutePath() + " does not exist");
    }

    return getPredictions(ImageIO.read(image), k);
  }

  /**
   * This method checks if the following classification is equal to or more than the predictor
   * provided.
   *
   * @param classification The word probability we are checking
   * @param predictor The percentage that the probability should reach
   * @return if word reaches the required probability
   */
  public boolean isAboveProbability(Classifications.Classification classification, int predictor) {
    return classification.getProbability() * 100 >= predictor;
  }

  /**
   * This method closes the ML prediction model instance. Used after each game/before the canvas
   * instance is replaced.
   */
  public void closeManager() {
    model.getNDManager().close();
  }
}
