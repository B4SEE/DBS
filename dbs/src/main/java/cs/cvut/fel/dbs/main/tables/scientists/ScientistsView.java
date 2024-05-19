package cs.cvut.fel.dbs.main.tables.scientists;

import cs.cvut.fel.dbs.entities.PersonsEntity;
import cs.cvut.fel.dbs.entities.ScientistsEntity;
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

public class ScientistsView {
    private static final Logger logger = LogManager.getLogger(ScientistsView.class);
    public static void showScientistsRecordsList() {
        CRUD.showCRUDScene();
        CRUD.recordsGridTitle.setText("Scientists");

        logger.info("Initializing scientists records list...");

        int count = CRUD.recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;

        for (ScientistsEntity scientist : ScientistsDAO.getAllScientists()) {
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(scientist.getPersonId().getFirstName() + " " + scientist.getPersonId().getLastName());
            GridPane.setRowIndex(recordGrid, count++);
            recordsController.infoButton.setOnAction(event -> {
                ScientistsDAO.clearAll();
                showScientistInfo(scientist);
            });
            recordsController.editButton.setOnAction(event -> {
                ScientistsDAO.clearAll();
                showScientistEditForm(scientist);
            });
            recordsController.deleteButton.setOnAction(event -> {
                ScientistsDAO.deleteScientist(scientist);
                showScientistsRecordsList();
            });
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }
        CRUD.addErrorMessageAndAddButton();
        CRUD.addButton.setOnAction(event -> {
            ScientistsDAO.clearAll();
            showEmptyForm();
        });
    }
    public static void showScientistInfo(ScientistsEntity scientist) {
        ScientistsDAO.clearAll();
        CRUD.recordsFormGrid.getChildren().clear();

        ScientistsDAO.scientistsFormController.fillFormFields(scientist);

        ScientistsDAO.scientistsFormController.getRepresentsInstitutionField().setEditable(false);
        ScientistsDAO.scientistsFormController.getTitleField().setEditable(false);

        Label formTitle = new Label("Scientist Info");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, ScientistsDAO.scientistsFormController, 1);

        CRUD.recordsFormGrid.add(ScientistsDAO.scientistsFormController.getPersonLabel(), 0, count++);
        Label scientistName = new Label(scientist.getPersonId().getFirstName() + " " + scientist.getPersonId().getLastName());
        Label scientistAddress = new Label(scientist.getPersonId().getAddressId().getCity() + ", " + scientist.getPersonId().getAddressId().getStreet() + " " + scientist.getPersonId().getAddressId().getHouseNumber());
        CRUD.recordsFormGrid.add(scientistName, 0, count++);
        CRUD.recordsFormGrid.add(scientistAddress, 0, count++);

        CRUD.recordsFormGrid.add(ScientistsDAO.scientistsFormController.getPhoneNumbersLabel(), 0, count++);
        for (String phoneNumber : scientist.getPersonId().getPhoneNumbersList()) {
            Label phoneNumberLabel = new Label(phoneNumber);
            CRUD.recordsFormGrid.add(phoneNumberLabel, 0, count++);
        }

