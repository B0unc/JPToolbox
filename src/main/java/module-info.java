module com.example.toolbox {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;


    opens com.example.toolbox to javafx.fxml;
    exports com.example.toolbox;
    exports RenameFile;
    opens RenameFile to javafx.fxml;
    exports General;
    opens General to  javafx.fxml;
}