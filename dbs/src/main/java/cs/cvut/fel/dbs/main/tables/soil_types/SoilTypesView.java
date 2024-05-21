package cs.cvut.fel.dbs.main.tables.soil_types;

import cs.cvut.fel.dbs.entities.SoiltypesEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.RecordsController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.util.Objects;

public class SoilTypesView {
    private static final Logger logger = LogManager.getLogger(SoilTypesView.class);
    public static void showSoilTypesRecordsList() {
        CRUD.showCRUDScene();
        CRUD.recordsGridTitle.setText("Soil Types");

        SoilTypesDAO.soilTypesFormController.getSoilTypeNameField().setPrefWidth(100);
        SoilTypesDAO.soilTypesFormController.getSoilTypeDescriptionField().setPrefHeight(200);
        SoilTypesDAO.soilTypesFormController.getSoilTypeDescriptionField().setWrapText(true);

        logger.info("Initializing soil types records list");

        int count = CRUD.recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;

        for (SoiltypesEntity soilType : SoilTypesDAO.getAllSoilTypes()) {
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(soilType.getSoilName());
            GridPane.setRowIndex(recordGrid, count++);
            recordsController.infoButton.setOnAction(event -> {
                SoilTypesDAO.clearAll();
                showSoilTypeInfo(soilType);
            });
            recordsController.editButton.setOnAction(event -> {
                SoilTypesDAO.clearAll();
                showSoilTypeEditForm(soilType);
            });
            recordsController.deleteButton.setOnAction(event -> {
                SoilTypesDAO.deleteSoilType(soilType);
            });
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }
        CRUD.addErrorMessageAndAddButton();
        CRUD.addButton.setOnAction(event -> {
            SoilTypesDAO.clearAll();
            showEmptyForm();
        });
    }
    public static void showSoilTypeInfo(SoiltypesEntity soilType) {
        SoilTypesDAO.clearAll();
        CRUD.recordsFormGrid.getChildren().clear();

        SoilTypesDAO.soilTypesFormController.fillFormFields(soilType);

        SoilTypesDAO.soilTypesFormController.getSoilTypeNameField().setEditable(false);
        SoilTypesDAO.soilTypesFormController.getSoilTypeDescriptionField().setEditable(false);

        Label formTitle = new Label("Soil Type Info");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        setFormFields(CRUD.recordsFormGrid, SoilTypesDAO.soilTypesFormController, 1);
    }
    public static void showSoilTypeEditForm(SoiltypesEntity soilType) {
        CRUD.recordsFormGrid.getChildren().clear();

        SoilTypesDAO.soilTypesFormController.fillFormFields(soilType);

        Label formTitle = new Label("Edit Soil Type");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, SoilTypesDAO.soilTypesFormController, 1);

        Button submitButton = getSubmitEditFormButton(soilType);
        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(submitButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    public static void showEmptyForm() {
        CRUD.recordsFormGrid.getChildren().clear();

        Label formTitle = new Label("Add Soil Type");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);

        int count = setFormFields(CRUD.recordsFormGrid, SoilTypesDAO.soilTypesFormController, 1);

        Button addButton = new Button("Add Soil Type");
        addButton.setOnAction(event -> SoilTypesController.addNewSoilType());

        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(addButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    private static Button getSubmitEditFormButton(SoiltypesEntity soilType) {
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> SoilTypesController.editSoilType(soilType));
        return submitButton;
    }
    private static Button getCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            SoilTypesDAO.clearAll();
            showSoilTypesRecordsList();
        });
        return cancelButton;
    }
    private static int setFormFields(GridPane formGrid, SoilTypesFormController soilTypesFormController, int count) {
        formGrid.add(new Label("Name:"), 0, count);
        formGrid.add(soilTypesFormController.getSoilTypeNameField(), 1, count++);
        formGrid.add(new Label("Description:"), 0, count);
        formGrid.add(soilTypesFormController.getSoilTypeDescriptionField(), 1, count++);
        return count;
    }
}
