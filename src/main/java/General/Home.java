package General;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class Home {

    public void openGithubLink() throws IOException {
        System.out.println("Opening Github Link");
        try {
            Desktop.getDesktop().browse(URI.create("https://github.com/B0unc/JPToolbox"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
