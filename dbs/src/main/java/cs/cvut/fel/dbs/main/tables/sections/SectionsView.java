package cs.cvut.fel.dbs.main.tables.sections;

import cs.cvut.fel.dbs.entities.ClimatetypesEntity;
import cs.cvut.fel.dbs.entities.EmployeesEntity;
import cs.cvut.fel.dbs.entities.SectionsEntity;
import cs.cvut.fel.dbs.entities.SoiltypesEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.RecordsController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;

public class SectionsView {
    private static final Logger logger = LogManager.getLogger(SectionsView.class);
    public static void showSectionsRecordsList() {
        CRUD.showCRUDScene();
        CRUD.recordsGridTitle.setText("Sections");

        logger.info("Initializing sections records list...");

        int count = CRUD.recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;

        for (SectionsEntity section : SectionsDAO.getAllSections()) {
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(section.getSectionName());
            GridPane.setRowIndex(recordGrid, count++);
            recordsController.infoButton.setOnAction(event -> {
                SectionsDAO.clearAll();
                showSectionInfo(section);
            });
            recordsController.editButton.setOnAction(event -> {
                SectionsDAO.clearAll();
                showSectionEditForm(section);
            });
            recordsController.deleteButton.setOnAction(event -> {
                SectionsDAO.deleteSection(section);
            });
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }
        CRUD.addErrorMessageAndAddButton();
        CRUD.addButton.setOnAction(event -> {
            SectionsDAO.clearAll();
            showEmptyForm();
        });
    }
    public static void showSectionInfo(SectionsEntity section) {
        SectionsDAO.clearAll();
        CRUD.recordsFormGrid.getChildren().clear();

        SectionsDAO.sectionsFormController.fillFormFields(section);

        SectionsDAO.sectionsFormController.getSectionNameField().setEditable(false);
        SectionsDAO.sectionsFormController.getGeographicalCoordinatesField().setEditable(false);
        SectionsDAO.sectionsFormController.getTemperatureField().setEditable(false);
        SectionsDAO.sectionsFormController.getLightField().setEditable(false);

        Label formTitle = new Label("Section Info");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, SectionsDAO.sectionsFormController, 1);

        CRUD.recordsFormGrid.add(SectionsDAO.sectionsFormController.getSoilTypeLabel(), 0, count++);
        Label soilType = new Label(section.getSoilType().getSoilName());
        CRUD.recordsFormGrid.add(soilType, 0, count++);

        CRUD.recordsFormGrid.add(SectionsDAO.sectionsFormController.getClimateTypeLabel(), 0, count++);
        Label climateType = new Label(section.getClimateType().getClimateName());
        CRUD.recordsFormGrid.add(climateType, 0, count++);

