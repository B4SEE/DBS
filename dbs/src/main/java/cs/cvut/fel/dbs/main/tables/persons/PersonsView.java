package cs.cvut.fel.dbs.main.tables.persons;

import cs.cvut.fel.dbs.entities.AddressesEntity;
import cs.cvut.fel.dbs.entities.PersonsEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.RecordsController;
import cs.cvut.fel.dbs.main.tables.addresses.AddressesDAO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;

public class PersonsView {
    private static final Logger logger = LogManager.getLogger(PersonsView.class);
    public static void showPersonsRecordsList() {
        CRUD.showCRUDScene();
        CRUD.recordsGridTitle.setText("Persons");

        logger.info("Initializing persons records list...");

        int count = CRUD.recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;

        for (PersonsEntity person : PersonsDAO.getAllPersons()) {
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(person.getFirstName() + " " + person.getLastName());
            GridPane.setRowIndex(recordGrid, count++);
            recordsController.infoButton.setOnAction(event -> {
                PersonsDAO.clearAll();
                showPersonInfo(person);
            });
            recordsController.editButton.setOnAction(event -> {
                PersonsDAO.clearAll();
                showPersonEditForm(person);
            });
            recordsController.deleteButton.setOnAction(event -> {
                PersonsDAO.deletePerson(person);
                showPersonsRecordsList();
            });
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }
        CRUD.addErrorMessageAndAddButton();
        CRUD.addButton.setOnAction(event -> {
            PersonsDAO.clearAll();
            showEmptyForm();
        });
    }
    public static void showPersonInfo(PersonsEntity person) {
        PersonsDAO.clearAll();
        CRUD.recordsFormGrid.getChildren().clear();

        PersonsDAO.personsFormController.fillFormFields(person);

        PersonsDAO.personsFormController.getPersonFirstNameField().setEditable(false);
        PersonsDAO.personsFormController.getPersonLastNameField().setEditable(false);
        PersonsDAO.personsFormController.getPersonBirthDateField().getEditor().setEditable(false);

        Label formTitle = new Label("Person Info");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, PersonsDAO.personsFormController, 1);

        CRUD.recordsFormGrid.add(PersonsDAO.personsFormController.getAddressLabel(), 0, count++);
        Label personAddress = new Label(person.getAddressId().getCity() + ", " + person.getAddressId().getStreet() + " " + person.getAddressId().getHouseNumber());
        CRUD.recordsFormGrid.add(personAddress, 0, count++);

        CRUD.recordsFormGrid.add(PersonsDAO.personsFormController.getPhoneNumbersLabel(), 0, count++);

