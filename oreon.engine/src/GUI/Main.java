package GUI;

import java.io.IOException;

import generators.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Main extends Application {

    @Override
    public void start(final Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));

        stage.setTitle("Terrain Generator");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void main(String[] args) {
//        Config.PATH = System.getProperty("user.dir") + "/oreon.engine/res/";
        Config.PATH = System.getProperty("user.dir") + "/res/";
        launch(args);
    }
}