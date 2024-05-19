package cs.cvut.fel.dbs.main.tables.addresses;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.AddressesEntity;
import cs.cvut.fel.dbs.main.CRUD;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressesDAO {
    private static final Logger logger = LogManager.getLogger(AddressesDAO.class);
    protected static AddressesFormController addressesFormController = new AddressesFormController();
    public static int page = 1;
    public static int recordsPerPage = 15;
    public static List<AddressesEntity> getAllAddresses() {
        List<AddressesEntity> addresses = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;
        try {
            logger.info("Getting all addresses...");
            String query = "SELECT * FROM addresses LIMIT " + recordsPerPage + " OFFSET " + (page - 1) * recordsPerPage;
            logger.info("Query: " + query);
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query);
            while (result.next()) {
                logger.info("Getting address...");
                AddressesEntity address = new AddressesEntity();
                setAddressInfo(result, address);
                addresses.add(address);
            }
        } catch (Exception e) {
            logger.error("Error while getting all addresses: " + e.getMessage());
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
        return addresses;
    }
    public static AddressesEntity getAddress(ResultSet addressInfo) {
        AddressesEntity address = new AddressesEntity();
        try {
            if (addressInfo.next()) {
                setAddressInfo(addressInfo, address);
            }
        } catch (Exception e) {
            logger.error("Error while getting address: " + e.getMessage());
        }
        return address;
    }
    private static void setAddressInfo(ResultSet addressInfo, AddressesEntity address) {
        try {
            logger.info("Setting address info...");
            address.setIdAddress(addressInfo.getInt("id_address"));
            address.setHouseNumber(addressInfo.getString("house_number"));
            address.setStreet(addressInfo.getString("street"));
            address.setCity(addressInfo.getString("city"));
            address.setZipCode(addressInfo.getString("zip_code"));
            logger.info("Address info: " + address.getCity() + " " + address.getStreet() + " " + address.getHouseNumber() + " " + address.getZipCode());
        } catch (Exception e) {
            logger.error("Error while setting address info: " + e.getMessage());
        }
    }
    protected static void clearAll() {
        addressesFormController.clearForm();
        addressesFormController.resetFields();
        CRUD.errorMessage.setVisible(false);
    }
    protected static void updateAddress(AddressesEntity address) {
        address.setCity(addressesFormController.getAddressCity());
        address.setStreet(addressesFormController.getAddressStreet());
        address.setHouseNumber(addressesFormController.getAddressHouseNumber());
        address.setZipCode(addressesFormController.getAddressZipCode());
    }
    protected static void deleteAddress(AddressesEntity address) {
        AddressesDAO.clearAll();
        Statement statement = null;
        ResultSet result = null;
        try {
            String query = "SELECT * FROM persons WHERE address_id = " + address.getIdAddress();
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query);
            if (result.next()) {
                CRUD.showErrorMessage("Address is used by a person.");
                return;
            }

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            AddressesEntity addressInDb = entityManager.find(AddressesEntity.class, address.getIdAddress());
            entityManager.remove(addressInDb);
            entityManager.getTransaction().commit();
            logger.info("Plant deleted successfully.");
            AddressesView.showAddressesRecordsList();
        } catch (Exception e) {
            logger.error("Error while deleting address: " + e.getMessage());
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