        for (String phoneNumber : person.getPhoneNumbersList()) {
            Label phoneNumberLabel = new Label(phoneNumber);
            CRUD.recordsFormGrid.add(phoneNumberLabel, 0, count++);
        }
    }
    public static void showPersonEditForm(PersonsEntity person) {
        CRUD.recordsFormGrid.getChildren().clear();

        PersonsDAO.personsFormController.fillFormFields(person);

        Label formTitle = new Label("Edit Instance");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, PersonsDAO.personsFormController, 1);

        CRUD.recordsFormGrid.add(PersonsDAO.personsFormController.getAddressLabel(), 0, count++);
        Label personAddress = new Label(person.getAddressId().getCity() + ", " + person.getAddressId().getStreet() + " " + person.getAddressId().getHouseNumber());
        if (PersonsDAO.selectedAddress != null) {
            personAddress.setText(PersonsDAO.selectedAddress.getCity() + ", " + PersonsDAO.selectedAddress.getStreet() + " " + PersonsDAO.selectedAddress.getHouseNumber());
        }
        CRUD.recordsFormGrid.add(personAddress, 0, count++);

        Button changeAddressButton = new Button("Change Address");
        changeAddressButton.setOnAction(event -> showAvailableAddresses(person, true, PersonsDAO.getListOfAvailableAddresses(person)));
        CRUD.recordsFormGrid.add(changeAddressButton, 1, count++);

        CRUD.recordsFormGrid.add(PersonsDAO.personsFormController.getPhoneNumbersLabel(), 0, count++);

        List<String> phoneNumbers = person.getPhoneNumbersList();
        if (!PersonsDAO.phoneNumbers.isEmpty()) {
            phoneNumbers.addAll(PersonsDAO.phoneNumbers);
        }

        for (String phoneNumber : phoneNumbers) {
            Label phoneNumberLabel = new Label(phoneNumber);
            Button deletePhoneNumberButton = getDeletePhoneNumberButton(phoneNumber, phoneNumberLabel);
            CRUD.recordsFormGrid.add(phoneNumberLabel, 0, count++);
            CRUD.recordsFormGrid.add(deletePhoneNumberButton, 1, count++);
        }

        Button addPhoneNumberButton = new Button("Add Phone Number");
        addPhoneNumberButton.setOnAction(event -> showAddPhoneNumberForm(person, true));
        CRUD.recordsFormGrid.add(addPhoneNumberButton, 0, count++);

        Button submitButton = getSubmitEditFormButton(person);
        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(submitButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }

    private static void showAddPhoneNumberForm(PersonsEntity person, boolean isEdit) {
        CRUD.recordsGrid.getChildren().clear();
        CRUD.showCRUDScene();
        if (person != null) {
            showPersonEditForm(person);
        } else {
            showEmptyForm();
        }

        int count = CRUD.recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;

        Label formTitle = new Label("Add Phone Number. Use format +XXX-XXX-XXXX");
        TextField phoneNumberField = new TextField();
        CRUD.recordsGrid.add(formTitle, 0, count++);
        CRUD.recordsGrid.add(new Label("Phone Number:"), 0, count);
        CRUD.recordsGrid.add(phoneNumberField, 1, count);

        CRUD.addErrorMessageAndAddButton();

        setupAddressPhoneNumberSaveAndCancelButtons(person, isEdit);
        CRUD.addButton.setText("Add");
        CRUD.addButton.setOnAction(event -> {
            String phoneNumber = phoneNumberField.getText();

            if (!PersonsDAO.checkPhoneNumber(phoneNumber)) {
                CRUD.showErrorMessage("Phone number is not valid.");
                return;
            }

            PersonsDAO.phoneNumbers.add(phoneNumber);
            logger.info("Phone numbers: " + PersonsDAO.phoneNumbers);

            showPersonsRecordsList();
            if (isEdit) {
                showPersonEditForm(person);
            } else {
                showEmptyForm();
            }
        });
        CRUD.cancelButton.setOnAction(event -> {
            showPersonsRecordsList();
            if (person != null) {
                showPersonEditForm(person);
            } else {
                showEmptyForm();
            }
        });
    }

    public static void showEmptyForm() {
        CRUD.recordsFormGrid.getChildren().clear();

        Label formTitle = new Label("Add Person");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);

        int count = setFormFields(CRUD.recordsFormGrid, PersonsDAO.personsFormController, 1);

        CRUD.recordsFormGrid.add(PersonsDAO.personsFormController.getAddressLabel(), 0, count++);
        Label plantName = new Label("Please select address");
        Button changeAddressButton = new Button("Select Address");

        if (PersonsDAO.selectedAddress != null) {
            plantName.setText(PersonsDAO.selectedAddress.getCity() + ", " + PersonsDAO.selectedAddress.getStreet() + " " + PersonsDAO.selectedAddress.getHouseNumber());
            changeAddressButton.setText("Change Address");
        }

        CRUD.recordsFormGrid.add(plantName, 0, count);
        changeAddressButton.setOnAction(event -> showAvailableAddresses(null, false, PersonsDAO.getListOfAvailableAddresses(null)));
        CRUD.recordsFormGrid.add(changeAddressButton, 1, count++);

        CRUD.recordsFormGrid.add(PersonsDAO.personsFormController.getPhoneNumbersLabel(), 0, count++);

        if (PersonsDAO.phoneNumbers != null) {
            for (String phoneNumber : PersonsDAO.phoneNumbers) {
                Label phoneNumberLabel = new Label(phoneNumber);
                Button deletePhoneNumberButton = getDeletePhoneNumberButton(phoneNumber, null);
                CRUD.recordsFormGrid.add(phoneNumberLabel, 0, count);
                CRUD.recordsFormGrid.add(deletePhoneNumberButton, 1, count++);
            }
        }

        Button addPhoneNumberButton = new Button("Add Phone Number");
        addPhoneNumberButton.setOnAction(event -> showAddPhoneNumberForm(null, false));
        CRUD.recordsFormGrid.add(addPhoneNumberButton, 0, count++);

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> PersonsController.addNewPerson());

        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(addButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }

    private static Button getSubmitEditFormButton(PersonsEntity person) {
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> PersonsController.editPerson(person));
        return submitButton;
    }
    private static Button getCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            PersonsDAO.clearAll();
            showPersonsRecordsList();
        });
        return cancelButton;
    }
    private static int setFormFields(GridPane formGrid, PersonsFormController personsFormController, int count) {
        formGrid.add(new Label("First Name:"), 0, count);
        formGrid.add(personsFormController.getPersonFirstNameField(), 1, count++);
        formGrid.add(new Label("Last Name:"), 0, count);
        formGrid.add(personsFormController.getPersonLastNameField(), 1, count++);
        formGrid.add(new Label("Birth Date:"), 0, count);
        formGrid.add(personsFormController.getPersonBirthDateField(), 1, count++);
        return count;
    }
    protected static void showAvailableAddresses(PersonsEntity person, boolean isEdit, List<AddressesEntity> availableAddresses) {
        CRUD.recordsGrid.getChildren().clear();

        CRUD.showCRUDScene();
        if (person != null) {
            showPersonEditForm(person);
        } else {
            showEmptyForm();
        }

        CRUD.nextPageButton.setVisible(true);
        CRUD.previousPageButton.setVisible(true);

        CRUD.nextPageButton.setOnAction(event -> {
            AddressesDAO.page++;
            showAvailableAddresses(person, isEdit, PersonsDAO.getListOfAvailableAddresses(person));
        });

        CRUD.previousPageButton.setOnAction(event -> {
            if (AddressesDAO.page > 1) {
                AddressesDAO.page--;
            }
            showAvailableAddresses(person, isEdit, PersonsDAO.getListOfAvailableAddresses(person));
        });

        int countAddresses = 0;
        for (AddressesEntity address : availableAddresses) {
            if (address == null || address.getCity() == null || address.getCity().isEmpty()) {
                continue;
            }
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(address.getCity() + ", " + address.getStreet() + " " + address.getHouseNumber() + ", " + address.getZipCode());
            GridPane.setRowIndex(recordGrid, countAddresses++);

            recordsController.infoButton.setOnAction(event -> {
                if (PersonsDAO.selectedAddress != null && PersonsDAO.selectedAddress.equals(address)) {
                    PersonsController.unselectAddress();
                    recordsController.recordInfo.setStyle("");
                    recordsController.infoButton.setText("Select");
                } else {
                    if (PersonsDAO.selectedAddress != null) {
                        CRUD.showWarningMessage("You can select only one address.");
                        return;
                    }
                    CRUD.clearMessage();
                    PersonsController.selectAddress(address);
                    recordsController.recordInfo.setStyle("-fx-background-color: #ffae00");
                    recordsController.infoButton.setText("Unselect");
                }
            });

            recordsController.infoButton.setText("Select");
            recordsController.editButton.setVisible(false);
            recordsController.deleteButton.setVisible(false);
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }

        setupAddressPhoneNumberSaveAndCancelButtons(person, isEdit);
        CRUD.cancelButton.setOnAction(event -> {
            showPersonsRecordsList();
            PersonsDAO.selectedAddress = null;
            if (person != null) {
                showPersonEditForm(person);
            } else {
                showEmptyForm();
            }
        });
    }
    private static void setupAddressPhoneNumberSaveAndCancelButtons(PersonsEntity person, boolean isEdit) {
        CRUD.addErrorMessageAndAddButton();
        CRUD.cancelButton.setVisible(true);
        CRUD.addButton.setText("Save selected");
        CRUD.addButton.setOnAction(event -> {
            showPersonsRecordsList();
            if (isEdit) {
                showPersonEditForm(person);
            } else {
                showEmptyForm();
            }
        });
    }
    private static Button getDeletePhoneNumberButton(String phoneNumber, Label label) {
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            if (PersonsDAO.phoneNumbersToDelete.contains(phoneNumber)) {
                label.setStyle("");
                PersonsController.cancelDeletePhoneNumber(phoneNumber);
            } else {
                label.setStyle("-fx-background-color: #af0303");
                PersonsController.deletePhoneNumber(phoneNumber);
            }
        });
        return deleteButton;
    }
}
