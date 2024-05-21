package cs.cvut.fel.dbs.main.tables.plants;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.ClimatetypesEntity;
import cs.cvut.fel.dbs.entities.PlantsEntity;
import cs.cvut.fel.dbs.entities.SoiltypesEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.climate_types.ClimateTypesDAO;
import cs.cvut.fel.dbs.main.tables.soil_types.SoilTypesDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlantsDAO {
    private static final Logger logger = LogManager.getLogger(PlantsDAO.class);
    protected static List<SoiltypesEntity> selectedSoilTypes = new ArrayList<>();
    protected static List<ClimatetypesEntity> selectedClimateTypes = new ArrayList<>();
    protected static List<SoiltypesEntity> soilTypesToDelete = new ArrayList<>();
    protected static List<ClimatetypesEntity> climateTypesToDelete = new ArrayList<>();
    protected static PlantsFormController plantsFormController = new PlantsFormController();
    public static List<PlantsEntity> getAllPlants() {
        List<PlantsEntity> plants = new ArrayList<>();
        try {
            String query = "SELECT * FROM plants";
            Statement statement = DatabaseConnection.getConnection().createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                PlantsEntity plant = new PlantsEntity();
                setPlantInfo(result, plant);
                plants.add(plant);
            }
        } catch (SQLException e) {
            logger.error("Failed to load plants: " + e.getMessage());
        }
        return plants;
    }
    public static PlantsEntity getPlant(ResultSet plantInfo) {
        if (plantInfo == null) {
            return null;
        }
        PlantsEntity plant = new PlantsEntity();
        try {
            if (plantInfo.next()) {
                setPlantInfo(plantInfo, plant);
            }
        } catch (SQLException e) {
            logger.error("Failed to get plant: " + e.getMessage());
        }
        return plant;
    }

    private static void setPlantInfo(ResultSet plantInfo, PlantsEntity plant) throws SQLException {
        plant.setIdPlant(plantInfo.getInt("id_plant"));
        plant.setPlantName(plantInfo.getString("plant_name"));
        plant.setPlantType(plantInfo.getString("plant_type"));
        plant.setMaxTemperature(plantInfo.getInt("max_temperature"));
        plant.setMinTemperature(plantInfo.getInt("min_temperature"));
        plant.setMaxLight(plantInfo.getInt("max_light"));
        plant.setMinLight(plantInfo.getInt("min_light"));
    }

    protected static void clearAll() {
        selectedSoilTypes.clear();
        selectedClimateTypes.clear();
        soilTypesToDelete.clear();
        climateTypesToDelete.clear();
        plantsFormController.clearForm();
        plantsFormController.resetFields();
        CRUD.errorMessage.setVisible(false);
    }
    protected static void deletePlant(PlantsEntity plant) {
        PlantsDAO.clearAll();
        EntityManager entityManager = DatabaseConnection.getEntityManager();
        entityManager.getTransaction().begin();

        PlantsEntity plantInDb = entityManager.find(PlantsEntity.class, plant.getIdPlant());

        String query = "SELECT * FROM instances WHERE plant = " + plantInDb.getIdPlant();
        ResultSet result;

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            statement.executeQuery(query);
            result = statement.getResultSet();
            logger.info("Result: " + result);
        } catch (Exception e) {
            logger.error("Error while getting instances for plant with id " + plantInDb.getIdPlant() + ":" + e.getMessage());
            return;
        }

        try {
            if (result.next()) {
                PlantsView.showPlantsRecordsList();
                CRUD.showErrorMessage("Failed to delete plant: There are associated instances with this plant.");
                logger.error("Failed to delete plant: There are associated instances with this plant.");
                return;
            }
        } catch (SQLException e) {
            logger.error("Failed to delete plant: " + e.getMessage());
            return;
        }

        entityManager.remove(plantInDb);
        entityManager.getTransaction().commit();
        PlantsView.showPlantsRecordsList();
        clearAll();
        logger.info("Plant deleted successfully.");
    }
    protected static void deletePreferredSoilType(PlantsEntity plant, SoiltypesEntity soilType) {
        if (plant.getPreferredSoilTypes().size() == 1) {
            CRUD.showErrorMessage("Failed: Plant must have at least one preferred soil type.");
            logger.error("Failed: Plant must have at least one preferred soil type.");
            return;
        }
        if (plant.getPreferredSoilTypes().contains(soilType)) {
            plant.deletePreferredSoilType(soilType);
        }
    }
    protected static void deletePreferredClimateType(PlantsEntity plant, ClimatetypesEntity climateType) {
        if (plant.getPreferredClimateTypes().size() == 1) {
            CRUD.showErrorMessage("Failed: Plant must have at least one preferred climate type.");
            logger.error("Failed: Plant must have at least one preferred climate type.");
            return;
        }
        if (plant.getPreferredClimateTypes().contains(climateType)) {
            plant.deletePreferredClimateType(climateType);
        }
    }
    protected static void updatePlant(PlantsEntity plant) {
        plant.setPlantName(plantsFormController.getPlantName());
        plant.setPlantType(plantsFormController.getPlantType());
        plant.setMinTemperature(plantsFormController.getMinTemperature());
        plant.setMaxTemperature(plantsFormController.getMaxTemperature());
        plant.setMinLight(plantsFormController.getMinLight());
        plant.setMaxLight(plantsFormController.getMaxLight());
    }
    protected static List<SoiltypesEntity> getListOfAvailableSoilTypes(PlantsEntity plant) {
        StringBuilder query = new StringBuilder("SELECT * FROM soiltypes");
        boolean hasPlant = plant != null;
        boolean hasSelectedSoilTypes = !PlantsDAO.selectedSoilTypes.isEmpty();

        if (hasPlant || hasSelectedSoilTypes) {
            query.append(" WHERE id_soil_type NOT IN (");

            if (hasPlant) {
                query.append("SELECT id_soil_type FROM plantspreferredsoiltypes WHERE id_plant = ").append(plant.getIdPlant());
            }

            if (hasSelectedSoilTypes) {
                if (hasPlant) {
                    query.append(" UNION ALL ");
                }
                query.append("SELECT id_soil_type FROM soiltypes WHERE ");
                for (int i = 0; i < PlantsDAO.selectedSoilTypes.size(); i++) {
                    if (i > 0) {
                        query.append(" OR ");
                    }
                    query.append("id_soil_type = ").append(PlantsDAO.selectedSoilTypes.get(i).getIdSoilType());
                }
            }
            query.append(")");
        }

        List<SoiltypesEntity> availableSoilTypes = new ArrayList<>();

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            statement.executeQuery(query.toString());
            ResultSet result = statement.getResultSet();
            while (!result.isAfterLast()) {
                availableSoilTypes.add(SoilTypesDAO.getSoilType(result));
            }
        } catch (Exception e) {
            logger.error("Failed to load soil types: " + e.getMessage());
        }

        return availableSoilTypes;
    }
    protected static List<ClimatetypesEntity> getListOfAvailableClimateTypes(PlantsEntity plant) {
        StringBuilder query = new StringBuilder("SELECT * FROM climatetypes");
        boolean hasPlant = plant != null;
        boolean hasSelectedClimateTypes = !PlantsDAO.selectedClimateTypes.isEmpty();

        if (hasPlant || hasSelectedClimateTypes) {
            query.append(" WHERE id_climate_type NOT IN (");

            if (hasPlant) {
                query.append("SELECT id_climate_type FROM plantspreferredclimatetypes WHERE id_plant = ").append(plant.getIdPlant());
            }

            if (hasSelectedClimateTypes) {
                if (hasPlant) {
                    query.append(" UNION ALL ");
                }
                query.append("SELECT id_climate_type FROM climatetypes WHERE ");
                for (int i = 0; i < PlantsDAO.selectedClimateTypes.size(); i++) {
                    if (i > 0) {
                        query.append(" OR ");
                    }
                    query.append("id_climate_type = ").append(PlantsDAO.selectedClimateTypes.get(i).getIdClimateType());
                }
            }
            query.append(")");
        }

        List<ClimatetypesEntity> availableClimateTypes = new ArrayList<>();

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            statement.executeQuery(query.toString());
            ResultSet result = statement.getResultSet();
            while (!result.isAfterLast()) {
                availableClimateTypes.add(ClimateTypesDAO.getClimateType(result));
            }
        } catch (Exception e) {
            logger.error("Failed to load climate types: " + e.getMessage());
        }

        return availableClimateTypes;
    }
    protected static boolean checkPlantNameIsUnique(PlantsEntity plant) {
        // check that plant name is unique
        EntityManager entityManager = DatabaseConnection.getEntityManager();
        List<PlantsEntity> plants = entityManager.createQuery("SELECT p FROM PlantsEntity p WHERE p.plantName = :plantName AND p.idPlant != :idPlant", PlantsEntity.class)
                .setParameter("plantName", plant.getPlantName())
                .setParameter("idPlant", plant.getIdPlant())
                .getResultList();
        return plants.isEmpty();
    }
}
