package AppMainToolbox;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("/Scenes/index.fxml"));
        System.setProperty("javafx.sg.warn", "true"); // Should fix the crash when using a debugger (This is due to java trying tofind SystemProperty but does not check to see if its there before it does the find)
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("JPLToolbox");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}