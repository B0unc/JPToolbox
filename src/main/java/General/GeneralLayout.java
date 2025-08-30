// Main size of the layout is 1280 X 720 im too lazy to figure out scaling for this as of 8/25/25
package General;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GeneralLayout {
    @FXML
    private BorderPane MainPane; // Where we load our FXML files to
    // The left side is always taken for scene transitions

    Map<String, Pane> LoaderPane = new HashMap<>(); // Better caching for loading panes
    // FilePath is our key and Pane is our object
    /*
        Function: LoadFXMLFiles
        Return: Pane
        Description: each time we click on the button we get the FXML file in the resource folder and load it into the map
     */
    private Pane LoadFXMLFiles(String filePath){

        if(LoaderPane.containsKey(filePath)){
            return LoaderPane.get(filePath);
        }

        if(getClass().getResource(filePath) == null){
            System.out.println("File not found " + filePath);
        }

        // Assume that the FXML file is not loaded in, yet
        // We need to get the resource includes File path and pane put it into our map
        // if something went wrong, an error should appear
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(filePath));
            Pane getPane = fxmlLoader.load();
            LoaderPane.put(filePath, getPane);
            return getPane;
        } catch (IOException e) {
            // Set the alert box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Getting pane Error");
            alert.setHeaderText(null);
            alert.setContentText("Error Loading Pane\n");
            alert.showAndWait();
            throw new RuntimeException(e);
        }

    }
    @FXML
    public void SwitchToHomeScene(){
        Pane Home = LoadFXMLFiles("/com/example/toolbox/home.fxml");
        MainPane.setCenter(Home);
        System.out.println("Switching to home scene");
    }
    @FXML
    public void SwitchToRenameFilesScene(){
        Pane Rename = LoadFXMLFiles("/com/example/toolbox/MainFileRename.fxml");
        MainPane.setCenter(Rename);
        System.out.println("Switching to rename scene");
    }

    @FXML
    public void SwitchToMKVRemove(){
        Pane MKVR = LoadFXMLFiles("/com/example/toolbox/MKVReIdx.fxml");
        MainPane.setCenter(MKVR);
        System.out.println("Switching to MKV remove scene");
    }

    public void initialize() {
        // Debugging to find files
        System.out.println("Testing file paths:");
        System.out.println("home.fxml found: " + (getClass().getResource("/com/example/toolbox/index.fxml") != null)); // Make sure to add / to the begging since fxml and java class are in two different folders
        System.out.println("MainFileRename.fxml found: " + (getClass().getResource("/com/example/toolbox/MainFileRename.fxml") != null));
        System.out.println("MKVReIdx.fxml found: " + (getClass().getResource("/com/example/toolbox/MKVReIdx.fxml") != null));
    }

}
