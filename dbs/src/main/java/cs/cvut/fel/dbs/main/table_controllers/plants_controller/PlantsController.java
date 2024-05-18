package cs.cvut.fel.dbs.main.table_controllers.plants_controller;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.ClimatetypesEntity;
import cs.cvut.fel.dbs.entities.PlantsEntity;
import cs.cvut.fel.dbs.entities.SoiltypesEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

public class PlantsController {
    private static final Logger logger = LogManager.getLogger(PlantsController.class);
    protected static boolean checkForm(PlantsFormController form, PlantsEntity plant) {
        PlantsView.showErrorMessage("");
        if (form.getPlantName().isEmpty()) {
            PlantsView.showErrorMessage("Plant name cannot be empty.");
            return false;
        }
        if (form.getPlantType().isEmpty()) {
            PlantsView.showErrorMessage("Plant type cannot be empty.");
            return false;
        }
        if (form.getMinTemperatureField().getText().isEmpty()) {
            PlantsView.showErrorMessage("Min temperature cannot be empty.");
            return false;
        }
        if (form.getMaxTemperatureField().getText().isEmpty()) {
            PlantsView.showErrorMessage("Max temperature cannot be empty.");
            return false;
        }
        if (form.getMinLightField().getText().isEmpty()) {
            PlantsView.showErrorMessage("Min light cannot be empty.");
            return false;
        }
        if (form.getMaxLightField().getText().isEmpty()) {
            PlantsView.showErrorMessage("Max light cannot be empty.");
            return false;
        }
        // check if temperature and light values are numbers
        try {
            Integer.parseInt(form.getMinTemperatureField().getText());
        } catch (NumberFormatException e) {
            PlantsView.showErrorMessage("Min temperature must be a number.");
            return false;
        }
        try {
            Integer.parseInt(form.getMaxTemperatureField().getText());
        } catch (NumberFormatException e) {
            PlantsView.showErrorMessage("Max temperature must be a number.");
            return false;
        }
        try {
            Integer.parseInt(form.getMinLightField().getText());
        } catch (NumberFormatException e) {
            PlantsView.showErrorMessage("Min light must be a number.");
            return false;
        }
        try {
            Integer.parseInt(form.getMaxLightField().getText());
        } catch (NumberFormatException e) {
            PlantsView.showErrorMessage("Max light must be a number.");
            return false;
        }
        if (!(form.getMinTemperature() >= 0 && form.getMinTemperature() <= 40)) {
            PlantsView.showErrorMessage("Temperature must be between 0 and 40.");
        }
        if (!(form.getMaxTemperature() >= 0 && form.getMaxTemperature() <= 40)) {
            PlantsView.showErrorMessage("Temperature must be between 0 and 40.");
        }
        if (form.getMinTemperature() > form.getMaxTemperature()) {
            PlantsView.showErrorMessage("Min temperature cannot be higher than max temperature.");
        }
        if (!(form.getMinLight() >= 0 || form.getMinLight() <= 100)) {
            PlantsView.showErrorMessage("Light must be between 0 and 100.");
        }
        if (!(form.getMaxLight() >= 0 || form.getMaxLight() <= 100)) {
            PlantsView.showErrorMessage("Light must be between 0 and 100.");
        }
        if (form.getMinLight() > form.getMaxLight()) {
            PlantsView.showErrorMessage("Min light cannot be higher than max light.");
        }
        if (PlantsDAO.selectedSoilTypes.isEmpty()) {
            if (plant == null || plant.getPreferredSoilTypes().isEmpty()) {
                PlantsView.showErrorMessage("No preferred soil types selected.");
            }
        }
        if (PlantsDAO.selectedClimateTypes.isEmpty()) {
            if (plant == null || plant.getPreferredClimateTypes().isEmpty()) {
                PlantsView.showErrorMessage("No preferred climate types selected.");
            }
        }
        return PlantsView.isErrorMessageEmpty();
    }
    protected static void deletePreferredSoilType(SoiltypesEntity soilType) {
        PlantsDAO.soilTypesToDelete.add(soilType);
    }
    protected static void cancelDeletePreferredSoilType(SoiltypesEntity soilType) {
        PlantsDAO.soilTypesToDelete.remove(soilType);
    }
    protected static void deletePreferredClimateType(ClimatetypesEntity climateType) {
        PlantsDAO.climateTypesToDelete.add(climateType);
    }
    protected static void cancelDeletePreferredClimateType(ClimatetypesEntity climateType) {
        PlantsDAO.climateTypesToDelete.remove(climateType);
    }
    protected static void addNewPlant() {
        if (!PlantsController.checkForm(PlantsDAO.plantsFormController, null)) {
            return;
        }
        if (!PlantsDAO.checkPlantNameIsUnique(new PlantsEntity())) {
            PlantsView.showErrorMessage("Plant with this name already exists.");
            return;
        }
        try {
            PlantsEntity plant = new PlantsEntity();
            PlantsDAO.updatePlant(plant);

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(plant);
            entityManager.getTransaction().commit();

            setSoilAndClimateTypes(plant);

            PlantsView.showPlantsRecordsList();
            PlantsView.showPlantInfo(plant);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            PlantsView.showPlantsRecordsList();
            PlantsView.showErrorMessage("Failed: " + e.getMessage());
        }
        PlantsDAO.clearAll();
    }
    protected static void editPlant(PlantsEntity plant) {
        try {
            PlantsDAO.updatePlant(plant);
            if (!PlantsController.checkForm(PlantsDAO.plantsFormController, plant)) {
                return;
            }
            if (!PlantsDAO.checkPlantNameIsUnique(new PlantsEntity())) {
                PlantsView.showErrorMessage("Plant with this name already exists.");
                return;
            }
            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(plant);
            entityManager.getTransaction().commit();

            setSoilAndClimateTypes(plant);

            PlantsView.showPlantsRecordsList();
            PlantsView.showPlantInfo(plant);
            return;
        } catch (RollbackException e) {
            logger.error("Transaction failed and has been rolled back: " + e.getMessage());
            PlantsView.showPlantsRecordsList();
            PlantsView.showErrorMessage("Failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            PlantsView.showPlantsRecordsList();
            PlantsView.showErrorMessage("Failed: " + e.getMessage());
        }
        PlantsDAO.clearAll();
    }

    private static void setSoilAndClimateTypes(PlantsEntity plant) {
        for (SoiltypesEntity soilType : PlantsDAO.selectedSoilTypes) {
            if (PlantsDAO.soilTypesToDelete.contains(soilType)) {
                continue;
            }
            if (!plant.getPreferredSoilTypes().contains(soilType)) {
                plant.addPreferredSoilType(soilType);
            }
        }
        for (ClimatetypesEntity climateType : PlantsDAO.selectedClimateTypes) {
            if (PlantsDAO.climateTypesToDelete.contains(climateType)) {
                continue;
            }
            if (!plant.getPreferredClimateTypes().contains(climateType)) {
                plant.addPreferredClimateType(climateType);
            }
        }
        for (SoiltypesEntity soilType : PlantsDAO.soilTypesToDelete) {
            PlantsDAO.deletePreferredSoilType(plant, soilType);
            PlantsView.showPlantEditForm(plant);
        }
        for (ClimatetypesEntity climateType : PlantsDAO.climateTypesToDelete) {
            PlantsDAO.deletePreferredClimateType(plant, climateType);
            PlantsView.showPlantEditForm(plant);
        }
    }
    protected static void selectSoilType(SoiltypesEntity soilType) {
        if (!PlantsDAO.selectedSoilTypes.contains(soilType)) {
            PlantsDAO.selectedSoilTypes.add(soilType);
        }
    }
    protected static void unselectSoilType(SoiltypesEntity soilType) {
        PlantsDAO.selectedSoilTypes.remove(soilType);
    }
    protected static void selectClimateType(ClimatetypesEntity climateType) {
        if (!PlantsDAO.selectedClimateTypes.contains(climateType)) {
            PlantsDAO.selectedClimateTypes.add(climateType);
        }
    }
    protected static void unselectClimateType(ClimatetypesEntity climateType) {
        PlantsDAO.selectedClimateTypes.remove(climateType);
    }
}
