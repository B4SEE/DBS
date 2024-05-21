package cs.cvut.fel.dbs.main.tables.instances;

import cs.cvut.fel.dbs.entities.InstancesEntity;
import cs.cvut.fel.dbs.entities.PlantsEntity;
import cs.cvut.fel.dbs.entities.SectionsEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.RecordsController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;

public class InstancesView {
    private static final Logger logger = LogManager.getLogger(InstancesView.class);
    public static void showInstancesRecordsList() {
        CRUD.showCRUDScene();
        CRUD.recordsGridTitle.setText("Instances");

        logger.info("Initializing instances records list...");

        int count = CRUD.recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;

        for (InstancesEntity instance : InstancesDAO.getAllInstances()) {
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(instance.getInstanceName());
            GridPane.setRowIndex(recordGrid, count++);
            recordsController.infoButton.setOnAction(event -> {
                InstancesDAO.clearAll();
                showInstanceInfo(instance);
            });
            recordsController.editButton.setOnAction(event -> {
                InstancesDAO.clearAll();
                showInstanceEditForm(instance);
            });
            recordsController.deleteButton.setOnAction(event -> {
                InstancesDAO.deleteInstance(instance);
                showInstancesRecordsList();
            });
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }
        CRUD.addErrorMessageAndAddButton();
        CRUD.addButton.setOnAction(event -> {
            InstancesDAO.clearAll();
            showEmptyForm();
        });
    }
    public static void showInstanceInfo(InstancesEntity instance) {
        InstancesDAO.clearAll();
        CRUD.recordsFormGrid.getChildren().clear();

        InstancesDAO.instancesFormController.fillFormFields(instance);

        InstancesDAO.instancesFormController.getInstanceNameField().setEditable(false);
        InstancesDAO.instancesFormController.getInstanceAgeField().setEditable(false);

        Label formTitle = new Label("Instance Info");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, InstancesDAO.instancesFormController, 1);

        CRUD.recordsFormGrid.add(InstancesDAO.instancesFormController.getPlantTypeLabel(), 0, count++);
        Label plantName = new Label(instance.getPlant().getPlantName());
        logger.info("Plant name: " + instance.getPlant().getPlantName());
        CRUD.recordsFormGrid.add(plantName, 0, count++);

