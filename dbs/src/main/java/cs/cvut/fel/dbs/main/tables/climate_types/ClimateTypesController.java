package cs.cvut.fel.dbs.main.tables.climate_types;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.ClimatetypesEntity;
import cs.cvut.fel.dbs.main.CRUD;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ClimateTypesController {
    private static final Logger logger = LogManager.getLogger(ClimateTypesController.class);
    protected static boolean checkForm(ClimateTypesFormController form) {
        CRUD.showErrorMessage("");
        if (form.getClimateTypeNameField().getText().isEmpty()) {
            CRUD.showErrorMessage("Name cannot be empty.");
            return false;
        }
        if (form.getClimateTypeDescriptionField().getText().isEmpty()) {
            CRUD.showErrorMessage("Description cannot be empty.");
            return false;
        }
        return CRUD.isErrorMessageEmpty();
    }
    protected static void addNewAddress() {
        if (!checkForm(ClimateTypesDAO.climateTypesFormController)) {
            return;
        }
        try {
            ClimatetypesEntity climateType = new ClimatetypesEntity();
            ClimateTypesDAO.updateClimateType(climateType);

            Statement statement = null;
            ResultSet result = null;
            // name should be unique
            try {
                String query = "SELECT * FROM climatetypes WHERE climate_name = '" + climateType.getClimateName() + "'";
                statement = DatabaseConnection.getConnection().createStatement();
                result = statement.executeQuery(query);
                if (result.next()) {
                    CRUD.showErrorMessage("Name should be unique.");
                    return;
                }
            } catch (Exception e) {
                logger.error("Failed: " + e.getMessage());
                CRUD.showErrorMessage("Failed: " + e.getMessage());
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

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(climateType);
            entityManager.getTransaction().commit();

            ClimateTypesView.showClimateTypesRecordsList();
            ClimateTypesView.showClimateTypeInfo(climateType);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            ClimateTypesView.showClimateTypesRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        ClimateTypesDAO.clearAll();
    }

    protected static void editAddress(ClimatetypesEntity climateType) {
        if (!checkForm(ClimateTypesDAO.climateTypesFormController)) {
            return;
        }
        try {
            ClimateTypesDAO.updateClimateType(climateType);

            Statement statement = null;
            ResultSet result = null;
            // name should be unique
            try {
                String query = "SELECT * FROM climatetypes WHERE climate_name = '" + climateType.getClimateName() + "' AND id_climate_type != " + climateType.getIdClimateType();
                statement = DatabaseConnection.getConnection().createStatement();
                result = statement.executeQuery(query);
                if (result.next()) {
                    CRUD.showErrorMessage("Name should be unique.");
                    return;
                }
            } catch (Exception e) {
                logger.error("Failed: " + e.getMessage());
                CRUD.showErrorMessage("Failed: " + e.getMessage());
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

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(climateType);
            entityManager.getTransaction().commit();

            ClimateTypesView.showClimateTypesRecordsList();
            ClimateTypesView.showClimateTypeInfo(climateType);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            ClimateTypesView.showClimateTypesRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        ClimateTypesDAO.clearAll();
    }
}
