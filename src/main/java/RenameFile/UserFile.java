package RenameFile;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class UserFile implements Initializable {
    //Log
    Logger log = Logger.getLogger(UserFile.class.getName());
    // FXML Controllers
    @FXML
            public Label PreviewTitleLabel;
    public Button SubmitRenameFiles;
    public Button RemoveUserFiles;
    public Button UserNameNumSave;
    public Button FileBrowser;
    @FXML
            private ListView<String> UserFileList;
    @FXML
            private TextField UserNameShow;
    @FXML
            private TextField UserNumShow;

    @FXML
            private Label UserNumError;
    @FXML
            private Label PreviewShowTitle;
    @FXML
            private ListView<String> UserSubFileList;

    // Variables
    String NameShow;
    int NumShow;

    FileChooser fileChooser = new FileChooser(); // Object to get the files
    List<File> videoList = new ArrayList<>();
    List<File> subList = new ArrayList<>();

    /*
        - Able to get multiple files (you cant get the folder, yet that's for later)
        - We need to be able to print the file names to the gui

        **TO0-DO LIST** (Get the major parts done first)
        - Check the file type
            - We only want video files
            - And subtitles
     */
    public void getUserFiles(){
        List<File> fileList = fileChooser.showOpenMultipleDialog(new Stage());
        if(fileList != null){
            for(File file : fileList){
                System.out.println(file.getName()); // Debugging
                displayUserFile(file);
            }
        }
    }

    // Add the file to our display
    public void displayUserFile(File file){
        // Debugging
        System.out.println(FilenameUtils.getExtension(file.getName()));
        if(FilenameUtils.getExtension(file.getName()).equals("srt") || FilenameUtils.getExtension(file.getName()).equals("ass")){
            subList.add(file);
            UserSubFileList.getItems().add(file.getName());
        }
        else{
            videoList.add(file);
            UserFileList.getItems().add(file.getName());
        }
    }

    //Enable drag and drop
    private void enableDragAndDropReordering(ListView<String> listView) {
        listView.setCellFactory(_ -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText((getIndex() + 1) + ". " + item);
                    }
                }
            };

            // Start drag
            cell.setOnDragDetected(event -> {
                if (!cell.isEmpty()) {
                    Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(String.valueOf(cell.getIndex()));
                    db.setContent(content);
                    event.consume();
                }
            });

            // Accept drag
            cell.setOnDragOver(event -> {
                if (event.getGestureSource() != cell &&
                        event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            // Handle drop
            cell.setOnDragDropped(event -> {
                if (event.getDragboard().hasString()) {
                    int draggedIndex = Integer.parseInt(event.getDragboard().getString());
                    int dropIndex = cell.getIndex();

                    if (draggedIndex != dropIndex) {
                        ObservableList<String> items = listView.getItems();
                        String draggedItem = items.get(draggedIndex);

                        items.remove(draggedIndex);
                        if (dropIndex > draggedIndex) {
                            dropIndex--;
                        }
                        items.add(dropIndex, draggedItem);

                        listView.getSelectionModel().select(dropIndex);
                    }
                    event.setDropCompleted(true);
                }
                event.consume();
            });

            return cell;
        });
    }

    // For Button UserNameNumSave
    public void SaveUserNameNum(){
        UserNumError.setText("");
        try{
            NameShow = UserNameShow.getText();
            NumShow = Integer.parseInt(UserNumShow.getText());
            PreviewShowTitle.setText(NameShow + " " + "[1 - " + NumShow + "]");
            // Debugging
            System.out.println(NameShow);
            System.out.println(NumShow);
        }
        catch(NumberFormatException e){
            UserNumError.setText("Please enter a number");

            UserNumShow.clear();
            NumShow = 0;
            UserNameShow.clear();
            NameShow = null;
            // Debugging
            log.info("Please Enter Numbers only");
        }
        catch (Exception e) {
            log.info(e.getMessage());
            UserNumError.setText("Something went wrong Please try again");
        }

    }

    /*
        Name: RenameUserFiles
        Return value: None
        Description: Renames both the video and subtitle files
    */
    public void RenameUserFiles(){
        System.out.println("Renaming UserFiles");
        int i = 1;
        boolean Success;
        // For now Two for loops im fucking lazy
        // The better solution is to do a for loop of the max size of the video or sublist
                // need a file variable for each video and sublist, and then we can rename it
        // I have to get the path of the file
        for(int idx = 0; idx < videoList.size(); idx++){
            // Get the BaseName for the renaming
            // Get the Video and Sub files
            // Get the Extension for each video and sub file
            // Get the Directory for each file
            String baseRename = NameShow + " " + i;
            File videoFile = videoList.get(idx);
            File subFile = subList.get(idx);

            // Get the path of the video and subfile
            Path VideoPath = videoFile.toPath();
            Path SubPath = subFile.toPath();
            // Get the Extension of each file
            String VideoExtension = FilenameUtils.getExtension(VideoPath.toString());
            String SubExtension = FilenameUtils.getExtension(SubPath.toString());
            // Get the new Names for each file
            String newVideoName = baseRename + "." + VideoExtension;
            String newSubName = baseRename + "." + SubExtension;
            // Debugging
            System.out.println("New Name: " + baseRename + "\n");
            System.out.println("Video Info\n" +
                    "Video's Path: " + VideoPath + "\n" +
                    "Video's Extension: " + VideoExtension + "\n" +
                    "Video's new Name: " + newVideoName + "\n");
            System.out.println("Sub Info\n" +
                    "Sub's Path: " + SubPath + "\n" +
                    "Sub's Extension: " + SubExtension + "\n" +
                    "Sub's new Name: " + newSubName + "\n");
            System.out.println("-----------------------------------------------------------------------------------\n");
            //Create the new file
            File New_videoFile = new File(videoFile.getParent(), newVideoName);
            File New_subFile = new File(subFile.getParent(), newSubName);

            boolean b1 = videoFile.renameTo(New_videoFile);
            boolean b2 = subFile.renameTo(New_subFile);
            if(b1 && b2){
                System.out.println("Renamed successfully");
                System.out.println("Video File: " + New_videoFile.getName());
                System.out.println("Sub File: " + New_subFile.getName());

                // Setting the current idx
                videoList.set(idx, New_videoFile);
                subList.set(idx, New_subFile);
                Success = true;

            }
            else {
                if(!b1){
                    log.info("Renaming failed at idx " + idx);
                    log.info("Video File: " + videoFile.getName());

                }
                else {
                    log.info("Renaming failed at idx " + idx);
                    log.info("Sub File: " + subFile.getName());
                }
                Success = false;
            }
            i++;

            if(Success){
                System.out.println("The big function Renamed successfully");
                UserFileList.getItems().clear();
                UserSubFileList.getItems().clear();
            }

        }
    }


    // clears all current user selected files
    public void DeleteCurrentFiles(){
        UserSubFileList.getItems().clear();
        UserFileList.getItems().clear();
    }

    // Set the default user file path
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\"));
        DeleteCurrentFiles();
        enableDragAndDropReordering(UserFileList);
    }
}
