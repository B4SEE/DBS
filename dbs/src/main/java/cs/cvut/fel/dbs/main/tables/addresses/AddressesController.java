package cs.cvut.fel.dbs.main.tables.addresses;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.AddressesEntity;
import cs.cvut.fel.dbs.main.CRUD;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AddressesController {
    private static final Logger logger = LogManager.getLogger(AddressesController.class);
    protected static boolean checkForm(AddressesFormController form) {
        CRUD.showErrorMessage("");
        if (form.getAddressCityField().getText().isEmpty()) {
            CRUD.showErrorMessage("City cannot be empty.");
            return false;
        }
        if (form.getAddressStreetField().getText().isEmpty()) {
            CRUD.showErrorMessage("Street cannot be empty.");
            return false;
        }
        if (form.getAddressHouseNumberField().getText().isEmpty()) {
            CRUD.showErrorMessage("House number cannot be empty.");
            return false;
        }
        if (form.getAddressZipCodeField().getText().isEmpty()) {
            CRUD.showErrorMessage("Zip code cannot be empty.");
            return false;
        }
        // check if zip code contains only numbers
        try {
            for (char c : form.getAddressZipCodeField().getText().toCharArray()) {
                Integer.parseInt(String.valueOf(c));
            }
        } catch (NumberFormatException e) {
            CRUD.showErrorMessage("Zip code must contain only numbers.");
            return false;
        }
        Statement statement = null;
        ResultSet result = null;
        Statement statement2 = null;
        ResultSet result2 = null;
        // search for street name in the city (zip code should be the same for the same street in the same city)
        try {
            String query = "SELECT zip_code FROM addresses WHERE street = '" + form.getAddressStreetField().getText() + "' AND city = '" + form.getAddressCityField().getText() + "'";
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query);
            if (result.next()) {
                if (!result.getString("zip_code").equals(form.getAddressZipCodeField().getText())) {
                    CRUD.showErrorMessage("Same street in the same city should have the same zip code.");
                    return false;
                }
            }
            // if not false means that zip code should be unique
            String query2 = "SELECT * FROM addresses WHERE zip_code = '" + form.getAddressZipCodeField().getText() + "'";
            statement2 = DatabaseConnection.getConnection().createStatement();
            result2 = statement2.executeQuery(query2);
            if (result2.next()) {
                CRUD.showErrorMessage("Same zip code already exists.");
                return false;
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
            if (result2 != null) {
                try {
                    result2.close();
                } catch (Exception e) {
                    logger.error("Failed to close result set: " + e.getMessage());
                }
            }
            if (statement2 != null) {
                try {
                    statement2.close();
                } catch (Exception e) {
                    logger.error("Failed to close statement: " + e.getMessage());
                }
            }
        }
        return CRUD.isErrorMessageEmpty();
    }
    protected static void addNewAddress() {
        if (!checkForm(AddressesDAO.addressesFormController)) {
            return;
        }
        try {
            AddressesEntity address = new AddressesEntity();
            AddressesDAO.updateAddress(address);

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(address);
            entityManager.getTransaction().commit();

            AddressesView.showAddressesRecordsList();
            AddressesView.showAddressInfo(address);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            AddressesView.showAddressesRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        AddressesDAO.clearAll();
    }

    protected static void editAddress(AddressesEntity address) {
        if (!checkForm(AddressesDAO.addressesFormController)) {
            return;
        }
        try {
            AddressesDAO.updateAddress(address);

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(address);
            entityManager.getTransaction().commit();

            AddressesView.showAddressesRecordsList();
            AddressesView.showAddressInfo(address);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            AddressesView.showAddressesRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        AddressesDAO.clearAll();
    }
}
