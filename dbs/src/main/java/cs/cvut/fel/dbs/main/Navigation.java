package cs.cvut.fel.dbs.main;

import cs.cvut.fel.dbs.main.table_controllers.plants_controller.PlantsView;
import javafx.event.ActionEvent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Navigation {
    private static final Logger logger = LogManager.getLogger(Navigation.class);
    public void handleCrudPlantsButton() {
        // Load CRUD scene
        logger.info("Loading CRUD scene...");
        CRUD.showCRUDScene();
        PlantsView.showPlantsRecordsList();
        CRUD.addErrorMessageAndAddButton();
    }

    public void handleSelectButton() {
        // Load SELECT scene
        logger.info("Loading SELECT scene...");
    }
    public void exit(ActionEvent actionEvent) {
        GUI.getStage().close();
    }
    public void mainMenu(ActionEvent actionEvent) {
        GUI.showMainMenu();
    }
}
