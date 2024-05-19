package cs.cvut.fel.dbs.main.tables.soil_types;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.SoiltypesEntity;
import cs.cvut.fel.dbs.main.CRUD;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SoilTypesController {
    private static final Logger logger = LogManager.getLogger(SoilTypesController.class);
    protected static boolean checkForm(SoilTypesFormController form) {
        CRUD.showErrorMessage("");
        if (form.getSoilTypeNameField().getText().isEmpty()) {
            CRUD.showErrorMessage("Name cannot be empty.");
            return false;
        }
        // description can be empty
        return CRUD.isErrorMessageEmpty();
    }
    protected static void addNewSoilType() {
        if (!checkForm(SoilTypesDAO.soilTypesFormController)) {
            return;
        }
        try {
            SoiltypesEntity soilType = new SoiltypesEntity();
            SoilTypesDAO.updateSoilType(soilType);

            Statement statement = null;
            ResultSet result = null;
            // name should be unique
            try {
                String query = "SELECT * FROM soiltypes WHERE soil_name = '" + soilType.getSoilName() + "'";
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
            entityManager.persist(soilType);
            entityManager.getTransaction().commit();

            SoilTypesView.showSoilTypesRecordsList();
            SoilTypesView.showSoilTypeInfo(soilType);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            SoilTypesView.showSoilTypesRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        SoilTypesDAO.clearAll();
    }

    protected static void editSoilType(SoiltypesEntity soilType) {
        if (!checkForm(SoilTypesDAO.soilTypesFormController)) {
            return;
        }
        try {
            SoilTypesDAO.updateSoilType(soilType);

            Statement statement = null;
            ResultSet result = null;
            // name should be unique
            try {
                String query = "SELECT * FROM soiltypes WHERE soil_name = '" + soilType.getSoilName() + "' AND soiltypes.id_soil_type != " + soilType.getIdSoilType();
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
            entityManager.merge(soilType);
            entityManager.getTransaction().commit();

            SoilTypesView.showSoilTypesRecordsList();
            SoilTypesView.showSoilTypeInfo(soilType);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            SoilTypesView.showSoilTypesRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        SoilTypesDAO.clearAll();
    }
}
