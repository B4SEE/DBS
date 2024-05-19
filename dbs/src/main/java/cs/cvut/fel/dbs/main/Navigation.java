package cs.cvut.fel.dbs.main;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.main.tables.addresses.AddressesView;
import cs.cvut.fel.dbs.main.tables.climate_types.ClimateTypesView;
import cs.cvut.fel.dbs.main.tables.instances.InstancesView;
import cs.cvut.fel.dbs.main.tables.plants.PlantsView;
import cs.cvut.fel.dbs.main.tables.soil_types.SoilTypesView;
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
    public void handleCrudAddressesButton() {
        // Load CRUD scene
        logger.info("Loading CRUD scene...");
        AddressesView.showAddressesRecordsList();
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

    public void handleCrudClimateTypesButton(ActionEvent actionEvent) {
        // Load CRUD scene
        logger.info("Loading CRUD scene...");
        ClimateTypesView.showClimateTypesRecordsList();
    }
    public void handleCrudSoilTypesButton(ActionEvent actionEvent) {
        // Load CRUD scene
        logger.info("Loading CRUD scene...");
        SoilTypesView.showSoilTypesRecordsList();
    }
}
