package GUI;

import generators.FileGenerator;
import generators.utils.Smoother;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;

public class SelectController {
    @FXML private Button First;
    @FXML private Button Second;
    @FXML private CheckBox Smoothed;

    final FileChooser fileChooser = new FileChooser();
    double values[][];
    double values1[][];
    double values2[][];


    public void firstButton() throws IOException {
        boolean error = false;
        try {
            values = FileGenerator.loadFromFile(fileChooser.showOpenDialog(First.getScene().getWindow()));
            values1 = FileGenerator.loadFromFile(fileChooser.showOpenDialog(First.getScene().getWindow()));
            values2 = FileGenerator.loadFromFile(fileChooser.showOpenDialog(First.getScene().getWindow()));
        } catch (Exception e) {
            error = true;
        }
        if(values == null || values1 == null || values2 == null || error) {
            ((Stage)First.getScene().getWindow()).setScene(new Scene(FXMLLoader.load(getClass().getResource("Error.fxml")), 210,100));
            return;
        }

        double out[][] = new double[values.length][values[0].length];
        for(int i=0; i < values.length; i++){
            for(int j=0; j < values[0].length; j++){
                out[i][j] = (values2[i][j] + values[i][j] * values1[i][j] + 0.5d * (1 - values2[i][j]) * values1[i][j]) / 2.5d;
            }
        }

        if(Smoothed.isSelected()){
            out = Smoother.smooth(out);
        }

        FileGenerator.generateFileInPlace(out);
    }

    public void secondButton() throws IOException {
        boolean error = false;
        try {
            values = FileGenerator.loadFromFile(fileChooser.showOpenDialog(Second.getScene().getWindow()));
            values1 = FileGenerator.loadFromFile(fileChooser.showOpenDialog(Second.getScene().getWindow()));
        } catch (Exception e) {
            error = true;
        }

        if(values == null || values1 == null || error) {
            ((Stage)Second.getScene().getWindow()).setScene(new Scene(FXMLLoader.load(getClass().getResource("Error.fxml")), 210,100));
            return;
        }

        double out[][] = new double[values.length][values[0].length];
        for(int i=0; i < values.length; i++){
            for(int j=0; j < values[0].length; j++){
                out[i][j] = (values[i][j] + 0.5d * values[i][j] * values1[i][j]) / 1.5d;
            }
        }

        if(Smoothed.isSelected()){
            out = Smoother.smooth(out);
        }

        FileGenerator.generateFileInPlace(out);
    }
}
