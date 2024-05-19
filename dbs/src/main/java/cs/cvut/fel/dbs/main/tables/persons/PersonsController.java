package cs.cvut.fel.dbs.main.tables.persons;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.AddressesEntity;
import cs.cvut.fel.dbs.entities.PersonsEntity;
import cs.cvut.fel.dbs.main.CRUD;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import java.sql.Date;

public class PersonsController {
    private static final Logger logger = LogManager.getLogger(PersonsController.class);
    protected static boolean checkForm(PersonsFormController form) {
        CRUD.showErrorMessage("");
        if (form.getPersonFirstName().isEmpty()) {
            CRUD.showErrorMessage("First name cannot be empty.");
            return false;
        }
        if (form.getPersonLastName().isEmpty()) {
            CRUD.showErrorMessage("Last name cannot be empty.");
            return false;
        }
        if (form.getPersonBirthDate() == null) {
            CRUD.showErrorMessage("Birth date cannot be empty.");
            return false;
        }
        Date birthDate = form.getPersonBirthDate();
        Date currentDate = new Date(System.currentTimeMillis());
        if (birthDate.after(currentDate)) {
            CRUD.showErrorMessage("Birth date cannot be in the future.");
            return false;
        }
        if (currentDate.toLocalDate().getYear() - birthDate.toLocalDate().getYear() < 18) {
            CRUD.showErrorMessage("Person must be at least 18 years old.");
            return false;
        }
        return CRUD.isErrorMessageEmpty();
    }
    protected static void addNewPerson() {
        if (!checkForm(PersonsDAO.personsFormController)) {
            return;
        }
        try {
            PersonsEntity person = new PersonsEntity();
            PersonsDAO.updatePerson(person);

            if (!PersonsDAO.checkPersonIsUnique(person)) {
                CRUD.showErrorMessage("Person with this name and birth date already exists.");
                return;
            }
            if (PersonsDAO.selectedAddress == null) {
                CRUD.showErrorMessage("No address selected.");
                return;
            } else {
                person.setAddressId(PersonsDAO.selectedAddress);
            }

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(person);
            entityManager.getTransaction().commit();

            setPhoneNumbers(person);

            PersonsView.showPersonsRecordsList();
            PersonsView.showPersonInfo(person);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            PersonsView.showPersonsRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        PersonsDAO.clearAll();
    }
    protected static void editPerson(PersonsEntity person) {
        try {
            PersonsDAO.updatePerson(person);

            if (!checkForm(PersonsDAO.personsFormController)) {
                return;
            }

            if (!PersonsDAO.checkPersonIsUnique(person)) {
                CRUD.showErrorMessage("Person with this name and birth date already exists.");
                return;
            }

            if (PersonsDAO.selectedAddress != null) {
                person.setAddressId(PersonsDAO.selectedAddress);
            }

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(person);
            entityManager.getTransaction().commit();

            if (!PersonsDAO.phoneNumbers.isEmpty()) {
                setPhoneNumbers(person);
            }

            PersonsView.showPersonsRecordsList();
            PersonsView.showPersonInfo(person);
            return;
        } catch (RollbackException e) {
            logger.error("Transaction failed and has been rolled back: " + e.getMessage());
            PersonsView.showPersonsRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            PersonsView.showPersonsRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        PersonsDAO.clearAll();
    }
    protected static void unselectAddress() {
        PersonsDAO.selectedAddress = null;
    }
    protected static void selectAddress(AddressesEntity address) {
        PersonsDAO.selectedAddress = address;
    }
    protected static void addPhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            CRUD.showErrorMessage("Phone number cannot be empty.");
            return;
        }
        if (!PersonsDAO.checkPhoneNumber(phoneNumber)) {
            return;
        }
        if (PersonsDAO.phoneNumbers.contains(phoneNumber)) {
            CRUD.showErrorMessage("Phone number already added.");
            return;
        }
        PersonsDAO.phoneNumbers.add(phoneNumber);
        PersonsDAO.personsFormController.getPhoneNumbersLabel().setText("Phone numbers: " + PersonsDAO.phoneNumbers);
    }
    protected static void setPhoneNumbers(PersonsEntity person) {
        for (String phoneNumber : PersonsDAO.phoneNumbers) {
            if (PersonsDAO.phoneNumbersToDelete.contains(phoneNumber)) {
                continue;
            }
            person.setPhoneNumber(phoneNumber);
        }
    }
    protected static void deleteExistingPhoneNumber(String phoneNumber) {
        PersonsDAO.deletePhoneNumber(phoneNumber);
    }

    public static void deletePhoneNumber(String phoneNumber) {
        PersonsDAO.phoneNumbersToDelete.add(phoneNumber);
    }

    public static void cancelDeletePhoneNumber(String phoneNumber) {
        PersonsDAO.phoneNumbersToDelete.remove(phoneNumber);
    }
}
