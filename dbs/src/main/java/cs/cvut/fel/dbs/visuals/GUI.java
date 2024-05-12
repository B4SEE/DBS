package cs.cvut.fel.dbs.visuals;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GUI {
    private static final Logger logger = LogManager.getLogger(GUI.class);
    private static Stage stage;
    public static Stage getStage() {
        return stage;
    }
    public static void setStage(Stage stage) {
        GUI.stage = stage;
    }
    private static void checkStage() {
        if (stage == null) {
            logger.error("Stage is not set.");
            System.exit(1);
        }
    }
    public static void showMainMenu() {
        checkStage();
        stage.setScene(MainMenuController.getMainMenuScene());
        stage.show();
    }

    public static Pane loadFXML(String path) {
        Pane grid = null;
        try {
            File fxmlFile = new File(path);
            URL fxmlUrl = fxmlFile.toURI().toURL();
            grid = FXMLLoader.load(fxmlUrl);
        } catch (IOException e) {
            logger.error("Error loading FXML file: " + e.getMessage());
        }
        return grid;
    }
}
