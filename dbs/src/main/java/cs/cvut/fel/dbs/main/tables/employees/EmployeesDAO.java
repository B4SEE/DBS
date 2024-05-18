package cs.cvut.fel.dbs.main.tables.employees;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.EmployeesEntity;
import cs.cvut.fel.dbs.main.tables.persons.PersonsDAO;
import cs.cvut.fel.dbs.main.tables.sections.SectionsDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeesDAO {
    private static final Logger logger = LogManager.getLogger(EmployeesDAO.class);
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
}
