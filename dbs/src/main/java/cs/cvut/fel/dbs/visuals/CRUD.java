package cs.cvut.fel.dbs.visuals;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    public static GridPane recordsFormGrid;
    public static Label noRecordsSelectedLabel;

    public static void showCRUDScene() {
        // Load fxml file
        logger.info("Initializing CRUD scene...");

        VBox grid = (VBox) GUI.loadFXML("layouts/crud.fxml");
        GUI.getStage().setScene(new Scene(grid, 800, 600));
        recordsGrid = (GridPane) grid.lookup("#recordsGrid");
        recordsFormGrid = (GridPane) grid.lookup("#recordsFormGrid");
        errorMessage = (Label) grid.lookup("#errorMessage");
        noRecordsSelectedLabel = (Label) grid.lookup("#noRecordsSelectedLabel");
        errorMessage.setVisible(false);
        addButton = (Button) grid.lookup("#addButton");
    }
    public void exit(ActionEvent actionEvent) {
        GUI.getStage().close();
    }
    public void mainMenu(ActionEvent actionEvent) {
        GUI.showMainMenu();
    }
    public static void addErrorMessageAndAddButton() {
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
