package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class MenuController {

    @FXML private Button SingleButton;
    @FXML private Button MultipleButton;

    final FileChooser fileChooser = new FileChooser();

    public void SingleFileButton(){
//        fileChooser.setInitialDirectory(new File("C:\\Users\\kopcion\\Desktop\\TerrainGeneration\\oreon.engine\\res"));
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
            Files.copy(file.toPath(), FileSystems.getDefault().getPath("C:\\Users\\kopcion\\Desktop\\TerrainGeneration\\oreon.engine\\res\\heightmap", "heightmap1.bmp"), REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("single button");

        //start engine
        LaunchGame.launch();
        SingleButton.getScene().getWindow().hide();
    }

    public void MultipleFileButton() throws IOException {
        ((Stage)MultipleButton.getScene().getWindow()).setScene(new Scene(FXMLLoader.load(getClass().getResource("MultipleMapsPresets.fxml")), 81,97));
    }
}
