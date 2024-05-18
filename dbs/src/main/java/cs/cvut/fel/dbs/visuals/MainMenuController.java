package cs.cvut.fel.dbs.visuals;

import cs.cvut.fel.dbs.visuals.table_controllers.plants_controller.PlantsController;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MainMenuController {
    private static final Logger logger = LogManager.getLogger(MainMenuController.class);
    public static Button crudButton;
    public static Button selectButton;

    public void handleCrudButton(ActionEvent actionEvent) {
        // Load CRUD scene
        logger.info("Loading CRUD scene...");
        CRUD.showCRUDScene();
        PlantsController.setPlantsRecordsList(CRUD.recordsGrid);
        CRUD.addErrorMessageAndAddButton();
    }

    public void handleSelectButton(ActionEvent actionEvent) {
        // Load SELECT scene
        logger.info("Loading SELECT scene...");
    }

    public static Scene getMainMenuScene() {
        // Load fxml file
        logger.info("Initializing main menu...");
        Pane grid = GUI.loadFXML("layouts/mainMenu.fxml");
        crudButton = (Button) grid.lookup("#crudButton");
        selectButton = (Button) grid.lookup("#selectButton");
        return new Scene(grid, 800, 600);
    }
}
