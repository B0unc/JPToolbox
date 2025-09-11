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
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
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
    Boolean debug = true;

    FileChooser fileChooser = new FileChooser(); // Object to get the files
    List<Path> videoList = new ArrayList<>();
    List<Path> subList = new ArrayList<>();

    /*
        - Able to get multiple files (you cant get the folder, yet that's for later)
        - We need to be able to print the file names to the gui

        **TO0-DO LIST** (Get the major parts done first)
        - Check the file type
            - We only want video files
            - And subtitles
     */
    // This fucking function is going to get me into some trouble
    public void getUserFiles(){
        List<File> fileList = fileChooser.showOpenMultipleDialog(new Stage());
        if(fileList != null){
            for(File file : fileList){
                System.out.println(file.getName()); // Debugging
                displayUserFile(file.toPath());
            }
        }
    }

    // Add the file to our display
    public void displayUserFile(Path file){
        // Debugging
        System.out.println(FilenameUtils.getExtension(file.getFileName().toString()));
        if(FilenameUtils.getExtension(file.getFileName().toString()).equals("srt")
                || FilenameUtils.getExtension(file.getFileName().toString()).equals("ass")){
            subList.add(file);
            UserSubFileList.getItems().add(file.getFileName().toString());
        }
        else{
            videoList.add(file);
            UserFileList.getItems().add(file.getFileName().toString());
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
            PreviewShowTitle.setText(NameShow + "[1 - " + NumShow + "]");
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
        Description: Renames both the video and subtitle files. If you don't clear the UserNameShow and num a bug will occur when the user
        trys to use the rename function twice, It will continue to count from the last input. For example, the first pass the user enters a total number of shows as
        6, and on the second pass the user enters 3. You will expect the file to be name title.1 title.2 and title.3, but if you don't clear the two list after the first pass
        it will produce this output title.7 title.8 and title.9

        ** Someday I will implement a more readable solution but now idc
    */
    public void RenameUserFiles(int i) throws IOException {
        System.out.println("Renaming UserFiles");
        if(UserSubFileList.getItems().isEmpty() || UserFileList.getItems().isEmpty()
        || NameShow == null || NumShow == 0){
            if(NameShow == null){
                System.out.println("Please enter a file name or enter a new one");
            }
            else if(NumShow == 0){
                System.out.println("Please enter how many Episodes there are");
            }
            else{
                System.out.println("No user files to rename");
            }
            DeleteCurrentFiles();
            return;
        }

        for(int idx = 0; idx < NumShow; idx++){
            // Getting the info for the file rename
            String baseRename = NameShow + i;
            Path videoFile = videoList.get(idx);
            Path subFile = subList.get(idx);
            String VideoExtension = FilenameUtils.getExtension(videoFile.toString());
            String SubExtension = FilenameUtils.getExtension(subFile.toString());
            String newVideoName = baseRename + "." + VideoExtension;
            String newSubName = baseRename + "." + SubExtension;

            if(debug){DisplayRenameUserFilesDebug(i,baseRename, VideoExtension, SubExtension, newVideoName, newSubName, videoFile, subFile);}

            //Create the new file
            boolean video_rename_success = renameTheFile(videoFile,newVideoName);
            boolean sub_rename_success = renameTheFile(subFile,newSubName);
            if(video_rename_success && sub_rename_success){
                System.out.println("Renamed successfully");
            }
            i++;
        }
        System.out.println("We have exited the for loop");
        DeleteCurrentFiles();
        NameShow = null;
    }

    private void DisplayRenameUserFilesDebug(int currentIdx, String baseRename, String VideoExtension, String SubExtension,
                                             String newVideoName, String newSubName, Path videoFile, Path subFile){

            System.out.println(currentIdx);
            System.out.println("New Name: " + baseRename + "\n");
            System.out.println("Video Info\n" +
                    "Video's Path: " + videoFile + "\n" +
                    "Video's Extension: " + VideoExtension + "\n" +
                    "Video's new Name: " + newVideoName + "\n");
            System.out.println("Sub Info\n" +
                    "Sub's Path: " + subFile + "\n" +
                    "Sub's Extension: " + SubExtension + "\n" +
                    "Sub's new Name: " + newSubName + "\n");
            System.out.println("-----------------------------------------------------------------------------------\n");
    }

    private Boolean renameTheFile(Path oldUserFile, String newName) throws IOException{
        Path newUserFile = oldUserFile.getParent().resolve(newName);
        Files.move(oldUserFile, newUserFile);
        return true;
    }


    public void DeleteCurrentFiles(){
        UserSubFileList.getItems().clear();
        UserFileList.getItems().clear();
        UserNameShow.clear();
        UserNumShow.clear();
        videoList.clear();
        subList.clear();
    }

    // Main entry point for the rename files function
    public void runUserFiles() throws IOException {
        RenameUserFiles(1);
    }

    // Set the default user file path
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("C:\\"));
        DeleteCurrentFiles();
        enableDragAndDropReordering(UserFileList);
    }
}
