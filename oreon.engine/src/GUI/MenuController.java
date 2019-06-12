package GUI;

import generators.Config;
import generators.MainGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class MenuController {

    @FXML private Button SingleButton;
    @FXML private Button MultipleButton;
    @FXML private Button generateSampleHeightmapsButton;

    private final FileChooser fileChooser = new FileChooser();

    public MenuController() {
//        fileChooser.setInitialDirectory(new File("./oreon.engine/res"));
        fileChooser.setInitialDirectory(new File(Config.PATH));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Heightmaps", "*.bmp"));
    }


    public void SingleFileButton(){
        File file = fileChooser.showOpenDialog(SingleButton.getScene().getWindow());
        if(file == null){
            try {
                ((Stage)SingleButton.getScene().getWindow()).setScene(new Scene(FXMLLoader.load(getClass().getResource("Error.fxml")), 210,100));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            Files.copy(file.toPath(), FileSystems.getDefault().getPath(Config.PATH + "heightmap/", "2.bmp"), REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //start engine
        SingleButton.getScene().getWindow().hide();
        LaunchGame.launch();
    }

    public void generateSampleHeightmaps() {
        try{
            generateSampleHeightmapsButton.getScene().getWindow().hide();
            Stage stage = new Stage();

            stage.setTitle("Loading");
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("Generating.fxml")), 240,100));
            stage.setAlwaysOnTop(true);
            stage.show();

            if(MainGenerator.generate()){
                ((Stage)generateSampleHeightmapsButton.getScene().getWindow()).show();
                stage.hide();
                ((Stage)generateSampleHeightmapsButton.getScene().getWindow()).setScene(new Scene(FXMLLoader.load(getClass().getResource("MapsGenerated.fxml")), 240,100));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void MultipleFileButton() throws IOException {
        ((Stage)MultipleButton.getScene().getWindow()).setScene(new Scene(FXMLLoader.load(getClass().getResource("MultipleMapsPresets.fxml")), 81,97));
    }
}
