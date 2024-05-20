package cs.cvut.fel.dbs.main;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.main.special.ViewSections;
import cs.cvut.fel.dbs.main.tables.addresses.AddressesView;
import cs.cvut.fel.dbs.main.tables.climate_types.ClimateTypesView;
import cs.cvut.fel.dbs.main.tables.employees.EmployeesView;
import cs.cvut.fel.dbs.main.tables.instances.InstancesView;
import cs.cvut.fel.dbs.main.tables.persons.PersonsView;
import cs.cvut.fel.dbs.main.tables.plants.PlantsView;
import cs.cvut.fel.dbs.main.tables.researches.ResearchesView;
import cs.cvut.fel.dbs.main.tables.scientists.ScientistsView;
import cs.cvut.fel.dbs.main.tables.sections.SectionsView;
import cs.cvut.fel.dbs.main.tables.soil_types.SoilTypesView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Navigation {
    private static final Logger logger = LogManager.getLogger(Navigation.class);
    public void handleCrudPlantsButton() {
        logger.info("Loading Plants CRUD scene...");
        PlantsView.showPlantsRecordsList();
    }
    public void handleCrudInstancesButton() {
        logger.info("Loading Instances CRUD scene...");
        InstancesView.showInstancesRecordsList();
    }
    public void handleCrudAddressesButton() {
        logger.info("Loading Addresses CRUD scene...");
        AddressesView.showAddressesRecordsList();
    }
    public void exit() {
        logger.info("Exiting application...");
        DatabaseConnection.close();
        GUI.getStage().close();
    }
    public void mainMenu() {
        logger.info("Loading main menu...");
        GUI.showMainMenu();
    }

    public void handleCrudClimateTypesButton() {
        logger.info("Loading Climate Types CRUD scene...");
        ClimateTypesView.showClimateTypesRecordsList();
    }
    public void handleCrudSoilTypesButton() {
        logger.info("Loading Soil Types CRUD scene...");
        SoilTypesView.showSoilTypesRecordsList();
    }

    public void handleCrudPersonsButton() {
        logger.info("Loading Persons CRUD scene...");
        PersonsView.showPersonsRecordsList();
    }

    public void handleCrudSectionsButton() {
        logger.info("Loading Sections CRUD scene...");
        SectionsView.showSectionsRecordsList();
    }

    public void handleCrudScientistsButton() {
        logger.info("Loading Scientists CRUD scene...");
        ScientistsView.showScientistsRecordsList();
    }

    public void handleCrudResearchesButton() {
        logger.info("Loading Researches CRUD scene...");
        ResearchesView.showResearchesRecordsList();
    }

    public void handleCrudEmployeesButton() {
        logger.info("Loading Employees CRUD scene...");
        EmployeesView.showEmployeesRecordsList();
    }

    public void handleViewSectionsButton() {
        logger.info("Loading Sections view scene...");
        ViewSections.getSectionsViewPage();
    }
}
