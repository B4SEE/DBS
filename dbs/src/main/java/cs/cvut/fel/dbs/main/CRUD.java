package cs.cvut.fel.dbs.main;

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
    public static Button cancelButton;
    public static GridPane recordsFormGrid;
    public static Label noRecordsSelectedLabel;
    public static Label recordsGridTitle;
    public static Button nextPageButton;
    public static Button previousPageButton;

    public static void showCRUDScene() {
        logger.info("Initializing CRUD scene...");
        VBox grid = (VBox) GUI.loadFXML("layouts/crud.fxml");

        // Set false to allow resizing
        GUI.getStage().setMaximized(false);
        GUI.getStage().setScene(new Scene(grid));
        GUI.getStage().setMaximized(true);
        GUI.getStage().show();

        recordsGrid = (GridPane) grid.lookup("#recordsGrid");
        recordsGridTitle = (Label) grid.lookup("#recordsGridTitle");
        recordsFormGrid = (GridPane) grid.lookup("#recordsFormGrid");
        noRecordsSelectedLabel = (Label) grid.lookup("#noRecordsSelectedLabel");

        errorMessage = (Label) grid.lookup("#errorMessage");

        addButton = (Button) grid.lookup("#addButton");
        cancelButton = (Button) grid.lookup("#cancelButton");
        nextPageButton = (Button) grid.lookup("#nextPageButton");
        previousPageButton = (Button) grid.lookup("#previousPageButton");

        errorMessage.setVisible(false);
        nextPageButton.setVisible(false);
        previousPageButton.setVisible(false);
        cancelButton.setVisible(false);
    }
    public static void addErrorMessageAndAddButton() {
        int numRows = recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;

        GridPane.setRowIndex(errorMessage, numRows + 1);
        GridPane.setRowIndex(addButton, numRows + 2);
        GridPane.setRowIndex(cancelButton, numRows + 3);
        GridPane.setRowIndex(nextPageButton, numRows + 4);
        GridPane.setRowIndex(previousPageButton, numRows + 5);
    }
    public static void showErrorMessage(String message) {
        CRUD.errorMessage.setText(message);
        CRUD.errorMessage.setStyle("-fx-text-fill: red");
        CRUD.errorMessage.setVisible(true);
    }
    public static void showWarningMessage(String message) {
        CRUD.errorMessage.setText(message);
        CRUD.errorMessage.setStyle("-fx-text-fill: orange");
        CRUD.errorMessage.setVisible(true);
    }
    public static void showInfoMessage(String message) {
        CRUD.errorMessage.setText(message);
        CRUD.errorMessage.setStyle("-fx-text-fill: blue");
        CRUD.errorMessage.setVisible(true);
    }
    public static void showSuccessMessage(String message) {
        CRUD.errorMessage.setText(message);
        CRUD.errorMessage.setStyle("-fx-text-fill: green");
        CRUD.errorMessage.setVisible(true);
    }
    public static void clearMessage() {
        CRUD.errorMessage.setText("");
    }
    public static boolean isErrorMessageEmpty() {
        return CRUD.errorMessage.getText().isEmpty();
    }
}
