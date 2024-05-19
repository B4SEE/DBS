package cs.cvut.fel.dbs.main.tables.researches;

import cs.cvut.fel.dbs.entities.ResearchesEntity;
import cs.cvut.fel.dbs.entities.ScientistsEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.RecordsController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;

public class ResearchesView {
    private static final Logger logger = LogManager.getLogger(ResearchesView.class);
    public static void showResearchesRecordsList() {
        CRUD.showCRUDScene();
        CRUD.recordsGridTitle.setText("Researches");

        logger.info("Initializing researches records list...");

        int count = CRUD.recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;

        for (ResearchesEntity research : ResearchesDAO.getAllResearches()) {
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(research.getResearchName());
            GridPane.setRowIndex(recordGrid, count++);
            recordsController.infoButton.setOnAction(event -> {
                ResearchesDAO.clearAll();
                showResearchInfo(research);
            });
            recordsController.editButton.setOnAction(event -> {
                ResearchesDAO.clearAll();
                showResearchEditForm(research);
            });
            recordsController.deleteButton.setOnAction(event -> {
                ResearchesDAO.deleteResearch(research);
                showResearchesRecordsList();
            });
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }
        CRUD.addErrorMessageAndAddButton();
        CRUD.addButton.setOnAction(event -> {
            ResearchesDAO.clearAll();
            showEmptyForm();
        });
    }
    public static void showResearchInfo(ResearchesEntity research) {
        ResearchesDAO.clearAll();
        CRUD.recordsFormGrid.getChildren().clear();

        ResearchesDAO.researchesFormController.fillFormFields(research);

        ResearchesDAO.researchesFormController.getResearchNameField().setEditable(false);

        Label formTitle = new Label("Instance Info");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, ResearchesDAO.researchesFormController, 1);

        CRUD.recordsFormGrid.add(ResearchesDAO.researchesFormController.getScientistLabel(), 0, count++);
        Label scientist = new Label(research.getScientistId().getPersonId().getFirstName() + " " + research.getScientistId().getPersonId().getLastName());
        CRUD.recordsFormGrid.add(scientist, 0, count);
    }
    public static void showResearchEditForm(ResearchesEntity research) {
        CRUD.recordsFormGrid.getChildren().clear();

        ResearchesDAO.researchesFormController.fillFormFields(research);

        Label formTitle = new Label("Edit Instance");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, ResearchesDAO.researchesFormController, 1);

        CRUD.recordsFormGrid.add(ResearchesDAO.researchesFormController.getScientistLabel(), 0, count++);
        Label scientist = new Label(research.getScientistId().getPersonId().getFirstName() + " " + research.getScientistId().getPersonId().getLastName());
        if (ResearchesDAO.selectedScientist != null) {
            scientist.setText(ResearchesDAO.selectedScientist.getPersonId().getFirstName() + " " + ResearchesDAO.selectedScientist.getPersonId().getLastName());
        }

        CRUD.recordsFormGrid.add(scientist, 0, count);
        Button changeScientistButton = new Button("Change Scientist");
        changeScientistButton.setOnAction(event -> showAvailableScientists(research, true, ResearchesDAO.getListOfAvailableScientists(research)));
        CRUD.recordsFormGrid.add(changeScientistButton, 1, count++);

        Button submitButton = getSubmitEditFormButton(research);
        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(submitButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    public static void showEmptyForm() {
        CRUD.recordsFormGrid.getChildren().clear();

        Label formTitle = new Label("Add Plant");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);

        int count = setFormFields(CRUD.recordsFormGrid, ResearchesDAO.researchesFormController, 1);

        CRUD.recordsFormGrid.add(ResearchesDAO.researchesFormController.getScientistLabel(), 0, count++);
        Label scientist = new Label("Please select a scientist");
        Button changeScientistButton = new Button("Select Scientist");
        if (ResearchesDAO.selectedScientist != null) {
            scientist.setText(ResearchesDAO.selectedScientist.getPersonId().getFirstName() + " " + ResearchesDAO.selectedScientist.getPersonId().getLastName());
            changeScientistButton.setText("Change Scientist");
        }

        CRUD.recordsFormGrid.add(scientist, 0, count);
        changeScientistButton.setOnAction(event -> showAvailableScientists(null, false, ResearchesDAO.getListOfAvailableScientists(null)));
        CRUD.recordsFormGrid.add(changeScientistButton, 1, count++);

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> ResearchesController.addNewResearch());

        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(addButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    private static Button getSubmitEditFormButton(ResearchesEntity research) {
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> ResearchesController.editResearch(research));
        return submitButton;
    }
    private static Button getCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            ResearchesDAO.clearAll();
            showResearchesRecordsList();
        });
        return cancelButton;
    }
    private static int setFormFields(GridPane formGrid, ResearchesFormController researchesFormController, int count) {
        formGrid.add(new Label("Research Name"), 0, count);
        formGrid.add(researchesFormController.getResearchNameField(), 1, count++);
        return count;
    }
    protected static void showAvailableScientists(ResearchesEntity research, boolean isEdit, List<ScientistsEntity> availableScientists) {
        CRUD.recordsGrid.getChildren().clear();
        CRUD.showCRUDScene();
        if (research != null) {
            showResearchEditForm(research);
        } else {
            showEmptyForm();
        }

        int countPlantTypes = 0;
        for (ScientistsEntity scientist : availableScientists) {
            if (scientist == null || scientist.getPersonId() == null || scientist.getPersonId().getFirstName() == null || scientist.getPersonId().getFirstName().isEmpty()) {
                continue;
            }

            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(scientist.getPersonId().getFirstName() + " " + scientist.getPersonId().getLastName());
            GridPane.setRowIndex(recordGrid, countPlantTypes++);

            recordsController.infoButton.setOnAction(event -> {
                if (ResearchesDAO.selectedScientist != null && ResearchesDAO.selectedScientist.equals(scientist)) {
                    ResearchesController.unselectScientist();
                    recordsController.recordInfo.setStyle("");
                    recordsController.infoButton.setText("Select");
                } else {
                    if (ResearchesDAO.selectedScientist != null) {
                        CRUD.showWarningMessage("You can select only one scientist.");
                        return;
                    }
                    CRUD.clearMessage();
                    ResearchesController.selectScientist(scientist);
                    recordsController.recordInfo.setStyle("-fx-background-color: #ffae00");
                    recordsController.infoButton.setText("Unselect");
                }
            });

            recordsController.infoButton.setText("Select");
            recordsController.editButton.setVisible(false);
            recordsController.deleteButton.setVisible(false);
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }

        setupScientistSaveAndCancelButtons(research, isEdit);
        CRUD.cancelButton.setOnAction(event -> {
            showResearchesRecordsList();
            ResearchesDAO.selectedScientist = null;
            if (research != null) {
                showResearchEditForm(research);
            } else {
                showEmptyForm();
            }
        });
    }
    private static void setupScientistSaveAndCancelButtons(ResearchesEntity research, boolean isEdit) {
        CRUD.addErrorMessageAndAddButton();
        CRUD.cancelButton.setVisible(true);
        CRUD.addButton.setText("Save selected");
        CRUD.addButton.setOnAction(event -> {
            logger.info("Saving selected soil/climate types...");
            showResearchesRecordsList();
            if (isEdit) {
                showResearchEditForm(research);
            } else {
                showEmptyForm();
            }
        });
    }
}
