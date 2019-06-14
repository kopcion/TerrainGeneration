package GUI;

import generators.Config;
import generators.MainGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class MenuController {

    @FXML private Button combineHightmapsButton;
    @FXML private Button SingleButton;
    @FXML private Button MultipleButton;
    @FXML private Button generateSampleHeightmapsButton;


    private final FileChooser fileChooser = new FileChooser();

    public MenuController() {
//        fileChooser.setInitialDirectory(new File("./oreon.engine/res"));
        fileChooser.setInitialDirectory(new File(Config.PATH));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Heightmaps", "*.bmp"));
    }

    public void initialize() {
        SingleButton.setFocusTraversable(false);
        generateSampleHeightmapsButton.setFocusTraversable(false);
        MultipleButton.setFocusTraversable(false);
        combineHightmapsButton.setFocusTraversable(false);
    }


    public void SingleFileButton(){
        File file = fileChooser.showOpenDialog(SingleButton.getScene().getWindow());
        if(file == null){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please select a file");
            alert.showAndWait();
            return;
        }
        try {
            Files.copy(file.toPath(), FileSystems.getDefault().getPath(Config.PATH + "heightmap/", "2.bmp"), REPLACE_EXISTING);
        } catch (IOException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while reading heightmap file.");
            alert.setContentText("Please make sure, that file is not broken.");
            alert.showAndWait();
            return;
        }

        //start engine
        SingleButton.getScene().getWindow().hide();
        LaunchGame.launch();
    }

    public void generateSampleHeightmaps() {
        try{
            Stage stage = new Stage();

            stage.setTitle("Loading");
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Generating.fxml")), 300,100));
            stage.setAlwaysOnTop(true);
            stage.show();

            if(MainGenerator.generate()){
                ((Stage)generateSampleHeightmapsButton.getScene().getWindow()).show();
                stage.hide();
                ((Stage)generateSampleHeightmapsButton.getScene().getWindow()).setScene(new Scene(FXMLLoader.load(getClass().getResource("MapsGenerated.fxml")), 400,200));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void MultipleFileButton() throws IOException {
        ((Stage)MultipleButton.getScene().getWindow()).setScene(new Scene(FXMLLoader.load(getClass().getResource("MultipleMapsPresets.fxml")), 500,300));
    }

    public void combineHightmapsButtonClicked() throws IOException {
        ((Stage)combineHightmapsButton.getScene().getWindow()).setScene(new Scene(FXMLLoader.load(getClass().getResource("CombineMaps.fxml")), 700,500));
    }
}
