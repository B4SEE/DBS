package cs.cvut.fel.dbs.main.tables.plants;

import cs.cvut.fel.dbs.entities.ClimatetypesEntity;
import cs.cvut.fel.dbs.entities.PlantsEntity;
import cs.cvut.fel.dbs.entities.SoiltypesEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.RecordsController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;

public class PlantsView {
    private static final Logger logger = LogManager.getLogger(PlantsView.class);
    public static void showPlantsRecordsList() {
        CRUD.showCRUDScene();
        CRUD.recordsGridTitle.setText("Plants");

        logger.info("Initializing plants list...");

        int count = CRUD.recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;

        for (PlantsEntity plant : PlantsDAO.getAllPlants()) {
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(plant.getPlantName());
            GridPane.setRowIndex(recordGrid, count++);
            recordsController.infoButton.setOnAction(event -> {
                PlantsDAO.clearAll();
                showPlantInfo(plant);
            });
            recordsController.editButton.setOnAction(event -> {
                PlantsDAO.clearAll();
                showPlantEditForm(plant);
            });
            recordsController.deleteButton.setOnAction(event -> {
                PlantsDAO.deletePlant(plant);
                showPlantsRecordsList();
            });
            CRUD.recordsGrid.getChildren().add(recordGrid);
            logger.info("Plant record added: " + plant.getPlantName() + "at row " + count);
        }
        CRUD.addErrorMessageAndAddButton();
        CRUD.addButton.setOnAction(event -> {
            PlantsDAO.clearAll();
            showEmptyForm();
        });
    }
    public static void showPlantInfo(PlantsEntity plant) {
        PlantsDAO.clearAll();
        CRUD.recordsFormGrid.getChildren().clear();

        PlantsDAO.plantsFormController.fillFormFields(plant);

        PlantsDAO.plantsFormController.getPlantNameField().setEditable(false);
        PlantsDAO.plantsFormController.getPlantTypeField().setEditable(false);
        PlantsDAO.plantsFormController.getMinTemperatureField().setEditable(false);
        PlantsDAO.plantsFormController.getMaxTemperatureField().setEditable(false);
        PlantsDAO.plantsFormController.getMinLightField().setEditable(false);
        PlantsDAO.plantsFormController.getMaxLightField().setEditable(false);

        Label formTitle = new Label("Plant Info");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, PlantsDAO.plantsFormController, 1);

        CRUD.recordsFormGrid.add(PlantsDAO.plantsFormController.getPreferredSoilTypesLabel(), 0, count++);
        List<SoiltypesEntity> preferredSoilTypes = plant.getPreferredSoilTypes();
        for (SoiltypesEntity soilType : preferredSoilTypes) {
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(new Label(soilType.getSoilName()));
            CRUD.recordsFormGrid.add(stackPane, 0, count++);
        }

