package cs.cvut.fel.dbs;

import cs.cvut.fel.dbs.app_logic.DatabaseConnection;
import cs.cvut.fel.dbs.visuals.GUI;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static Stage stage;

    private void prepare() {
        logger.info("Starting application...");
        stage.setTitle("Standard App Window");
        GUI.setStage(stage);
        DatabaseConnection.connect();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        prepare();
        GUI.showMainMenu();
    }
}