        CRUD.recordsFormGrid.add(ScientistsDAO.scientistsFormController.getWorksInSectionLabel(), 0, count++);
        Label sectionName = new Label("No section selected");
        if (scientist.getWorksInSection() != null) {
            if (scientist.getWorksInSection().getSectionName() != null && !scientist.getWorksInSection().getSectionName().isEmpty()) {
                sectionName.setText(scientist.getWorksInSection().getSectionName());
                logger.info("Section: " + scientist.getWorksInSection().getSectionName());
            }
        }
        CRUD.recordsFormGrid.add(sectionName, 0, count);
    }
    public static void showScientistEditForm(ScientistsEntity scientist) {
        CRUD.recordsFormGrid.getChildren().clear();

        ScientistsDAO.scientistsFormController.fillFormFields(scientist);

        Label formTitle = new Label("Edit Scientist");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, ScientistsDAO.scientistsFormController, 1);

        CRUD.recordsFormGrid.add(ScientistsDAO.scientistsFormController.getPersonLabel(), 0, count++);
        Label scientistName = new Label(scientist.getPersonId().getFirstName() + " " + scientist.getPersonId().getLastName());
        if (ScientistsDAO.selectedPerson != null) {
            scientistName.setText(ScientistsDAO.selectedPerson.getFirstName() + " " + ScientistsDAO.selectedPerson.getLastName());
        }

        CRUD.recordsFormGrid.add(scientistName, 0, count);
        Button changePlantButton = new Button("Change Personal Info");
        changePlantButton.setOnAction(event -> showAvailablePersons(scientist, true, ScientistsDAO.getListOfAvailablePersons(scientist)));
        CRUD.recordsFormGrid.add(changePlantButton, 1, count++);

        CRUD.recordsFormGrid.add(ScientistsDAO.scientistsFormController.getWorksInSectionLabel(), 0, count++);

        Button changeSectionButton = new Button("Change Section");
        changeSectionButton.setOnAction(event -> showAvailableSections(scientist, true, ScientistsDAO.getListOfAvailableSections(scientist)));

        Label sectionName = new Label("No section selected");
        if (scientist.getWorksInSection() != null || ScientistsDAO.selectedSection != null) {
            if (ScientistsDAO.selectedSection != null) {
                sectionName.setText(ScientistsDAO.selectedSection.getSectionName());
                Button deleteSectionButton = getDeleteSectionButton(ScientistsDAO.selectedSection, sectionName);
                CRUD.recordsFormGrid.add(deleteSectionButton, 2, count);
            } else if (scientist.getWorksInSection().getSectionName() != null && !scientist.getWorksInSection().getSectionName().isEmpty()) {
                sectionName.setText(scientist.getWorksInSection().getSectionName());
                Button deleteSectionButton = getDeleteSectionButton(scientist.getWorksInSection(), sectionName);
                CRUD.recordsFormGrid.add(deleteSectionButton, 2, count);
            }
        }
        CRUD.recordsFormGrid.add(sectionName, 0, count);
        CRUD.recordsFormGrid.add(changeSectionButton, 1, count++);

        Button submitButton = getSubmitEditFormButton(scientist);
        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(submitButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    public static void showEmptyForm() {
        CRUD.recordsFormGrid.getChildren().clear();

        Label formTitle = new Label("Add Plant");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);

        int count = setFormFields(CRUD.recordsFormGrid, ScientistsDAO.scientistsFormController, 1);

        CRUD.recordsFormGrid.add(ScientistsDAO.scientistsFormController.getPersonLabel(), 0, count++);
        Label personName = new Label("Please select a person");
        Button changePersonButton = new Button("Select Person");

        if (ScientistsDAO.selectedPerson != null) {
            personName.setText(ScientistsDAO.selectedPerson.getFirstName() + " " + ScientistsDAO.selectedPerson.getLastName());
            changePersonButton.setText("Change Person");
        }

        CRUD.recordsFormGrid.add(personName, 0, count);
        changePersonButton.setOnAction(event -> showAvailablePersons(null, false, ScientistsDAO.getListOfAvailablePersons(null)));
        CRUD.recordsFormGrid.add(changePersonButton, 1, count++);

        CRUD.recordsFormGrid.add(ScientistsDAO.scientistsFormController.getWorksInSectionLabel(), 0, count++);
        Label sectionName = new Label("Select a section");
        Button changeSectionButton = new Button("Select Section");
        if (ScientistsDAO.selectedSection != null) {
            sectionName.setText(ScientistsDAO.selectedSection.getSectionName());
            changeSectionButton.setText("Change Section");
        }
        CRUD.recordsFormGrid.add(sectionName, 0, count);
        changeSectionButton.setOnAction(event -> showAvailableSections(null, false, ScientistsDAO.getListOfAvailableSections(null)));
        CRUD.recordsFormGrid.add(changeSectionButton, 1, count++);

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> ScientistsController.addNewScientist());

        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(addButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    private static Button getSubmitEditFormButton(ScientistsEntity scientist) {
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> ScientistsController.editScientist(scientist));
        return submitButton;
    }
    private static Button getCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            ScientistsDAO.clearAll();
            showScientistsRecordsList();
        });
        return cancelButton;
    }
    private static int setFormFields(GridPane formGrid, ScientistsFormController scientistsFormController, int count) {
        formGrid.add(new Label("Represents Institution"), 0, count++);
        formGrid.add(scientistsFormController.getRepresentsInstitutionField(), 0, count++);
        formGrid.add(new Label("Title"), 0, count++);
        formGrid.add(scientistsFormController.getTitleField(), 0, count++);
        return count;
    }
    protected static void showAvailablePersons(ScientistsEntity scientist, boolean isEdit, List<PersonsEntity> availablePersons) {
        CRUD.recordsGrid.getChildren().clear();
        CRUD.showCRUDScene();
        if (scientist != null) {
            showScientistEditForm(scientist);
        } else {
            showEmptyForm();
        }

        int countPersons = 0;
        for (PersonsEntity person : availablePersons) {
            if (person == null || person.getFirstName() == null || person.getFirstName().isEmpty()) {
                continue;
            }

            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(person.getFirstName() + " " + person.getLastName());
            GridPane.setRowIndex(recordGrid, countPersons++);

            recordsController.infoButton.setOnAction(event -> {
                if (ScientistsDAO.selectedPerson != null && ScientistsDAO.selectedPerson.equals(person)) {
                    ScientistsController.unselectPerson();
                    recordsController.recordInfo.setStyle("");
                    recordsController.infoButton.setText("Select");
                } else {
                    if (ScientistsDAO.selectedPerson != null) {
                        CRUD.showWarningMessage("You can select only one plant type.");
                        return;
                    }
                    CRUD.clearMessage();
                    ScientistsController.selectPerson(person);
                    recordsController.recordInfo.setStyle("-fx-background-color: #ffae00");
                    recordsController.infoButton.setText("Unselect");
                }
            });

            recordsController.infoButton.setText("Select");
            recordsController.editButton.setVisible(false);
            recordsController.deleteButton.setVisible(false);
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }

        setupPersonSectionSaveAndCancelButtons(scientist, isEdit);
        CRUD.cancelButton.setOnAction(event -> {
            showScientistsRecordsList();
            ScientistsDAO.selectedPerson = null;
            if (scientist != null) {
                showScientistEditForm(scientist);
            } else {
                showEmptyForm();
            }
        });
    }
    protected static void showAvailableSections(ScientistsEntity scientist, boolean isEdit, List<SectionsEntity> availableSections) {
        CRUD.recordsGrid.getChildren().clear();
        CRUD.showCRUDScene();
        if (scientist != null) {
            showScientistEditForm(scientist);
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
                if (ScientistsDAO.selectedSection != null && ScientistsDAO.selectedSection.equals(section)) {
                    ScientistsController.unselectSection();
                    recordsController.recordInfo.setStyle("");
                    recordsController.infoButton.setText("Select");
                } else {
                    if (ScientistsDAO.selectedSection != null) {
                        CRUD.showWarningMessage("You can select only one section.");
                        return;
                    }
                    CRUD.clearMessage();
                    ScientistsController.selectSection(section);
                    recordsController.recordInfo.setStyle("-fx-background-color: #ffae00");
                    recordsController.infoButton.setText("Unselect");
                }
            });

            recordsController.infoButton.setText("Select");
            recordsController.editButton.setVisible(false);
            recordsController.deleteButton.setVisible(false);
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }

        setupPersonSectionSaveAndCancelButtons(scientist, isEdit);
        CRUD.cancelButton.setOnAction(event -> {
            showScientistsRecordsList();
            ScientistsDAO.selectedSection = null;
            if (scientist != null) {
                showScientistEditForm(scientist);
            } else {
                showEmptyForm();
            }
        });
    }
    private static void setupPersonSectionSaveAndCancelButtons(ScientistsEntity scientist, boolean isEdit) {
        CRUD.addErrorMessageAndAddButton();
        CRUD.cancelButton.setVisible(true);
        CRUD.addButton.setText("Save selected");
        CRUD.addButton.setOnAction(event -> {
            logger.info("Saving selected person/section...");
            showScientistsRecordsList();
            if (isEdit) {
                showScientistEditForm(scientist);
            } else {
                showEmptyForm();
            }
        });
    }
    private static Button getDeleteSectionButton(SectionsEntity section, Label label) {
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            if (ScientistsDAO.sectionToDelete != null && ScientistsDAO.sectionToDelete.equals(section)) {
                label.setStyle("");
                ScientistsController.cancelDeleteSection();
            } else {
                label.setStyle("-fx-background-color: #af0303");
                ScientistsController.deleteSection(section);
            }
        });
        return deleteButton;
    }
}
