package cs.cvut.fel.dbs.visuals.table_controllers;

import cs.cvut.fel.dbs.visuals.GUI;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RecordsController {
    private static final Logger logger = LogManager.getLogger(RecordsController.class);
    public static GridPane Grid;
    public static Label recordInfo;
    public static Button infoButton;
    public static Button editButton;
    public static Button deleteButton;

    public static GridPane getRecordGrid(String recordInfoText) {
        logger.info("Initializing record grid...");
        GridPane grid = (GridPane) GUI.loadFXML("layouts/record.fxml");
        recordInfo = (Label) grid.lookup("#recordInfo");
        infoButton = (Button) grid.lookup("#infoButton");
        editButton = (Button) grid.lookup("#editButton");
        deleteButton = (Button) grid.lookup("#deleteButton");
        recordInfo.setText(recordInfoText);
        return grid;
    }
}
