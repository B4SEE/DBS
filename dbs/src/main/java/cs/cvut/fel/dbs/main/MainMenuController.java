package cs.cvut.fel.dbs.main;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MainMenuController {
    private static final Logger logger = LogManager.getLogger(MainMenuController.class);
    public static Scene getMainMenuScene() {
        // Load fxml file
        logger.info("Initializing main menu...");
        Pane grid = GUI.loadFXML("layouts/mainMenu.fxml");

        Scene scene = new Scene(grid);
        GUI.getStage().setMaximized(true);
        GUI.getStage().setResizable(false);
        return scene;
    }
}