        CRUD.recordsFormGrid.add(SectionsDAO.sectionsFormController.getIsHandledByLabel(), 0, count++);
        Label sectionManager = new Label(section.getIsHandledBy().getPerson().getFirstName() + " " + section.getIsHandledBy().getPerson().getLastName());
        CRUD.recordsFormGrid.add(sectionManager, 0, count);
    }
    public static void showSectionEditForm(SectionsEntity section) {
        CRUD.recordsFormGrid.getChildren().clear();

        SectionsDAO.sectionsFormController.fillFormFields(section);

        Label formTitle = new Label("Edit Section");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, SectionsDAO.sectionsFormController, 1);

        //

        CRUD.recordsFormGrid.add(SectionsDAO.sectionsFormController.getSoilTypeLabel(), 0, count++);
        Label soilType = new Label(section.getSoilType().getSoilName());
        if (SectionsDAO.selectedSoilType != null) {
            soilType.setText(SectionsDAO.selectedSoilType.getSoilName());
        }
        CRUD.recordsFormGrid.add(soilType, 0, count);

        Button changeSoilTypeButton = new Button("Change Soil Type");
        changeSoilTypeButton.setOnAction(event -> showAvailableSoilTypes(section, true, SectionsDAO.getListOfAvailableSoilTypes(section)));
        CRUD.recordsFormGrid.add(changeSoilTypeButton, 1, count++);

        //

        CRUD.recordsFormGrid.add(SectionsDAO.sectionsFormController.getClimateTypeLabel(), 0, count++);
        Label climateType = new Label(section.getClimateType().getClimateName());
        if (SectionsDAO.selectedClimateType != null) {
            climateType.setText(SectionsDAO.selectedClimateType.getClimateName());
        }
        CRUD.recordsFormGrid.add(climateType, 0, count);

        Button changeClimateTypeButton = new Button("Change Climate Type");
        changeClimateTypeButton.setOnAction(event -> showAvailableClimateTypes(section, true, SectionsDAO.getListOfAvailableClimateTypes(section)));
        CRUD.recordsFormGrid.add(changeClimateTypeButton, 1, count++);

        //

        CRUD.recordsFormGrid.add(SectionsDAO.sectionsFormController.getIsHandledByLabel(), 0, count++);
        Label sectionManager = new Label(section.getIsHandledBy().getPerson().getFirstName() + " " + section.getIsHandledBy().getPerson().getLastName());
        if (SectionsDAO.selectedSectionManager != null) {
            sectionManager.setText(SectionsDAO.selectedSectionManager.getPerson().getFirstName() + " " + SectionsDAO.selectedSectionManager.getPerson().getLastName());
        }
        CRUD.recordsFormGrid.add(sectionManager, 0, count);

        Button changeSectionManagerButton = new Button("Change Section Manager");
        changeSectionManagerButton.setOnAction(event -> showAvailableSectionManagers(section, true, SectionsDAO.getListOfAvailableEmployees(section)));
        CRUD.recordsFormGrid.add(changeSectionManagerButton, 1, count++);

        //

        Button submitButton = getSubmitEditFormButton(section);
        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(submitButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    public static void showEmptyForm() {
        CRUD.recordsFormGrid.getChildren().clear();

        Label formTitle = new Label("Add New Section");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);

        int count = setFormFields(CRUD.recordsFormGrid, SectionsDAO.sectionsFormController, 1);

        //

        CRUD.recordsFormGrid.add(SectionsDAO.sectionsFormController.getSoilTypeLabel(), 0, count++);
        Label soilType = new Label("Please select a soil type");
        Button changeSoilTypeButton = new Button("Select Soil Type");
        if (SectionsDAO.selectedSoilType != null) {
            soilType.setText(SectionsDAO.selectedSoilType.getSoilName());
            changeSoilTypeButton.setText("Change Soil Type");
        }

        CRUD.recordsFormGrid.add(soilType, 0, count);
        changeSoilTypeButton.setOnAction(event -> showAvailableSoilTypes(null, false, SectionsDAO.getListOfAvailableSoilTypes(null)));
        CRUD.recordsFormGrid.add(changeSoilTypeButton, 1, count++);

        //

        CRUD.recordsFormGrid.add(SectionsDAO.sectionsFormController.getClimateTypeLabel(), 0, count++);
        Label climateType = new Label("Please select a climate type");
        Button changeClimateTypeButton = new Button("Select Climate Type");
        if (SectionsDAO.selectedClimateType != null) {
            climateType.setText(SectionsDAO.selectedClimateType.getClimateName());
            changeClimateTypeButton.setText("Change Climate Type");
        }

        CRUD.recordsFormGrid.add(climateType, 0, count);
        changeClimateTypeButton.setOnAction(event -> showAvailableClimateTypes(null, false, SectionsDAO.getListOfAvailableClimateTypes(null)));
        CRUD.recordsFormGrid.add(changeClimateTypeButton, 1, count++);

        //

        CRUD.recordsFormGrid.add(SectionsDAO.sectionsFormController.getIsHandledByLabel(), 0, count++);
        Label sectionManager = new Label("Please select a section manager");
        Button changeSectionManagerButton = new Button("Select Section Manager");
        if (SectionsDAO.selectedSectionManager != null) {
            sectionManager.setText(SectionsDAO.selectedSectionManager.getPerson().getFirstName() + " " + SectionsDAO.selectedSectionManager.getPerson().getLastName());
            changeSectionManagerButton.setText("Change Section Manager");
        }

        CRUD.recordsFormGrid.add(sectionManager, 0, count);
        changeSectionManagerButton.setOnAction(event -> showAvailableSectionManagers(null, false, SectionsDAO.getListOfAvailableEmployees(null)));
        CRUD.recordsFormGrid.add(changeSectionManagerButton, 1, count++);

        //

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> SectionsController.addNewSection());

        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(addButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    private static Button getSubmitEditFormButton(SectionsEntity section) {
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> SectionsController.editSection(section));
        return submitButton;
    }
    private static Button getCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            SectionsDAO.clearAll();
            showSectionsRecordsList();
        });
        return cancelButton;
    }
    private static int setFormFields(GridPane formGrid, SectionsFormController sectionsFormController, int count) {
        formGrid.add(new Label("Section Name:"), 0, count);
        formGrid.add(sectionsFormController.getSectionNameField(), 1, count++);
        formGrid.add(new Label("Geographical Coordinates:"), 0, count);
        formGrid.add(sectionsFormController.getGeographicalCoordinatesField(), 1, count++);
        formGrid.add(new Label("Temperature:"), 0, count);
        formGrid.add(sectionsFormController.getTemperatureField(), 1, count++);
        formGrid.add(new Label("Light:"), 0, count);
        formGrid.add(sectionsFormController.getLightField(), 1, count++);
        return count;
    }
    protected static void showAvailableSoilTypes(SectionsEntity section, boolean isEdit, List<SoiltypesEntity> availableSoilTypes) {
        CRUD.recordsGrid.getChildren().clear();
        CRUD.showCRUDScene();
        if (section != null) {
            showSectionEditForm(section);
        } else {
            showEmptyForm();
        }

        int countSoilTypes = 0;
        for (SoiltypesEntity soilType : availableSoilTypes) {
            if (soilType == null || soilType.getSoilName() == null || soilType.getSoilName().isEmpty()) {
                continue;
            }

            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(soilType.getSoilName());
            GridPane.setRowIndex(recordGrid, countSoilTypes++);

            recordsController.infoButton.setOnAction(event -> {
                if (SectionsDAO.selectedSoilType != null && SectionsDAO.selectedSoilType.equals(soilType)) {
                    SectionsController.unselectSoilType();
                    recordsController.recordInfo.setStyle("");
                    recordsController.infoButton.setText("Select");
                } else {
                    if (SectionsDAO.selectedSoilType != null) {
                        CRUD.showWarningMessage("You can select only one soil type.");
                        return;
                    }
                    CRUD.clearMessage();
                    SectionsController.selectSoilType(soilType);
                    recordsController.recordInfo.setStyle("-fx-background-color: #ffae00");
                    recordsController.infoButton.setText("Unselect");
                }
            });

            recordsController.infoButton.setText("Select");
            recordsController.editButton.setVisible(false);
            recordsController.deleteButton.setVisible(false);
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }

        setupSoilClimateManagerSaveAndCancelButtons(section, isEdit);
        CRUD.cancelButton.setOnAction(event -> {
            showSectionsRecordsList();
            SectionsDAO.selectedSoilType = null;
            if (section != null) {
                showSectionEditForm(section);
            } else {
                showEmptyForm();
            }
        });
    }
    protected static void showAvailableClimateTypes(SectionsEntity section, boolean isEdit, List<ClimatetypesEntity> availableClimateTypes) {
        CRUD.recordsGrid.getChildren().clear();
        CRUD.showCRUDScene();
        if (section != null) {
            showSectionEditForm(section);
        } else {
            showEmptyForm();
        }

        int countClimateTypes = 0;
        for (ClimatetypesEntity climateType : availableClimateTypes) {
            if (climateType == null || climateType.getClimateName() == null || climateType.getClimateName().isEmpty()) {
                continue;
            }

            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(climateType.getClimateName());
            GridPane.setRowIndex(recordGrid, countClimateTypes++);

            recordsController.infoButton.setOnAction(event -> {
                if (SectionsDAO.selectedClimateType != null && SectionsDAO.selectedClimateType.equals(climateType)) {
                    SectionsController.unselectClimateType();
                    recordsController.recordInfo.setStyle("");
                    recordsController.infoButton.setText("Select");
                } else {
                    if (SectionsDAO.selectedClimateType != null) {
                        CRUD.showWarningMessage("You can select only one climate type.");
                        return;
                    }
                    CRUD.clearMessage();
                    SectionsController.selectClimateType(climateType);
                    recordsController.recordInfo.setStyle("-fx-background-color: #ffae00");
                    recordsController.infoButton.setText("Unselect");
                }
            });

            recordsController.infoButton.setText("Select");
            recordsController.editButton.setVisible(false);
            recordsController.deleteButton.setVisible(false);
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }

        setupSoilClimateManagerSaveAndCancelButtons(section, isEdit);
        CRUD.cancelButton.setOnAction(event -> {
            showSectionsRecordsList();
            SectionsDAO.selectedClimateType = null;
            if (section != null) {
                showSectionEditForm(section);
            } else {
                showEmptyForm();
            }
        });
    }
    protected static void showAvailableSectionManagers(SectionsEntity section, boolean isEdit, List<EmployeesEntity> availableSectionManagers) {
        CRUD.recordsGrid.getChildren().clear();
        CRUD.showCRUDScene();
        if (section != null) {
            showSectionEditForm(section);
        } else {
            showEmptyForm();
        }

        int countSectionManagers = 0;
        for (EmployeesEntity sectionManager : availableSectionManagers) {
            if (sectionManager == null || sectionManager.getPerson() == null || sectionManager.getPerson().getFirstName() == null || sectionManager.getPerson().getFirstName().isEmpty()) {
                continue;
            }

            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(sectionManager.getPerson().getFirstName() + " " + sectionManager.getPerson().getLastName());
            GridPane.setRowIndex(recordGrid, countSectionManagers++);

            recordsController.infoButton.setOnAction(event -> {
                if (SectionsDAO.selectedSectionManager != null && SectionsDAO.selectedSectionManager.equals(sectionManager)) {
                    SectionsController.unselectSectionManager();
                    recordsController.recordInfo.setStyle("");
                    recordsController.infoButton.setText("Select");
                } else {
                    if (SectionsDAO.selectedSectionManager != null) {
                        CRUD.showWarningMessage("You can select only one section manager.");
                        return;
                    }
                    CRUD.clearMessage();
                    SectionsController.selectSectionManager(sectionManager);
                    recordsController.recordInfo.setStyle("-fx-background-color: #ffae00");
                    recordsController.infoButton.setText("Unselect");
                }
            });

            recordsController.infoButton.setText("Select");
            recordsController.editButton.setVisible(false);
            recordsController.deleteButton.setVisible(false);
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }

        setupSoilClimateManagerSaveAndCancelButtons(section, isEdit);
        CRUD.cancelButton.setOnAction(event -> {
            showSectionsRecordsList();
            SectionsDAO.selectedSectionManager = null;
            if (section != null) {
                showSectionEditForm(section);
            } else {
                showEmptyForm();
            }
        });
    }
    private static void setupSoilClimateManagerSaveAndCancelButtons(SectionsEntity section, boolean isEdit) {
        CRUD.addErrorMessageAndAddButton();
        CRUD.cancelButton.setVisible(true);
        CRUD.addButton.setText("Save selected");
        CRUD.addButton.setOnAction(event -> {
            logger.info("Saving selected soil/climate/manager...");
            showSectionsRecordsList();
            if (isEdit) {
                showSectionEditForm(section);
            } else {
                showEmptyForm();
            }
        });
    }
}
