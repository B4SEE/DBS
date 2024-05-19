package cs.cvut.fel.dbs.main.tables.employees;

import cs.cvut.fel.dbs.entities.EmployeesEntity;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EmployeesFormController {
    private final TextField employeeIdField = new TextField();
    private final Label personLabel = new Label("Personal information: ");
    private final Label phoneNumbersLabel = new Label("Phone numbers: ");
    private final Label isSupervisedByLabel = new Label("Is supervised by: ");

    public String getEmployeeId() {
        return employeeIdField.getText();
    }
    public void setEmployeeId(String employeeId) {
        employeeIdField.setText(employeeId);
    }
    public TextField getEmployeeIdField() {
        return employeeIdField;
    }
    public Label getPersonLabel() {
        return personLabel;
    }
    public Label getPhoneNumbersLabel() {
        return phoneNumbersLabel;
    }
    public Label getIsSupervisedByLabel() {
        return isSupervisedByLabel;
    }

    public void clearForm() {
        getEmployeeIdField().clear();
    }
    public void resetFields() {
        getEmployeeIdField().setEditable(true);
    }
    public void fillFormFields(EmployeesEntity employee) {
        setEmployeeId(employee.getEmployeeNumber());
    }
}
