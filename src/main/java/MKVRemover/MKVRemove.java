package MKVRemover;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MKVRemove implements Initializable {
    @FXML
    Button BrowseFiles;
    @FXML
    ListView<String> displayUserFilesMKV;

    // non-FXML variables
    FileChooser fileChooser = new FileChooser();
    List<File> videoFiles = new ArrayList<>();



    public void browseForFiles(){
        System.out.println("Browse for files");
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if(files!=null){
            for(File file:files){
                displayUserFile(file);
            }
        }
    }

    public void displayUserFile(File file){
        // Debugging
        System.out.println(FilenameUtils.getExtension(file.getName()));
        videoFiles.add(file);
        displayUserFilesMKV.getItems().add(file.getName());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\"));
        if(displayUserFilesMKV != null) {
            displayUserFilesMKV.getItems().clear();
        }
    }
}


