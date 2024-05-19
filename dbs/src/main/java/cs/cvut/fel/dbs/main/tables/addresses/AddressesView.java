package cs.cvut.fel.dbs.main.tables.addresses;

import cs.cvut.fel.dbs.entities.AddressesEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.RecordsController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Objects;

public class AddressesView {
    private static final Logger logger = LogManager.getLogger(AddressesView.class);
    public static void showAddressesRecordsList() {
        CRUD.showCRUDScene();
        CRUD.recordsGridTitle.setText("Addresses");
        CRUD.nextPageButton.setVisible(true);
        CRUD.previousPageButton.setVisible(true);

        CRUD.nextPageButton.setOnAction(event -> {
            AddressesDAO.page++;
            showAddressesRecordsList();
        });

        CRUD.previousPageButton.setOnAction(event -> {
            if (AddressesDAO.page > 1) {
                AddressesDAO.page--;
                showAddressesRecordsList();
            }
        });

        logger.info("Initializing addresses records list...");

        int count = CRUD.recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;

        for (AddressesEntity address : AddressesDAO.getAllAddresses()) {
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(address.getCity() + " " + address.getStreet() + " " + address.getHouseNumber());
            GridPane.setRowIndex(recordGrid, count++);
            recordsController.infoButton.setOnAction(event -> {
                AddressesDAO.clearAll();
                showAddressInfo(address);
            });
            recordsController.editButton.setOnAction(event -> {
                AddressesDAO.clearAll();
                showAddressEditForm(address);
            });
            recordsController.deleteButton.setOnAction(event -> {
                AddressesDAO.deleteAddress(address);
            });
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }
        CRUD.addErrorMessageAndAddButton();
        CRUD.addButton.setOnAction(event -> {
            AddressesDAO.clearAll();
            showEmptyForm();
        });
    }
    public static void showAddressInfo(AddressesEntity address) {
        AddressesDAO.clearAll();
        CRUD.recordsFormGrid.getChildren().clear();

        AddressesDAO.addressesFormController.fillFormFields(address);

        AddressesDAO.addressesFormController.getAddressCityField().setEditable(false);
        AddressesDAO.addressesFormController.getAddressStreetField().setEditable(false);
        AddressesDAO.addressesFormController.getAddressHouseNumberField().setEditable(false);
        AddressesDAO.addressesFormController.getAddressZipCodeField().setEditable(false);

        Label formTitle = new Label("Address Info");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        setFormFields(CRUD.recordsFormGrid, AddressesDAO.addressesFormController, 1);
    }
    public static void showAddressEditForm(AddressesEntity address) {
        CRUD.recordsFormGrid.getChildren().clear();

        AddressesDAO.addressesFormController.fillFormFields(address);

        Label formTitle = new Label("Edit Instance");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, AddressesDAO.addressesFormController, 1);

        Button submitButton = getSubmitEditFormButton(address);
        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(submitButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    public static void showEmptyForm() {
        CRUD.recordsFormGrid.getChildren().clear();

        Label formTitle = new Label("Add Plant");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);

        int count = setFormFields(CRUD.recordsFormGrid, AddressesDAO.addressesFormController, 1);

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> AddressesController.addNewAddress());

        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(addButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    private static Button getSubmitEditFormButton(AddressesEntity address) {
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> AddressesController.editAddress(address));
        return submitButton;
    }
    private static Button getCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            AddressesDAO.clearAll();
            showAddressesRecordsList();
        });
        return cancelButton;
    }
    private static int setFormFields(GridPane formGrid, AddressesFormController addressesFormController, int count) {
        formGrid.add(new Label("City:"), 0, count);
        formGrid.add(addressesFormController.getAddressCityField(), 1, count++);
        formGrid.add(new Label("Street:"), 0, count);
        formGrid.add(addressesFormController.getAddressStreetField(), 1, count++);
        formGrid.add(new Label("House number:"), 0, count);
        formGrid.add(addressesFormController.getAddressHouseNumberField(), 1, count++);
        formGrid.add(new Label("Zip code:"), 0, count);
        formGrid.add(addressesFormController.getAddressZipCodeField(), 1, count++);
        return count;
    }
}
