package cs.cvut.fel.dbs.visuals;

import cs.cvut.fel.dbs.visuals.table_controllers.PlantsController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Objects;

public class CRUD {
    private static final Logger logger = LogManager.getLogger(CRUD.class);
    public static GridPane recordsGrid;
    public static Label errorMessage;
    public static Button addButton;

    public static void showCRUDScene() {
        // Load fxml file
        logger.info("Initializing CRUD scene...");

        VBox grid = (VBox) GUI.loadFXML("layouts/crud.fxml");
        GUI.getStage().setScene(new Scene(grid, 800, 600));
        recordsGrid = (GridPane) grid.lookup("#recordsGrid");
        errorMessage = (Label) grid.lookup("#errorMessage");
        errorMessage.setVisible(false);
        addButton = (Button) grid.lookup("#addButton");

        PlantsController.setPlantsRecordsList(recordsGrid);

        int numRows = recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;
        logger.info("Number of rows: " + numRows);
        GridPane.setRowIndex(errorMessage, numRows + 1);
        GridPane.setRowIndex(addButton, numRows + 2);
    }
}
