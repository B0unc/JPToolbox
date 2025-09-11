module com.example.toolbox {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
    requires java.desktop;
    requires javafx.graphics;
    requires java.logging;


    opens AppMainToolbox to javafx.fxml;
    exports AppMainToolbox;
    exports RenameFile;
    opens RenameFile to javafx.fxml;
    exports General;
    opens General to  javafx.fxml;
    exports MKVRemover;
    opens MKVRemover to  javafx.fxml;
}