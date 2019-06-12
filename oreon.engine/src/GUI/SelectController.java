package GUI;

import generators.Config;
import generators.FileGenerator;
import generators.utils.Smoother;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("ALL")
public class SelectController {
    @FXML private Button First;
    @FXML private Button Second;
    @FXML private CheckBox Smoothed;

    final FileChooser fileChooser = new FileChooser();
    double values[][];
    double values1[][];
    double values2[][];

    public SelectController() {
//        fileChooser.setInitialDirectory(new File("./oreon.engine/res"));
        fileChooser.setInitialDirectory(new File(Config.PATH));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Heightmaps", "*.bmp"));
    }


    public void firstButton() throws IOException {
        boolean error = false;
        try {
            List<File> files = fileChooser.showOpenMultipleDialog(First.getScene().getWindow());
            if(files.size() < 3)
                error = true;
            else {
                values = FileGenerator.loadFromFile(files.get(0));
                values1 = FileGenerator.loadFromFile(files.get(1));
                values2 = FileGenerator.loadFromFile(files.get(2));
            }
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
        First.getScene().getWindow().hide();
        LaunchGame.launch();
    }

    public void secondButton() throws IOException {
        boolean error = false;
        try {
            List<File> files = fileChooser.showOpenMultipleDialog(First.getScene().getWindow());
            if(files.size() < 3)
                error = true;
            else {
                values = FileGenerator.loadFromFile(files.get(0));
                values1 = FileGenerator.loadFromFile(files.get(1));
                values2 = FileGenerator.loadFromFile(files.get(2));
            }
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
        First.getScene().getWindow().hide();
        LaunchGame.launch();
    }
}
