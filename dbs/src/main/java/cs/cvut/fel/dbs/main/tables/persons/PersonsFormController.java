package cs.cvut.fel.dbs.main.tables.persons;

import cs.cvut.fel.dbs.entities.PersonsEntity;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Date;

public class PersonsFormController {
    private final TextField personFirstNameField = new TextField();
    private final TextField personLastNameField = new TextField();
    private final DatePicker personBirthDateField = new DatePicker();
    private final Label addressLabel = new Label("Address: ");
    private final Label phoneNumbersLabel = new Label("Phone numbers: ");
    public String getPersonFirstName() {
        return personFirstNameField.getText();
    }
    public void setPersonFirstName(String personFirstName) {
        personFirstNameField.setText(personFirstName);
    }
    public String getPersonLastName() {
        return personLastNameField.getText();
    }
    public void setPersonLastName(String personLastName) {
        personLastNameField.setText(personLastName);
    }
    public Date getPersonBirthDate() {
        return Date.valueOf(personBirthDateField.getValue());
    }
    public void setPersonBirthDate(Date personBirthDate) {
        personBirthDateField.setValue(personBirthDate.toLocalDate());
    }
    public TextField getPersonFirstNameField() {
        return personFirstNameField;
    }
    public TextField getPersonLastNameField() {
        return personLastNameField;
    }
    public DatePicker getPersonBirthDateField() {
        return personBirthDateField;
    }
    public Label getAddressLabel() {
        return addressLabel;
    }
    public Label getPhoneNumbersLabel() {
        return phoneNumbersLabel;
    }

    public void clearForm() {
        getPersonFirstNameField().clear();
        getPersonLastNameField().clear();
        getPersonBirthDateField().getEditor().clear();
        getPersonBirthDateField().setValue(null);
    }
    public void resetFields() {
        getPersonFirstNameField().setEditable(true);
        getPersonLastNameField().setEditable(true);
        getPersonBirthDateField().setEditable(true);
    }
    public void fillFormFields(PersonsEntity person) {
        setPersonFirstName(person.getFirstName());
        setPersonLastName(person.getLastName());
        setPersonBirthDate(person.getDateOfBirth());
    }
}
