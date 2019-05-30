import java.io.IOException;
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
        stage.setScene(new Scene(root, 226, 156));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}