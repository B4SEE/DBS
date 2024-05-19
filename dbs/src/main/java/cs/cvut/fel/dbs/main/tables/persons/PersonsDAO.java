package cs.cvut.fel.dbs.main.tables.persons;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.AddressesEntity;
import cs.cvut.fel.dbs.entities.PersonsEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.addresses.AddressesDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonsDAO {
    private static final Logger logger = LogManager.getLogger(PersonsDAO.class);
    protected static PersonsFormController personsFormController = new PersonsFormController();
    protected static AddressesEntity selectedAddress = null;
    protected static List<String> phoneNumbers = new ArrayList<>();
    protected static List<String> phoneNumbersToDelete = new ArrayList<>();
    public static List<PersonsEntity> getAllPersons() {
        List<PersonsEntity> persons = new ArrayList<>();
        try {
            String query = "SELECT * FROM persons";
            Statement statement = DatabaseConnection.getConnection().createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                PersonsEntity person = new PersonsEntity();
                setPersonInfo(result, person);
                persons.add(person);
            }
        } catch (Exception e) {
            logger.error("Error while getting all persons: " + e.getMessage());
        }
        return persons;
    }
    public static PersonsEntity getPerson(ResultSet personInfo) {
        logger.info("Getting person...");
        if (personInfo == null) {
            logger.error("Person info is null.");
            return null;
        }
        PersonsEntity person = new PersonsEntity();
        try {
            if (personInfo.next()) {
                logger.info("Person found.");
                setPersonInfo(personInfo, person);
            }
        } catch (Exception e) {
            logger.error("Error while getting person: " + e.getMessage());
        }
        return person;
    }
    private static void setPersonInfo(ResultSet personInfo, PersonsEntity person) {
        try {
            logger.info("Setting person info...");
            person.setIdPerson(personInfo.getInt("id_person"));
            person.setFirstName(personInfo.getString("first_name"));
            person.setLastName(personInfo.getString("last_name"));
            person.setDateOfBirth(personInfo.getDate("date_of_birth"));
            String addressQuery = "SELECT * FROM person_address WHERE id_address = " + personInfo.getInt("address_id");
            Statement addressStatement = DatabaseConnection.getConnection().createStatement();
            ResultSet addressResult = addressStatement.executeQuery(addressQuery);
            person.setAddressId(AddressesDAO.getAddress(addressResult));
            logger.info("Person info set: " + person.getFirstName() + " " + person.getLastName());
        } catch (Exception e) {
            logger.error("Error while setting person info: " + e.getMessage());
        }
    }
    protected static boolean checkPersonIsUnique(PersonsEntity person) {
        logger.info("Checking if person is unique...");
        EntityManager entityManager = DatabaseConnection.getEntityManager();
        List<PersonsEntity> persons = entityManager.createQuery("SELECT p FROM PersonsEntity p WHERE p.firstName = :firstName AND p.lastName = :lastName AND p.dateOfBirth = :dateOfBirth AND p.idPerson != :idPerson", PersonsEntity.class)
                .setParameter("firstName", person.getFirstName())
                .setParameter("lastName", person.getLastName())
                .setParameter("dateOfBirth", person.getDateOfBirth())
                .setParameter("idPerson", person.getIdPerson())
                .getResultList();

        return persons.isEmpty();
    }

    protected static boolean checkPhoneNumber(String phoneNumber) {
        // //check if phone number is unique and in the right format (phone_number VARCHAR(13) NULL PRIMARY KEY CHECK (phone_number ~ '^\+[0-9]{1,3}-[0-9]{3}-[0-9]{4}$'))
        EntityManager entityManager = DatabaseConnection.getEntityManager();
        List<String> phoneNumbers = entityManager.createQuery("SELECT p.phoneNumber FROM PhonenumbersEntity p WHERE p.phoneNumber = :phoneNumber", String.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();

        if (!phoneNumbers.isEmpty()) {
            CRUD.showErrorMessage("Phone number already exists.");
            return false;
        }
        // check if phone number is in the right format
        if (!phoneNumber.matches("^\\+[0-9]{1,3}-[0-9]{3}-[0-9]{4}$")) {
            CRUD.showErrorMessage("Phone number must be in the format +XXX-XXX-XXXX.");
            return false;
        }
        return true;
    }

    protected static void updatePerson(PersonsEntity person) {
        person.setFirstName(personsFormController.getPersonFirstName());
        person.setLastName(personsFormController.getPersonLastName());
        person.setDateOfBirth(personsFormController.getPersonBirthDate());
    }
    protected static void clearAll() {
        logger.info("ClearAll method was called by: " + Thread.currentThread().getStackTrace()[2].getMethodName());
        personsFormController.clearForm();
        personsFormController.resetFields();
        selectedAddress = null;
        phoneNumbers.clear();
    }

    public static void deletePerson(PersonsEntity person) {
        //check if person does not have children (employees, scientists)
        EntityManager entityManager = DatabaseConnection.getEntityManager();
        List<PersonsEntity> employees = entityManager.createQuery("SELECT e.person FROM EmployeesEntity e WHERE e.person = :person", PersonsEntity.class)
                .setParameter("person", person)
                .getResultList();
        List<PersonsEntity> scientists = entityManager.createQuery("SELECT s.personId FROM ScientistsEntity s WHERE s.personId = :person", PersonsEntity.class)
                .setParameter("person", person)
                .getResultList();
        if (!employees.isEmpty() || !scientists.isEmpty()) {
            CRUD.showErrorMessage("Cannot delete person that is referenced by employee or scientist.");
            return;
        }
        entityManager.getTransaction().begin();
        entityManager.remove(person);
        entityManager.getTransaction().commit();
        clearAll();
    }

    public static List<AddressesEntity> getListOfAvailableAddresses(PersonsEntity person) {
        StringBuilder query = new StringBuilder("SELECT * FROM addresses");
        if (person != null) {
            query.append(" WHERE id_address != ");
            if (selectedAddress != null) {
                query.append(selectedAddress.getIdAddress());
                logger.info("Query: " + query);
            } else {
                query.append(person.getAddressId().getIdAddress());
                logger.info("Query: " + query);
            }
        }
        query.append(" LIMIT ").append(AddressesDAO.recordsPerPage).append(" OFFSET ").append((AddressesDAO.page - 1) * AddressesDAO.recordsPerPage);
        List<AddressesEntity> availableAddresses = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query.toString());
            while (result.next()) {
                AddressesEntity address = new AddressesEntity();
                AddressesDAO.setAddressInfo(result, address);
                availableAddresses.add(address);
            }
        } catch (Exception e) {
            logger.error("Error while getting available addresses: " + e.getMessage());
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                logger.error("Failed to close statement: " + e.getMessage());
            }
        }

        return availableAddresses;
    }

    public static void deletePhoneNumber(String phoneNumber) {
        String query = "DELETE FROM phonenumbers WHERE phone_number = '" + phoneNumber + "'";
        Statement statement = null;
        try {
            statement = DatabaseConnection.getConnection().createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            logger.error("Error while deleting phone number: " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                logger.error("Failed to close statement: " + e.getMessage());
            }
        }
    }
}