        CRUD.recordsFormGrid.add(PlantsDAO.plantsFormController.getPreferredClimateTypesLabel(), 0, count++);
        List<ClimatetypesEntity> preferredClimateTypes = plant.getPreferredClimateTypes();
        for (ClimatetypesEntity climateType : preferredClimateTypes) {
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(new Label(climateType.getClimateName()));
            CRUD.recordsFormGrid.add(stackPane, 0, count++);
        }
    }
    public static void showPlantEditForm(PlantsEntity plant) {
        CRUD.recordsFormGrid.getChildren().clear();

        PlantsDAO.plantsFormController.fillFormFields(plant);

        Label formTitle = new Label("Edit Plant");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, PlantsDAO.plantsFormController, 1);

        CRUD.recordsFormGrid.add(PlantsDAO.plantsFormController.getPreferredSoilTypesLabel(), 0, count++);
        // add list of preferred soil types with delete button
        List<SoiltypesEntity> preferredSoilTypes = plant.getPreferredSoilTypes();
        if (preferredSoilTypes.isEmpty()) {
            CRUD.recordsFormGrid.add(new Label("No preferred soil types"), 0, count++);
        }
        if (!PlantsDAO.selectedSoilTypes.isEmpty()) {
            preferredSoilTypes.addAll(PlantsDAO.selectedSoilTypes);
        }
        for (SoiltypesEntity soilType : preferredSoilTypes) {
            Label label = new Label(soilType.getSoilName());

            Button deleteButton = getDeleteSoilTypeButton(soilType, label);

            CRUD.recordsFormGrid.add(label, 0, count);
            CRUD.recordsFormGrid.add(deleteButton, 1, count++);
        }

        Button addSoilTypeButton = getAddSoilTypeButton(plant);
        CRUD.recordsFormGrid.add(addSoilTypeButton, 0, count++);

        CRUD.recordsFormGrid.add(PlantsDAO.plantsFormController.getPreferredClimateTypesLabel(), 0, count++);
        List<ClimatetypesEntity> preferredClimateTypes = plant.getPreferredClimateTypes();
        if (preferredClimateTypes.isEmpty()) {
            CRUD.recordsFormGrid.add(new Label("No preferred climate types"), 0, count++);
        }
        if (!PlantsDAO.selectedClimateTypes.isEmpty()) {
            preferredClimateTypes.addAll(PlantsDAO.selectedClimateTypes);
        }
        for (ClimatetypesEntity climateType : preferredClimateTypes) {
            Label label = new Label(climateType.getClimateName());

            Button deleteButton = getDeleteClimateTypeButton(climateType, label);

            CRUD.recordsFormGrid.add(label, 0, count);
            CRUD.recordsFormGrid.add(deleteButton, 1, count++);
        }

        Button addClimateTypeButton = getAddClimateTypeButton(plant);
        CRUD.recordsFormGrid.add(addClimateTypeButton, 0, count++);

        Button submitButton = getSubmitEditFormButton(plant);
        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(submitButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    public static void showEmptyForm() {
        CRUD.recordsFormGrid.getChildren().clear();

        Label formTitle = new Label("Add Plant");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);

        int count = setFormFields(CRUD.recordsFormGrid, PlantsDAO.plantsFormController, 1);

        CRUD.recordsFormGrid.add(PlantsDAO.plantsFormController.getPreferredSoilTypesLabel(), 0, count++);
        if (PlantsDAO.selectedSoilTypes.isEmpty()) {
            CRUD.recordsFormGrid.add(new Label("No preferred soil types"), 0, count++);
        }

        for (SoiltypesEntity soilType : PlantsDAO.selectedSoilTypes) {
            Label label = new Label(soilType.getSoilName());

            Button deleteButton = getDeleteSoilTypeButton(soilType, label);

            CRUD.recordsFormGrid.add(label, 0, count);
            CRUD.recordsFormGrid.add(deleteButton, 1, count++);
        }

        Button addSoilTypeButton = new Button("Add Soil Type");
        addSoilTypeButton.setOnAction(event -> showAvailableSoilTypes(null, false, PlantsDAO.getListOfAvailableSoilTypes(null)));

        CRUD.recordsFormGrid.add(addSoilTypeButton, 0, count++);
        CRUD.recordsFormGrid.add(PlantsDAO.plantsFormController.getPreferredClimateTypesLabel(), 0, count++);
        if (PlantsDAO.selectedClimateTypes.isEmpty()) {
            CRUD.recordsFormGrid.add(new Label("No preferred climate types"), 0, count++);
        }

        for (ClimatetypesEntity climateType : PlantsDAO.selectedClimateTypes) {
            Label label = new Label(climateType.getClimateName());

            Button deleteButton = getDeleteClimateTypeButton(climateType, label);

            CRUD.recordsFormGrid.add(label, 0, count);
            CRUD.recordsFormGrid.add(deleteButton, 1, count++);
        }

        Button addClimateTypeButton = new Button("Add Climate Type");
        addClimateTypeButton.setOnAction(event -> showAvailableClimateTypes(null, false, PlantsDAO.getListOfAvailableClimateTypes(null)));

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> PlantsController.addNewPlant());

        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(addClimateTypeButton, 0, count++);
        CRUD.recordsFormGrid.add(addButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }

    private static Button getDeleteSoilTypeButton(SoiltypesEntity soilType, Label label) {
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            if (PlantsDAO.soilTypesToDelete.contains(soilType)) {
                label.setStyle("");
                PlantsController.cancelDeletePreferredSoilType(soilType);
                logger.info("Soil type delete cancelled: " + soilType.getSoilName());
            } else {
                label.setStyle("-fx-background-color: #af0303");
                PlantsController.deletePreferredSoilType(soilType);
                logger.info("Soil type delete confirmed: " + soilType.getSoilName());
            }
        });
        return deleteButton;
    }
    private static Button getDeleteClimateTypeButton(ClimatetypesEntity climateType, Label label) {
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            if (PlantsDAO.climateTypesToDelete.contains(climateType)) {
                label.setStyle("");
                PlantsController.cancelDeletePreferredClimateType(climateType);
                logger.info("Climate type delete cancelled: " + climateType.getClimateName());
            } else {
                label.setStyle("-fx-background-color: #af0303");
                PlantsController.deletePreferredClimateType(climateType);
                logger.info("Climate type delete confirmed: " + climateType.getClimateName());
            }
        });
        return deleteButton;
    }

    private static Button getAddSoilTypeButton(PlantsEntity plant) {
        Button addSoilTypeButton = new Button("Add Soil Type");
        addSoilTypeButton.setOnAction(event -> showAvailableSoilTypes(plant, true, PlantsDAO.getListOfAvailableSoilTypes(plant)));
        return addSoilTypeButton;
    }

    private static Button getAddClimateTypeButton(PlantsEntity plant) {
        Button addClimateTypeButton = new Button("Add Climate Type");
        addClimateTypeButton.setOnAction(event -> showAvailableClimateTypes(plant, true, PlantsDAO.getListOfAvailableClimateTypes(plant)));
        return addClimateTypeButton;
    }
    private static Button getSubmitEditFormButton(PlantsEntity plant) {
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> PlantsController.editPlant(plant));
        return submitButton;
    }
    private static Button getCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            PlantsDAO.clearAll();
            showPlantsRecordsList();
        });
        return cancelButton;
    }
    private static int setFormFields(GridPane formGrid, PlantsFormController plantsFormController, int count) {
        formGrid.add(new Label("Plant Name:"), 0, count);
        formGrid.add(plantsFormController.getPlantNameField(), 1, count++);
        formGrid.add(new Label("Plant Type:"), 0, count);
        formGrid.add(plantsFormController.getPlantTypeField(), 1, count++);
        formGrid.add(new Label("Min Temperature:"), 0, count);
        formGrid.add(plantsFormController.getMinTemperatureField(), 1, count++);
        formGrid.add(new Label("Max Temperature:"), 0, count);
        formGrid.add(plantsFormController.getMaxTemperatureField(), 1, count++);
        formGrid.add(new Label("Min Light:"), 0, count);
        formGrid.add(plantsFormController.getMinLightField(), 1, count++);
        formGrid.add(new Label("Max Light:"), 0, count);
        formGrid.add(plantsFormController.getMaxLightField(), 1, count++);
        return count;
    }
    protected static void showAvailableSoilTypes(PlantsEntity plant, boolean isEdit, List<SoiltypesEntity> availableSoilTypes) {
        CRUD.recordsGrid.getChildren().clear();
        CRUD.showCRUDScene();
        if (plant != null) {
            showPlantEditForm(plant);
        } else {
            showEmptyForm();
        }

        int countSoilTypes = 0;
        for (SoiltypesEntity soilType : availableSoilTypes) {
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(soilType.getSoilName());
            GridPane.setRowIndex(recordGrid, countSoilTypes++);
            recordsController.recordInfo.setText(soilType.getSoilName());

            recordsController.infoButton.setOnAction(event -> {
                if (PlantsDAO.selectedSoilTypes.contains(soilType)) {
                    PlantsController.unselectSoilType(soilType);
                    logger.info("Soil type unselected: " + soilType.getSoilName());
                    recordsController.recordInfo.setStyle("");
                    recordsController.infoButton.setText("Select");
                } else {
                    PlantsController.selectSoilType(soilType);
                    logger.info("Soil type selected: " + soilType.getSoilName());
                    recordsController.recordInfo.setStyle("-fx-background-color: #ffae00");
                    recordsController.infoButton.setText("Unselect");
                }
            });

            recordsController.infoButton.setText("Select");
            recordsController.editButton.setVisible(false);
            recordsController.deleteButton.setVisible(false);
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }

        setupSoilClimateTypesSaveAndCancelButtons(plant, isEdit);
        CRUD.cancelButton.setOnAction(event -> {
            showPlantsRecordsList();
            PlantsDAO.selectedSoilTypes.clear();
            if (plant != null) {
                showPlantEditForm(plant);
            } else {
                showEmptyForm();
            }
        });
    }
    protected static void showAvailableClimateTypes(PlantsEntity plant, boolean isEdit, List<ClimatetypesEntity> availableClimateTypes) {
        CRUD.recordsGrid.getChildren().clear();
        CRUD.showCRUDScene();
        if (plant != null) {
            showPlantEditForm(plant);
        } else {
            showEmptyForm();
        }

        int countClimateTypes = 0;
        for (ClimatetypesEntity climateType : availableClimateTypes) {
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(climateType.getClimateName());
            GridPane.setRowIndex(recordGrid, countClimateTypes++);
            recordsController.recordInfo.setText(climateType.getClimateName());

            recordsController.infoButton.setOnAction(event -> {
                if (PlantsDAO.selectedClimateTypes.contains(climateType)) {
                    PlantsController.unselectClimateType(climateType);
                    logger.info("Climate type unselected: " + climateType.getClimateName());
                    recordsController.recordInfo.setStyle("");
                    recordsController.infoButton.setText("Select");
                } else {
                    PlantsController.selectClimateType(climateType);
                    logger.info("Climate type selected: " + climateType.getClimateName());
                    recordsController.recordInfo.setStyle("-fx-background-color: #ffae00");
                    recordsController.infoButton.setText("Unselect");
                }
            });

            recordsController.infoButton.setText("Select");
            recordsController.editButton.setVisible(false);
            recordsController.deleteButton.setVisible(false);
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }

        setupSoilClimateTypesSaveAndCancelButtons(plant, isEdit);
        CRUD.cancelButton.setOnAction(event -> {
            showPlantsRecordsList();
            PlantsDAO.selectedClimateTypes.clear();
            if (plant != null) {
                showPlantEditForm(plant);
            } else {
                showEmptyForm();
            }
        });
    }

    private static void setupSoilClimateTypesSaveAndCancelButtons(PlantsEntity plant, boolean isEdit) {
        CRUD.addErrorMessageAndAddButton();
        CRUD.cancelButton.setVisible(true);
        CRUD.addButton.setText("Save selected");
        CRUD.addButton.setOnAction(event -> {
            logger.info("Saving selected soil/climate types...");
            if (PlantsDAO.selectedSoilTypes.isEmpty() && PlantsDAO.selectedClimateTypes.isEmpty()) {
                logger.info("No soil/climate types selected");
            }
            showPlantsRecordsList();
            if (isEdit) {
                showPlantEditForm(plant);
            } else {
                showEmptyForm();
            }
        });
    }

    protected static void showErrorMessage(String message) {
        CRUD.errorMessage.setText(message);
        CRUD.errorMessage.setVisible(true);
    }
    protected static boolean isErrorMessageEmpty() {
        return CRUD.errorMessage.getText().isEmpty();
    }
}
