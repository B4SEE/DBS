package cs.cvut.fel.dbs.main.tables.employees;

import cs.cvut.fel.dbs.entities.EmployeesEntity;
import cs.cvut.fel.dbs.entities.PersonsEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.RecordsController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;

public class EmployeesView {
    private static final Logger logger = LogManager.getLogger(EmployeesView.class);
    public static void showEmployeesRecordsList() {
        CRUD.showCRUDScene();
        CRUD.recordsGridTitle.setText("Employees Records");

        logger.info("Initializing employees records list...");

        int count = CRUD.recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;

        for (EmployeesEntity employee : EmployeesDAO.getAllEmployees()) {
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(employee.getPerson().getFirstName() + " " + employee.getPerson().getLastName());
            GridPane.setRowIndex(recordGrid, count++);
            recordsController.infoButton.setOnAction(event -> {
                EmployeesDAO.clearAll();
                showEmployeeInfo(employee);
            });
            recordsController.editButton.setOnAction(event -> {
                EmployeesDAO.clearAll();
                showEmployeeEditForm(employee);
            });
            recordsController.deleteButton.setOnAction(event -> {
                EmployeesDAO.deleteEmployee(employee);
                showEmployeesRecordsList();
            });
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }
        CRUD.addErrorMessageAndAddButton();
        CRUD.addButton.setOnAction(event -> {
            EmployeesDAO.clearAll();
            showEmptyForm();
        });
    }
    public static void showEmployeeInfo(EmployeesEntity employee) {
        EmployeesDAO.clearAll();
        CRUD.recordsFormGrid.getChildren().clear();

        EmployeesDAO.employeesFormController.fillFormFields(employee);

        EmployeesDAO.employeesFormController.getEmployeeIdField().setEditable(false);

        Label formTitle = new Label("Scientist Info");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, EmployeesDAO.employeesFormController, 1);

        CRUD.recordsFormGrid.add(EmployeesDAO.employeesFormController.getPersonLabel(), 0, count++);
        Label scientistName = new Label(employee.getPerson().getFirstName() + " " + employee.getPerson().getLastName());
        Label scientistAddress = new Label(employee.getPerson().getAddressId().getCity() + ", " + employee.getPerson().getAddressId().getStreet() + " " + employee.getPerson().getAddressId().getHouseNumber());
        CRUD.recordsFormGrid.add(scientistName, 0, count++);
        CRUD.recordsFormGrid.add(scientistAddress, 0, count++);

        CRUD.recordsFormGrid.add(EmployeesDAO.employeesFormController.getPhoneNumbersLabel(), 0, count++);
        for (String phoneNumber : employee.getPerson().getPhoneNumbersList()) {
            Label phoneNumberLabel = new Label(phoneNumber);
            CRUD.recordsFormGrid.add(phoneNumberLabel, 0, count++);
        }

