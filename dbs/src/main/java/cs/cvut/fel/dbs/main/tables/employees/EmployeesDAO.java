package cs.cvut.fel.dbs.main.tables.employees;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.EmployeesEntity;
import cs.cvut.fel.dbs.entities.PersonsEntity;
import cs.cvut.fel.dbs.entities.SectionsEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.persons.PersonsDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeesDAO {
    private static final Logger logger = LogManager.getLogger(EmployeesDAO.class);
    public static EmployeesEntity supervisorToDelete = null;
    protected static EmployeesFormController employeesFormController = new EmployeesFormController();
    protected static PersonsEntity selectedPerson = null;
    protected static EmployeesEntity selectedSupervisor = null;
    public static List<EmployeesEntity> getAllEmployees() {
        List<EmployeesEntity> employees = new ArrayList<>();
        try {
            String query = "SELECT * FROM employees";
            Statement statement = DatabaseConnection.getConnection().createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                EmployeesEntity employee = new EmployeesEntity();
                setEmployeeInfo(result, employee);
                employees.add(employee);
            }
        } catch (SQLException e) {
            logger.error("Error while getting all employees: " + e.getMessage());
        }
        return employees;
    }
    public static EmployeesEntity getEmployee(ResultSet employeeInfo) {
        if (employeeInfo == null) {
            return null;
        }
        EmployeesEntity employee = new EmployeesEntity();
        try {
            if (employeeInfo.next()) {
                setEmployeeInfo(employeeInfo, employee);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Error while getting employee: " + e.getMessage());
        }
        return employee;
    }
    private static void setEmployeeInfo(ResultSet employeeInfo, EmployeesEntity employee) {
        try {
            employee.setIdEmployee(employeeInfo.getInt("id_employee"));
            employee.setEmployeeNumber(employeeInfo.getString("employee_number"));
            // get person
            String personQuery = "SELECT * FROM persons WHERE id_person = " + employeeInfo.getInt("person_id");
            Statement personStatement = DatabaseConnection.getConnection().createStatement();
            ResultSet personResult = personStatement.executeQuery(personQuery);
            employee.setPerson(PersonsDAO.getPerson(personResult));
            // get is supervised by
            String supervisorQuery = "SELECT * FROM employees WHERE id_employee = " + employeeInfo.getInt("is_supervised_by");
            Statement supervisorStatement = DatabaseConnection.getConnection().createStatement();
            ResultSet supervisorResult = supervisorStatement.executeQuery(supervisorQuery);
            employee.setIsSupervisedBy(getEmployee(supervisorResult));
        } catch (SQLException e) {
            logger.error("Error while setting employee info: " + e.getMessage());
        }
    }
    protected static boolean checkEmployeeCodeIsUnique(EmployeesEntity employee) {
        try {
            EntityManager entityManager = DatabaseConnection.getEntityManager();
            List<EmployeesEntity> employees = entityManager.createQuery("SELECT e FROM EmployeesEntity e WHERE e.employeeNumber = :employeeNumber AND e.idEmployee != :idEmployee", EmployeesEntity.class)
                    .setParameter("employeeNumber", employee.getEmployeeNumber())
                    .setParameter("idEmployee", employee.getIdEmployee())
                    .getResultList();

            return employees.isEmpty();
        } catch (Exception e) {
            logger.error("Error while checking employee code uniqueness: " + e.getMessage());
            return false;
        }
    }
    protected static boolean checkEmployeeNumberFormat(String employeeNumber) {
        //check format employee_number ~ '^E[0-9]{6}$'
        logger.info("Checking employee number format: " + employeeNumber);
        logger.info("Result: " + employeeNumber.matches("^E[0-9]{6}$"));
        return employeeNumber.matches("^E[0-9]{6}$");
    }

    public static void updateEmployee(EmployeesEntity employee) {
        employee.setEmployeeNumber(employeesFormController.getEmployeeId());
    }
    protected static void deleteEmployee(EmployeesEntity employee) {
        try {
            // check if employee handles section, if yes return
            EntityManager entityManager = DatabaseConnection.getEntityManager();
            List<SectionsEntity> sections = entityManager.createQuery("SELECT s FROM SectionsEntity s WHERE s.isHandledBy = :employee", SectionsEntity.class)
                    .setParameter("employee", employee)
                    .getResultList();
            if (!sections.isEmpty()) {
                CRUD.showErrorMessage("Cannot delete employee, because he/she handles section.");
                return;
            }

            // check if employee is supervisor, if yes, set for all employees supervised by this employee null
            List<EmployeesEntity> employees = entityManager.createQuery("SELECT e FROM EmployeesEntity e WHERE e.isSupervisedBy = :employee", EmployeesEntity.class)
                    .setParameter("employee", employee)
                    .getResultList();

            entityManager.getTransaction().begin();
            for (EmployeesEntity e : employees) {
                e.setIsSupervisedBy(null);
                entityManager.persist(e);
            }
            entityManager.getTransaction().commit();

            // delete employee
            entityManager.getTransaction().begin();
            EmployeesEntity managedEmployee = entityManager.merge(employee);
            entityManager.remove(managedEmployee);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error while deleting employee: " + e.getMessage());
        }
    }

    public static void clearAll() {
        selectedPerson = null;
        selectedSupervisor = null;
        supervisorToDelete = null;
        employeesFormController.clearForm();
        employeesFormController.resetFields();
    }
    public static List<EmployeesEntity> getListOfAvailableSupervisors(EmployeesEntity employee) {
        //select all except current employee and employees who are supervised by current employee
        StringBuilder query = new StringBuilder("SELECT * FROM employees WHERE id_employee != ");
        if (employee != null) {
            query.append(employee.getIdEmployee());
            if (employee.getIsSupervisedBy() != null) {
                query.append(" AND id_employee != ").append(employee.getIsSupervisedBy().getIdEmployee());
            }
            query.append(" AND id_employee NOT IN (SELECT id_employee FROM employees WHERE is_supervised_by = ").append(employee.getIdEmployee()).append(")");
        } else {
            query.append(selectedSupervisor.getIdEmployee());
            if (selectedSupervisor.getIsSupervisedBy() != null) {
                query.append(" AND id_employee != ").append(selectedSupervisor.getIsSupervisedBy().getIdEmployee());
            }
            query.append(" AND id_employee NOT IN (SELECT id_employee FROM employees WHERE is_supervised_by = ").append(selectedSupervisor.getIdEmployee()).append(")");
        }
        List<EmployeesEntity> availableSupervisors = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query.toString());
            while (!result.isAfterLast()) {
                availableSupervisors.add(getEmployee(result));
            }
        } catch (Exception e) {
            logger.error("Failed to load available supervisors: " + e.getMessage());
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    logger.error("Failed to close result set: " + e.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error("Failed to close statement: " + e.getMessage());
                }
            }
        }
        return availableSupervisors;
    }
    public static List<PersonsEntity> getListOfAvailablePersons(EmployeesEntity employee) {
        StringBuilder query = new StringBuilder("SELECT * FROM persons WHERE id_person NOT IN (SELECT person_id FROM scientists UNION SELECT person_id FROM employees)");
        if (employee != null) {
            query.append(" AND id_person != ");
            if (selectedPerson != null) {
                query.append(selectedPerson.getIdPerson());
                logger.info("Query: " + query);
            } else {
                query.append(employee.getPerson().getIdPerson());
                logger.info("Query: " + query);
            }
        }
        List<PersonsEntity> availablePersons = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query.toString());
            while (!result.isAfterLast()) {
                availablePersons.add(PersonsDAO.getPerson(result));
            }
        } catch (Exception e) {
            logger.error("Failed to load available persons: " + e.getMessage());
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    logger.error("Failed to close result set: " + e.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error("Failed to close statement: " + e.getMessage());
                }
            }
        }
        return availablePersons;
    }
}
