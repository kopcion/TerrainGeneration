package GUI;

import generators.Config;
import generators.FileGenerator;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class CombineMapsController {

    public ImageView firstImage;
    public ImageView secondImage;
    public ImageView combinedImageView;

    private final FileChooser fileChooser = new FileChooser();

    public Button selectFirstButton;
    public Button selectSecondButton;
    public Button combineButton;
    public Button generateInEngineButton;
    public ChoiceBox combiningFunctionChoiceBox;

    double firstImageValues[][];
    boolean pickedFirstImage;
    double secondImageValues[][];
    boolean pickedSecondImage;

    public CombineMapsController() {
        pickedFirstImage = false;
        pickedSecondImage = false;
        fileChooser.setInitialDirectory(new File(Config.PATH));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Heightmaps", "*.bmp"));
    }

    public void initialize() {
        combiningFunctionChoiceBox.getItems().addAll("Minimum", "Maximum", "Add", "Subtract", "Multiply", "Average", "Square root of sum", "Square root of sum of squares");
    }


    public void selectFirstImageButtonClicked(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(selectFirstButton.getScene().getWindow());
        if(file == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please select a file.");
            alert.showAndWait();
        }

        try {
            firstImageValues = FileGenerator.loadFromFile(file);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while reading heightmap file.");
            alert.setContentText("Please make sure, that file is not broken.");
            alert.showAndWait();
        }

        Image image = new Image(file.toURI().toString());
        firstImage.setImage(image);

        pickedFirstImage = true;

        if(pickedFirstImage && pickedSecondImage) {
            combineButton.setDisable(false);
        }
    }

    public void selectSecondImageButtonClicked(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(selectSecondButton.getScene().getWindow());
        if(file == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please select a file.");
            alert.showAndWait();
        }

        try {
            secondImageValues = FileGenerator.loadFromFile(file);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while reading heightmap file.");
            alert.setContentText("Please make sure, that file is not broken.");
            alert.showAndWait();
        }

        Image image = new Image(file.toURI().toString());
        secondImage.setImage(image);
        pickedSecondImage = true;

        if(pickedFirstImage && pickedSecondImage) {
            combineButton.setDisable(false);
        }
    }

    public void combineButtonClicked(ActionEvent actionEvent) {
        try {
            String method = combiningFunctionChoiceBox.getValue().toString();
            double out[][] = new double[firstImageValues.length][firstImageValues[0].length];

            if(method == "Minimum") {
                for(int i=0; i < firstImageValues.length; i++){
                    for(int j=0; j < firstImageValues[0].length; j++){
                        out[i][j] = min(secondImageValues[i][j], firstImageValues[i][j]);
                    }
                }
            } else if(method == "Maximum") {
                for(int i=0; i < firstImageValues.length; i++){
                    for(int j=0; j < firstImageValues[0].length; j++){
                        out[i][j] = max(secondImageValues[i][j], firstImageValues[i][j]);
                    }
                }
            } else if(method == "Add") {
                for(int i=0; i < firstImageValues.length; i++){
                    for(int j=0; j < firstImageValues[0].length; j++){
                        out[i][j] = secondImageValues[i][j] + firstImageValues[i][j];
                    }
                }
            } else if(method == "Subtract") {
                for(int i=0; i < firstImageValues.length; i++){
                    for(int j=0; j < firstImageValues[0].length; j++){
                        out[i][j] = firstImageValues[i][j] - secondImageValues[i][j];
                    }
                }
            } else if(method == "Multiply") {
                for(int i=0; i < firstImageValues.length; i++){
                    for(int j=0; j < firstImageValues[0].length; j++){
                        out[i][j] = firstImageValues[i][j] * secondImageValues[i][j];
                    }
                }
            } else if(method == "Average") {
                for(int i=0; i < firstImageValues.length; i++){
                    for(int j=0; j < firstImageValues[0].length; j++){
                        out[i][j] = (firstImageValues[i][j] + secondImageValues[i][j])/2;
                    }
                }
            } else if(method == "Square root of sum") {
                for(int i=0; i < firstImageValues.length; i++){
                    for(int j=0; j < firstImageValues[0].length; j++){
                        out[i][j] = sqrt(firstImageValues[i][j] + secondImageValues[i][j]);
                    }
                }
            } else if(method == "Square root of sum of squares") {
                for(int i=0; i < firstImageValues.length; i++){
                    for(int j=0; j < firstImageValues[0].length; j++){
                        out[i][j] = sqrt((firstImageValues[i][j]*firstImageValues[i][j]) + (secondImageValues[i][j]*secondImageValues[i][j]));
                    }
                }
            }

            //Normalization:

            double maximumValue = 0f;
            for (int i = 0; i < out.length; i++) {
                for (int j = 0; j < out[0].length; j++) {
                    maximumValue = max(maximumValue, out[i][j]);
                }
            }

            double minimumValue = 1f;
            for (int i = 0; i < out.length; i++) {
                for (int j = 0; j < out[0].length; j++) {
                    minimumValue = min(minimumValue, out[i][j]);
                }
            }

            double range = maximumValue - minimumValue;

            for (int i = 0; i < out.length; i++) {
                for (int j = 0; j < out[0].length; j++) {
                    out[i][j] -= minimumValue;
                    out[i][j] /= range;
                }
            }


            FileGenerator.generateFileInPlace(out);
            File file = new File(Config.PATH + "heightmap/2.bmp");
            Image image = new Image(file.toURI().toString());
            combinedImageView.setImage(image);
            generateInEngineButton.setDisable(false);

        } catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please select a combining method.");
            alert.showAndWait();
        }

    }

    public void generateInEngineButtonClicked(ActionEvent actionEvent) {
        generateInEngineButton.getScene().getWindow().hide();
        LaunchGame.launch();
    }
}