        CRUD.recordsFormGrid.add(EmployeesDAO.employeesFormController.getIsSupervisedByLabel(), 0, count++);
        Label sectionName = new Label("No supervisor");
        if (employee.getIsSupervisedBy() != null) {
            if (employee.getIsSupervisedBy().getPerson().getFirstName() != null && !employee.getIsSupervisedBy().getPerson().getFirstName().isEmpty()) {
                sectionName.setText(employee.getIsSupervisedBy().getPerson().getFirstName() + " " + employee.getIsSupervisedBy().getPerson().getLastName());
            }
        }
        CRUD.recordsFormGrid.add(sectionName, 0, count);
    }
    public static void showEmployeeEditForm(EmployeesEntity employee) {
        CRUD.recordsFormGrid.getChildren().clear();

        EmployeesDAO.employeesFormController.fillFormFields(employee);

        Label formTitle = new Label("Edit Instance");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(CRUD.recordsFormGrid, EmployeesDAO.employeesFormController, 1);

        CRUD.recordsFormGrid.add(EmployeesDAO.employeesFormController.getPersonLabel(), 0, count++);
        Label scientistName = new Label(employee.getPerson().getFirstName() + " " + employee.getPerson().getLastName());
        if (EmployeesDAO.selectedPerson != null) {
            scientistName.setText(EmployeesDAO.selectedPerson.getFirstName() + " " + EmployeesDAO.selectedPerson.getLastName());
        }

        CRUD.recordsFormGrid.add(scientistName, 0, count);
        Button changePlantButton = new Button("Change Personal Info");
        changePlantButton.setOnAction(event -> showAvailablePersons(employee, true, EmployeesDAO.getListOfAvailablePersons(employee)));
        CRUD.recordsFormGrid.add(changePlantButton, 1, count++);

        CRUD.recordsFormGrid.add(EmployeesDAO.employeesFormController.getIsSupervisedByLabel(), 0, count++);

        Button changeSectionButton = new Button("Change Section");
        changeSectionButton.setOnAction(event -> showAvailableSupervisors(employee, true, EmployeesDAO.getListOfAvailableSupervisors(employee)));

        Label supervisorName = new Label("No supervisor");
        if (employee.getIsSupervisedBy() != null || EmployeesDAO.selectedSupervisor != null) {
            if (EmployeesDAO.selectedSupervisor != null) {
                supervisorName.setText(EmployeesDAO.selectedSupervisor.getPerson().getFirstName() + " " + EmployeesDAO.selectedSupervisor.getPerson().getLastName());
                Button deleteSectionButton = getDeleteSupervisorButton(EmployeesDAO.selectedSupervisor, supervisorName);
                CRUD.recordsFormGrid.add(deleteSectionButton, 2, count);
            } else if (employee.getIsSupervisedBy().getPerson().getFirstName() != null && !employee.getIsSupervisedBy().getPerson().getFirstName().isEmpty()) {
                supervisorName.setText(employee.getIsSupervisedBy().getPerson().getFirstName() + " " + employee.getIsSupervisedBy().getPerson().getLastName());
                Button deleteSectionButton = getDeleteSupervisorButton(employee.getIsSupervisedBy(), supervisorName);
                CRUD.recordsFormGrid.add(deleteSectionButton, 2, count);
            }
        }
        CRUD.recordsFormGrid.add(supervisorName, 0, count);
        CRUD.recordsFormGrid.add(changeSectionButton, 1, count++);

        Button submitButton = getSubmitEditFormButton(employee);
        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(submitButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    public static void showEmptyForm() {
        CRUD.recordsFormGrid.getChildren().clear();

        Label formTitle = new Label("Add Plant");
        CRUD.recordsFormGrid.add(formTitle, 0, 0);

        int count = setFormFields(CRUD.recordsFormGrid, EmployeesDAO.employeesFormController, 1);

        CRUD.recordsFormGrid.add(EmployeesDAO.employeesFormController.getPersonLabel(), 0, count++);
        Label personName = new Label("Please select a person");
        Button changePersonButton = new Button("Select Person");

        if (EmployeesDAO.selectedPerson != null) {
            personName.setText(EmployeesDAO.selectedPerson.getFirstName() + " " + EmployeesDAO.selectedPerson.getLastName());
            changePersonButton.setText("Change Person");
        }

        CRUD.recordsFormGrid.add(personName, 0, count);
        changePersonButton.setOnAction(event -> showAvailablePersons(null, false, EmployeesDAO.getListOfAvailablePersons(null)));
        CRUD.recordsFormGrid.add(changePersonButton, 1, count++);

        CRUD.recordsFormGrid.add(EmployeesDAO.employeesFormController.getIsSupervisedByLabel(), 0, count++);
        Label supervisorName = new Label("Select a supervisor");
        Button changeSectionButton = new Button("Select Supervisor");
        if (EmployeesDAO.selectedSupervisor != null) {
            supervisorName.setText(EmployeesDAO.selectedSupervisor.getPerson().getFirstName() + " " + EmployeesDAO.selectedSupervisor.getPerson().getLastName());
            changeSectionButton.setText("Change Supervisor");
        }
        CRUD.recordsFormGrid.add(supervisorName, 0, count);
        changeSectionButton.setOnAction(event -> showAvailableSupervisors(null, false, EmployeesDAO.getListOfAvailableSupervisors(null)));
        CRUD.recordsFormGrid.add(changeSectionButton, 1, count++);

        Button addButton = new Button("Add");
        addButton.setOnAction(event -> EmployeesController.addNewEmployee());

        Button cancelButton = getCancelButton();

        CRUD.recordsFormGrid.add(addButton, 0, count);
        CRUD.recordsFormGrid.add(cancelButton, 1, count);
    }
    private static Button getSubmitEditFormButton(EmployeesEntity employee) {
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> EmployeesController.editEmployee(employee));
        return submitButton;
    }
    private static Button getCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            EmployeesDAO.clearAll();
            showEmployeesRecordsList();
        });
        return cancelButton;
    }
    private static int setFormFields(GridPane formGrid, EmployeesFormController employeesFormController, int count) {
        formGrid.add(new Label("Employee ID"), 0, count++);
        formGrid.add(employeesFormController.getEmployeeIdField(), 0, count++);
        return count;
    }
    protected static void showAvailablePersons(EmployeesEntity employee, boolean isEdit, List<PersonsEntity> availablePersons) {
        CRUD.recordsGrid.getChildren().clear();
        CRUD.showCRUDScene();
        if (employee != null) {
            showEmployeeEditForm(employee);
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
                if (EmployeesDAO.selectedPerson != null && EmployeesDAO.selectedPerson.equals(person)) {
                    EmployeesController.unselectPerson();
                    recordsController.recordInfo.setStyle("");
                    recordsController.infoButton.setText("Select");
                } else {
                    if (EmployeesDAO.selectedPerson != null) {
                        CRUD.showWarningMessage("You can select only one plant type.");
                        return;
                    }
                    CRUD.clearMessage();
                    EmployeesController.selectPerson(person);
                    recordsController.recordInfo.setStyle("-fx-background-color: #ffae00");
                    recordsController.infoButton.setText("Unselect");
                }
            });

            recordsController.infoButton.setText("Select");
            recordsController.editButton.setVisible(false);
            recordsController.deleteButton.setVisible(false);
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }

        setupPersonSectionSaveAndCancelButtons(employee, isEdit);
        CRUD.cancelButton.setOnAction(event -> {
            showEmployeesRecordsList();
            EmployeesDAO.selectedPerson = null;
            if (employee != null) {
                showEmployeeEditForm(employee);
            } else {
                showEmptyForm();
            }
        });
    }
    protected static void showAvailableSupervisors(EmployeesEntity employee, boolean isEdit, List<EmployeesEntity> availableSupervisors) {
        CRUD.recordsGrid.getChildren().clear();
        CRUD.showCRUDScene();
        if (employee != null) {
            showEmployeeEditForm(employee);
        } else {
            showEmptyForm();
        }

        int countSupervisors = 0;
        for (EmployeesEntity supervisor : availableSupervisors) {
            if (supervisor == null || supervisor.getPerson() == null || supervisor.getPerson().getFirstName() == null || supervisor.getPerson().getFirstName().isEmpty()) {
                continue;
            }
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(supervisor.getPerson().getFirstName() + " " + supervisor.getPerson().getLastName());
            GridPane.setRowIndex(recordGrid, countSupervisors++);
            recordsController.recordInfo.setText(supervisor.getPerson().getFirstName() + " " + supervisor.getPerson().getLastName());

            recordsController.infoButton.setOnAction(event -> {
                if (EmployeesDAO.selectedSupervisor != null && EmployeesDAO.selectedSupervisor.equals(supervisor)) {
                    EmployeesController.unselectSupervisor();
                    recordsController.recordInfo.setStyle("");
                    recordsController.infoButton.setText("Select");
                } else {
                    if (EmployeesDAO.selectedSupervisor != null) {
                        CRUD.showWarningMessage("You can select only one section.");
                        return;
                    }
                    CRUD.clearMessage();
                    EmployeesController.selectSupervisor(supervisor);
                    recordsController.recordInfo.setStyle("-fx-background-color: #ffae00");
                    recordsController.infoButton.setText("Unselect");
                }
            });

            recordsController.infoButton.setText("Select");
            recordsController.editButton.setVisible(false);
            recordsController.deleteButton.setVisible(false);
            CRUD.recordsGrid.getChildren().add(recordGrid);
        }

        setupPersonSectionSaveAndCancelButtons(employee, isEdit);
        CRUD.cancelButton.setOnAction(event -> {
            showEmployeesRecordsList();
            EmployeesDAO.selectedSupervisor = null;
            if (employee != null) {
                showEmployeeEditForm(employee);
            } else {
                showEmptyForm();
            }
        });
    }
    private static void setupPersonSectionSaveAndCancelButtons(EmployeesEntity employee, boolean isEdit) {
        CRUD.addErrorMessageAndAddButton();
        CRUD.cancelButton.setVisible(true);
        CRUD.addButton.setText("Save selected");
        CRUD.addButton.setOnAction(event -> {
            logger.info("Saving selected soil/climate types...");
            showEmployeesRecordsList();
            if (isEdit) {
                showEmployeeEditForm(employee);
            } else {
                showEmptyForm();
            }
        });
    }
    private static Button getDeleteSupervisorButton(EmployeesEntity supervisor, Label label) {
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            if (EmployeesDAO.supervisorToDelete != null && EmployeesDAO.supervisorToDelete.equals(supervisor)) {
                label.setStyle("");
                EmployeesController.cancelDeleteSupervisor();
            } else {
                label.setStyle("-fx-background-color: #af0303");
                EmployeesController.deleteSupervisor(supervisor);
            }
        });
        return deleteButton;
    }
}