        CRUD.recordsFormGrid.add(InstancesDAO.instancesFormController.getSectionLabel(), 0, count++);
        Label sectionName = new Label(instance.getSection().getSectionName());
        logger.info("Section name: " + instance.getSection().getSectionName());
        CRUD.recordsFormGrid.add(sectionName, 0, count);
    }
    public static void showInstanceEditForm(InstancesEntity instance) {
        CRUD.recordsFormGrid.getChildren().clear();

        InstancesDAO.instancesFormController.fillFormFields(instance);

        Label formTitle = new Label("Edit Instance");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, InstancesDAO.instancesFormController, 1);

        CRUD.recordsFormGrid.add(InstancesDAO.instancesFormController.getPlantTypeLabel(), 0, count++);

        Label plantName = new Label(instance.getPlant().getPlantName());
        if (InstancesDAO.selectedPlant != null) {
            plantName.setText(InstancesDAO.selectedPlant.getPlantName());
        }

        CRUD.recordsFormGrid.add(plantName, 0, count);
        Button changePlantButton = new Button("Change Plant Type");
        changePlantButton.setOnAction(event -> showAvailablePlantTypes(instance, true, InstancesDAO.getListOfAvailablePlantTypes(instance)));
        CRUD.recordsFormGrid.add(changePlantButton, 1, count++);

        CRUD.recordsFormGrid.add(InstancesDAO.instancesFormController.getSectionLabel(), 0, count++);

        Label sectionName = new Label(instance.getSection().getSectionName());
        if (InstancesDAO.selectedSection != null) {
            sectionName.setText(InstancesDAO.selectedSection.getSectionName());
        }

        CRUD.recordsFormGrid.add(sectionName, 0, count);
        Button changeSectionButton = new Button("Change Section");
        changeSectionButton.setOnAction(event -> showAvailableSections(instance, true, InstancesDAO.getListOfAvailableSections(instance)));
        CRUD.recordsFormGrid.add(changeSectionButton, 1, count++);

        Button submitButton = getSubmitEditFormButton(instance);
        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(submitButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    public static void showEmptyForm() {
        CRUD.recordsFormGrid.getChildren().clear();

        Label formTitle = new Label("Add Instance");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);

        int count = setFormFields(CRUD.recordsFormGrid, InstancesDAO.instancesFormController, 1);

        CRUD.recordsFormGrid.add(InstancesDAO.instancesFormController.getPlantTypeLabel(), 0, count++);
        Label plantName = new Label("Please select a plant type");
        Button changePlantButton = new Button("Select Plant Type");

        if (InstancesDAO.selectedPlant != null) {
            plantName.setText(InstancesDAO.selectedPlant.getPlantName());
            changePlantButton.setText("Change Plant Type");
        }

        CRUD.recordsFormGrid.add(plantName, 0, count);
        changePlantButton.setOnAction(event -> showAvailablePlantTypes(null, false, InstancesDAO.getListOfAvailablePlantTypes(null)));
        CRUD.recordsFormGrid.add(changePlantButton, 1, count++);

        CRUD.recordsFormGrid.add(InstancesDAO.instancesFormController.getSectionLabel(), 0, count++);
        Label sectionName = new Label("Please select a section");
        Button changeSectionButton = new Button("Select Section");

        if (InstancesDAO.selectedSection != null) {
            sectionName.setText(InstancesDAO.selectedSection.getSectionName());
            changeSectionButton.setText("Change Section");
        }

        CRUD.recordsFormGrid.add(sectionName, 0, count);
        changeSectionButton.setOnAction(event -> showAvailableSections(null, false, InstancesDAO.getListOfAvailableSections(null)));
        CRUD.recordsFormGrid.add(changeSectionButton, 1, count++);

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> InstancesController.addNewInstance());

        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(addButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    private static Button getSubmitEditFormButton(InstancesEntity instance) {
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> InstancesController.editInstance(instance));
        return submitButton;
    }
    private static Button getCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            InstancesDAO.clearAll();
            showInstancesRecordsList();
        });
        return cancelButton;
    }
    private static int setFormFields(GridPane formGrid, InstancesFormController instancesFormController, int count) {
        formGrid.add(new Label("Instance Name:"), 0, count);
        formGrid.add(instancesFormController.getInstanceNameField(), 1, count++);
        formGrid.add(new Label("Instance Age:"), 0, count);
        formGrid.add(instancesFormController.getInstanceAgeField(), 1, count++);
        return count;
    }
    protected static void showAvailablePlantTypes(InstancesEntity instance, boolean isEdit, List<PlantsEntity> availablePlantTypes) {
        CRUD.recordsGrid.getChildren().clear();
        CRUD.showCRUDScene();
        if (instance != null) {
            showInstanceEditForm(instance);
        } else {
            showEmptyForm();
        }

        int countPlantTypes = 0;
        for (PlantsEntity plant : availablePlantTypes) {
            if (plant == null || plant.getPlantName() == null || plant.getPlantName().isEmpty()) {
                continue;
            }

            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(plant.getPlantName());
            GridPane.setRowIndex(recordGrid, countPlantTypes++);
            recordsController.recordInfo.setText(plant.getPlantName());

            recordsController.infoButton.setOnAction(event -> {
                if (InstancesDAO.selectedPlant != null && InstancesDAO.selectedPlant.equals(plant)) {
                    InstancesController.unselectPlant();
                    recordsController.recordInfo.setStyle("");
                    recordsController.infoButton.setText("Select");
                } else {
                    if (InstancesDAO.selectedPlant != null) {
                        CRUD.showWarningMessage("You can select only one plant type.");
                        return;
                    }
                    CRUD.clearMessage();
                    InstancesController.selectPlant(plant);
                    recordsController.recordInfo.setStyle("-fx-background-color: #ffae00");
                    recordsController.infoButton.setText("Unselect");
                }
            });

            recordsController.infoButton.setText("Select");
            recordsController.editButton.setVisible(false);
            recordsController.deleteButton.setVisible(false);
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }

        setupPlantSectionSaveAndCancelButtons(instance, isEdit);
        CRUD.cancelButton.setOnAction(event -> {
            showInstancesRecordsList();
            InstancesDAO.selectedPlant = null;
            if (instance != null) {
                showInstanceEditForm(instance);
            } else {
                showEmptyForm();
            }
        });
    }
    protected static void showAvailableSections(InstancesEntity instance, boolean isEdit, List<SectionsEntity> availableSections) {
        CRUD.recordsGrid.getChildren().clear();
        CRUD.showCRUDScene();
        if (instance != null) {
            showInstanceEditForm(instance);
        } else {
            showEmptyForm();
        }

        int countSections = 0;
        for (SectionsEntity section : availableSections) {
            if (section == null || section.getSectionName() == null || section.getSectionName().isEmpty()) {
                continue;
            }
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(section.getSectionName());
            GridPane.setRowIndex(recordGrid, countSections++);
            recordsController.recordInfo.setText(section.getSectionName());

            recordsController.infoButton.setOnAction(event -> {
                if (InstancesDAO.selectedSection != null && InstancesDAO.selectedSection.equals(section)) {
                    InstancesController.unselectSection();
                    recordsController.recordInfo.setStyle("");
                    recordsController.infoButton.setText("Select");
                } else {
                    if (InstancesDAO.selectedSection != null) {
                        CRUD.showWarningMessage("You can select only one section.");
                        return;
                    }
                    CRUD.clearMessage();
                    InstancesController.selectSection(section);
                    recordsController.recordInfo.setStyle("-fx-background-color: #ffae00");
                    recordsController.infoButton.setText("Unselect");
                }
            });

            recordsController.infoButton.setText("Select");
            recordsController.editButton.setVisible(false);
            recordsController.deleteButton.setVisible(false);
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }

        setupPlantSectionSaveAndCancelButtons(instance, isEdit);
        CRUD.cancelButton.setOnAction(event -> {
            showInstancesRecordsList();
            InstancesDAO.selectedSection = null;
            if (instance != null) {
                showInstanceEditForm(instance);
            } else {
                showEmptyForm();
            }
        });
    }
    private static void setupPlantSectionSaveAndCancelButtons(InstancesEntity instance, boolean isEdit) {
        CRUD.addErrorMessageAndAddButton();
        CRUD.cancelButton.setVisible(true);
        CRUD.addButton.setText("Save selected");
        CRUD.addButton.setOnAction(event -> {
            logger.info("Saving selected soil/climate types...");
            showInstancesRecordsList();
            if (isEdit) {
                showInstanceEditForm(instance);
            } else {
                showEmptyForm();
            }
        });
    }
}
