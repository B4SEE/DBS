package cs.cvut.fel.dbs.main.tables.climate_types;

import cs.cvut.fel.dbs.entities.ClimatetypesEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.RecordsController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Objects;
public class ClimateTypesView {
    private static final Logger logger = LogManager.getLogger(ClimateTypesView.class);
    public static void showClimateTypesRecordsList() {
        CRUD.showCRUDScene();
        CRUD.recordsGridTitle.setText("Climate Types");

        ClimateTypesDAO.climateTypesFormController.getClimateTypeNameField().setPrefWidth(100);
        ClimateTypesDAO.climateTypesFormController.getClimateTypeDescriptionField().setPrefHeight(200);
        ClimateTypesDAO.climateTypesFormController.getClimateTypeDescriptionField().setWrapText(true);

        logger.info("Initializing climate types records list...");

        int count = CRUD.recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;

        for (ClimatetypesEntity climateType : ClimateTypesDAO.getAllClimateTypes()) {
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(climateType.getClimateName());
            GridPane.setRowIndex(recordGrid, count++);
            recordsController.infoButton.setOnAction(event -> {
                ClimateTypesDAO.clearAll();
                showClimateTypeInfo(climateType);
            });
            recordsController.editButton.setOnAction(event -> {
                ClimateTypesDAO.clearAll();
                showClimateTypeEditForm(climateType);
            });
            recordsController.deleteButton.setOnAction(event -> {
                ClimateTypesDAO.deleteClimateType(climateType);
            });
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }
        CRUD.addErrorMessageAndAddButton();
        CRUD.addButton.setOnAction(event -> {
            ClimateTypesDAO.clearAll();
            showEmptyForm();
        });
    }
    public static void showClimateTypeInfo(ClimatetypesEntity climateType) {
        ClimateTypesDAO.clearAll();
        CRUD.recordsFormGrid.getChildren().clear();

        ClimateTypesDAO.climateTypesFormController.fillFormFields(climateType);

        ClimateTypesDAO.climateTypesFormController.getClimateTypeNameField().setEditable(false);
        ClimateTypesDAO.climateTypesFormController.getClimateTypeDescriptionField().setEditable(false);

        Label formTitle = new Label("Address Info");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        setFormFields(CRUD.recordsFormGrid, ClimateTypesDAO.climateTypesFormController, 1);
    }
    public static void showClimateTypeEditForm(ClimatetypesEntity climateType) {
        CRUD.recordsFormGrid.getChildren().clear();

        ClimateTypesDAO.climateTypesFormController.fillFormFields(climateType);

        Label formTitle = new Label("Edit Instance");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, ClimateTypesDAO.climateTypesFormController, 1);

        Button submitButton = getSubmitEditFormButton(climateType);
        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(submitButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    public static void showEmptyForm() {
        CRUD.recordsFormGrid.getChildren().clear();

        Label formTitle = new Label("Add Plant");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);

        int count = setFormFields(CRUD.recordsFormGrid, ClimateTypesDAO.climateTypesFormController, 1);

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> ClimateTypesController.addNewAddress());

        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(addButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    private static Button getSubmitEditFormButton(ClimatetypesEntity climateType) {
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> ClimateTypesController.editAddress(climateType));
        return submitButton;
    }
    private static Button getCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            ClimateTypesDAO.clearAll();
            showClimateTypesRecordsList();
        });
        return cancelButton;
    }
    private static int setFormFields(GridPane formGrid, ClimateTypesFormController climateTypesFormController, int count) {
        formGrid.add(new Label("Name:"), 0, count);
        formGrid.add(climateTypesFormController.getClimateTypeNameField(), 1, count++);
        formGrid.add(new Label("Description:"), 0, count);
        formGrid.add(climateTypesFormController.getClimateTypeDescriptionField(), 1, count++);
        return count;
    }
}
