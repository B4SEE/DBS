package cs.cvut.fel.dbs.main.tables.employees;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.EmployeesEntity;
import cs.cvut.fel.dbs.entities.PersonsEntity;
import cs.cvut.fel.dbs.main.CRUD;

import javax.persistence.EntityManager;

public class EmployeesController {
    protected static boolean checkForm(EmployeesFormController form, EmployeesEntity employee) {
        CRUD.showErrorMessage("");
        if (form.getEmployeeId().isEmpty()) {
            CRUD.showErrorMessage("Employee ID cannot be empty.");
            return false;
        }
        if (EmployeesDAO.selectedPerson == null) {
            if (employee == null || employee.getPerson() == null) {
                CRUD.showErrorMessage("No person selected.");
            }
        }
        return CRUD.isErrorMessageEmpty();
    }
    protected static void addNewEmployee() {
        try {
            if (!checkForm(EmployeesDAO.employeesFormController, null)) {
                return;
            }
            EmployeesEntity employee = new EmployeesEntity();
            EmployeesDAO.updateEmployee(employee);

            if (EmployeesDAO.selectedPerson != null) {
                employee.setPerson(EmployeesDAO.selectedPerson);
            }
            if (EmployeesDAO.selectedSupervisor != null) {
                employee.setIsSupervisedBy(EmployeesDAO.selectedSupervisor);
            }
            if (EmployeesDAO.supervisorToDelete != null) {
                if (employee.getIsSupervisedBy() != null && employee.getIsSupervisedBy().getIdEmployee() == EmployeesDAO.supervisorToDelete.getIdEmployee()) {
                    employee.setIsSupervisedBy(null);
                }
            }

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(employee);
            entityManager.getTransaction().commit();

            EmployeesView.showEmployeesRecordsList();
            EmployeesView.showEmployeeInfo(employee);
            return;
        } catch (Exception e) {
            EmployeesView.showEmployeesRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        EmployeesDAO.clearAll();
    }
    protected static void editEmployee(EmployeesEntity employee) {
        try {
            if (!checkForm(EmployeesDAO.employeesFormController, employee)) {
                return;
            }
            EmployeesDAO.updateEmployee(employee);

            if (EmployeesDAO.selectedPerson != null) {
                employee.setPerson(EmployeesDAO.selectedPerson);
            }
            if (EmployeesDAO.selectedSupervisor != null) {
                employee.setIsSupervisedBy(EmployeesDAO.selectedSupervisor);
            }
            if (EmployeesDAO.supervisorToDelete != null) {
                if (employee.getIsSupervisedBy() != null && employee.getIsSupervisedBy().getIdEmployee() == EmployeesDAO.supervisorToDelete.getIdEmployee()) {
                    employee.setIsSupervisedBy(null);
                }
            }

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(employee);
            entityManager.getTransaction().commit();

            EmployeesView.showEmployeesRecordsList();
            EmployeesView.showEmployeeInfo(employee);
            return;
        } catch (Exception e) {
            EmployeesView.showEmployeesRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        EmployeesDAO.clearAll();
    }
    protected static void selectPerson(PersonsEntity person) {
        EmployeesDAO.selectedPerson = person;
    }
    protected static void unselectPerson() {
        EmployeesDAO.selectedPerson = null;
    }
    protected static void selectSupervisor(EmployeesEntity supervisor) {
        EmployeesDAO.selectedSupervisor = supervisor;
    }
    protected static void unselectSupervisor() {
        EmployeesDAO.selectedSupervisor = null;
    }
    protected static void deleteSupervisor(EmployeesEntity supervisor) {
        EmployeesDAO.supervisorToDelete = supervisor;
    }
    protected static void cancelDeleteSupervisor() {
        EmployeesDAO.supervisorToDelete = null;
    }
}
