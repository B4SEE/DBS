package cs.cvut.fel.dbs.main;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.main.tables.instances.InstancesView;
import cs.cvut.fel.dbs.main.tables.plants.PlantsView;
import javafx.event.ActionEvent;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Navigation {
    private static final Logger logger = LogManager.getLogger(Navigation.class);
    public void handleCrudPlantsButton() {
        // Load CRUD scene
        logger.info("Loading CRUD scene...");
        PlantsView.showPlantsRecordsList();
    }
    public void handleCrudInstancesButton() {
        // Load CRUD scene
        logger.info("Loading CRUD scene...");
        InstancesView.showInstancesRecordsList();
    }

    public void handleSelectButton() {
        // Load SELECT scene
        logger.info("Loading SELECT scene...");
    }
    public void exit(ActionEvent actionEvent) {
        DatabaseConnection.close();
        GUI.getStage().close();
    }
    public void mainMenu(ActionEvent actionEvent) {
        GUI.showMainMenu();
    }
}
