package cs.cvut.fel.dbs.main.tables;

import cs.cvut.fel.dbs.main.GUI;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RecordsController {
    private static final Logger logger = LogManager.getLogger(RecordsController.class);
    public GridPane Grid;
    public Label recordInfo;
    public Button infoButton;
    public Button editButton;
    public Button deleteButton;

    public GridPane getRecordGrid(String recordInfoText) {
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
