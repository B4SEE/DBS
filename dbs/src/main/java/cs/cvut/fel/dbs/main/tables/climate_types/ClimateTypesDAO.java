package cs.cvut.fel.dbs.main.tables.climate_types;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.ClimatetypesEntity;
import cs.cvut.fel.dbs.main.CRUD;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClimateTypesDAO {
    private static final Logger logger = LogManager.getLogger(ClimateTypesDAO.class);
    protected static ClimateTypesFormController climateTypesFormController = new ClimateTypesFormController();

    public static List<ClimatetypesEntity> getAllClimateTypes() {
        List<ClimatetypesEntity> climateTypes = new ArrayList<>();
        try {
            String query = "SELECT * FROM climatetypes";
            Statement statement = DatabaseConnection.getConnection().createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                ClimatetypesEntity climateType = new ClimatetypesEntity();
                setClimateTypeInfo(result, climateType);
                climateTypes.add(climateType);
            }
        } catch (Exception e) {
            logger.error("Error while getting all climate types: " + e.getMessage());
        }
        return climateTypes;
    }
    public static ClimatetypesEntity getClimateType(ResultSet climateTypeInfo) {
        if (climateTypeInfo == null) {
            return null;
        }
        ClimatetypesEntity climateType = new ClimatetypesEntity();
        try {
            if (climateTypeInfo.next()) {
                setClimateTypeInfo(climateTypeInfo, climateType);
            }
        } catch (Exception e) {
            logger.error("Error while getting climate type: " + e.getMessage());
        }
        return climateType;
    }
    private static void setClimateTypeInfo(ResultSet climateTypeInfo, ClimatetypesEntity climateType) throws SQLException {
        climateType.setIdClimateType(climateTypeInfo.getInt("id_climate_type"));
        climateType.setClimateName(climateTypeInfo.getString("climate_name"));
        climateType.setClimateDescription(climateTypeInfo.getString("climate_description"));
    }
    protected static void clearAll() {
        climateTypesFormController.clearForm();
        climateTypesFormController.resetFields();
        CRUD.errorMessage.setVisible(false);
    }
    protected static void updateClimateType(ClimatetypesEntity climateType) {
        climateType.setClimateName(climateTypesFormController.getClimateTypeName());
        climateType.setClimateDescription(climateTypesFormController.getClimateTypeDescription());
    }
    protected static void deleteClimateType(ClimatetypesEntity climateType) {
        ClimateTypesDAO.clearAll();
        Statement statement = null;
        ResultSet result = null;
        try {
            String query = "SELECT * FROM plantspreferredclimatetypes WHERE id_climate_type = " + climateType.getIdClimateType();
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query);
            if (result.next()) {
                CRUD.showErrorMessage("Cannot delete climate type that is used in plant.");
                return;
            }

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            ClimatetypesEntity climateTypeInDb = entityManager.find(ClimatetypesEntity.class, climateType.getIdClimateType());
            entityManager.remove(climateTypeInDb);
            entityManager.getTransaction().commit();

            ClimateTypesView.showClimateTypesRecordsList();
            clearAll();
            logger.info("Climate type deleted successfully.");
        } catch (Exception e) {
            logger.error("Error while deleting address: " + e.getMessage());
            ClimateTypesView.showClimateTypesRecordsList();
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (Exception e) {
                    logger.error("Failed to close result set: " + e.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    logger.error("Failed to close statement: " + e.getMessage());
                }
            }
        }
    }
}
