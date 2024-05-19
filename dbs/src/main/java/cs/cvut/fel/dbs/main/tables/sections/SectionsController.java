package cs.cvut.fel.dbs.main.tables.sections;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.*;
import cs.cvut.fel.dbs.main.CRUD;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;

public class SectionsController {
    private static final Logger logger = LogManager.getLogger(SectionsController.class);
    protected static boolean checkCoordinatesFormat(String coordinates) {
        // format example: '52.5200° N, 13.4051° E'
        if (!coordinates.matches("\\d{1,3}\\.\\d{4}° [NS], \\d{1,3}\\.\\d{4}° [EW]")) {
            logger.error("Invalid coordinates format");
            return false;
        }
        return true;
    }
    protected static boolean checkForm(SectionsFormController form, SectionsEntity section) {
        CRUD.showErrorMessage("");
        if (form.getSectionName().isEmpty()) {
            CRUD.showErrorMessage("Section name cannot be empty.");
            return false;
        }
        if (form.getGeographicalCoordinates().isEmpty()) {
            CRUD.showErrorMessage("Geographical coordinates cannot be empty.");
            return false;
        }
        if (!checkCoordinatesFormat(form.getGeographicalCoordinates())) {
            CRUD.showErrorMessage("Invalid coordinates format.");
            return false;
        }
        if (form.getTemperatureField().getText().isEmpty()) {
            CRUD.showErrorMessage("Temperature cannot be empty.");
            return false;
        }
        if (form.getLightField().getText().isEmpty()) {
            CRUD.showErrorMessage("Light cannot be empty.");
            return false;
        }
        // check if temperature and light values are numbers
        try {
            Integer.parseInt(form.getTemperatureField().getText());
        } catch (NumberFormatException e) {
            CRUD.showErrorMessage("Temperature must be a number.");
            return false;
        }
        try {
            Integer.parseInt(form.getLightField().getText());
        } catch (NumberFormatException e) {
            CRUD.showErrorMessage("Light must be a number.");
            return false;
        }
        if (!(form.getTemperature() >= 0 && form.getTemperature() <= 40)) {
            CRUD.showErrorMessage("Temperature must be between 0 and 40.");
            return false;
        }
        if (!(form.getLight() >= 0 && form.getLight() <= 100)) {
            CRUD.showErrorMessage("Light must be between 0 and 100.");
            return false;
        }
        if (SectionsDAO.selectedSoilType == null) {
            if (section == null || section.getSoilType() == null) {
                CRUD.showErrorMessage("No soil type selected.");
            }
        }
        if (SectionsDAO.selectedClimateType == null) {
            if (section == null || section.getClimateType() == null) {
                CRUD.showErrorMessage("No climate type selected.");
            }
        }
        if (SectionsDAO.selectedSectionManager == null) {
            if (section == null || section.getIsHandledBy() == null) {
                CRUD.showErrorMessage("No section manager selected.");
            }
        }
        return CRUD.isErrorMessageEmpty();
    }
    protected static void unselectSoilType() {
        SectionsDAO.selectedSoilType = null;
    }
    protected static void selectSoilType(SoiltypesEntity soilType) {
        SectionsDAO.selectedSoilType = soilType;
    }
    protected static void unselectClimateType() {
        SectionsDAO.selectedClimateType = null;
    }
    protected static void selectClimateType(ClimatetypesEntity climateType) {
        SectionsDAO.selectedClimateType = climateType;
    }
    protected static void unselectSectionManager() {
        SectionsDAO.selectedSectionManager = null;
    }
    protected static void selectSectionManager(EmployeesEntity sectionManager) {
        SectionsDAO.selectedSectionManager = sectionManager;
    }

    protected static void addNewSection() {
        if (!checkForm(SectionsDAO.sectionsFormController, null)) {
            return;
        }
        try {
            SectionsEntity section = new SectionsEntity();
            SectionsDAO.updateSection(section);

            if (SectionsDAO.selectedSoilType != null) {
                section.setSoilType(SectionsDAO.selectedSoilType);
            }
            if (SectionsDAO.selectedClimateType != null) {
                section.setClimateType(SectionsDAO.selectedClimateType);
            }
            if (SectionsDAO.selectedSectionManager != null) {
                section.setIsHandledBy(SectionsDAO.selectedSectionManager);
            }

            if (!SectionsDAO.checkSectionNameAndCoordinatesAreUnique(section)) {
                CRUD.showErrorMessage("Section with same name and coordinates already exists.");
                return;
            }

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(section);
            entityManager.getTransaction().commit();

            SectionsView.showSectionsRecordsList();
            SectionsView.showSectionInfo(section);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            SectionsView.showSectionsRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        SectionsDAO.clearAll();
    }
    protected static void editSection(SectionsEntity section) {
        if (!checkForm(SectionsDAO.sectionsFormController, section)) {
            return;
        }
        try {
            SectionsDAO.updateSection(section);

            if (SectionsDAO.selectedSoilType != null) {
                section.setSoilType(SectionsDAO.selectedSoilType);
            }
            if (SectionsDAO.selectedClimateType != null) {
                section.setClimateType(SectionsDAO.selectedClimateType);
            }
            if (SectionsDAO.selectedSectionManager != null) {
                section.setIsHandledBy(SectionsDAO.selectedSectionManager);
            }

            if (!SectionsDAO.checkSectionNameAndCoordinatesAreUnique(section)) {
                CRUD.showErrorMessage("Section with same name and coordinates already exists.");
                return;
            }

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(section);
            entityManager.getTransaction().commit();

            SectionsView.showSectionsRecordsList();
            SectionsView.showSectionInfo(section);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            SectionsView.showSectionsRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        SectionsDAO.clearAll();
    }
}
