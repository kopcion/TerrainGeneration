package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ErrorController {
    @FXML private Button OkButton;

    public void OK(){
        try {
            ((Stage)OkButton.getScene().getWindow()).setScene(new Scene(FXMLLoader.load(getClass().getResource("Menu.fxml")), 226, 156));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
