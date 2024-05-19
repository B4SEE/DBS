package cs.cvut.fel.dbs.main.tables.scientists;

import cs.cvut.fel.dbs.entities.ScientistsEntity;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ScientistsFormController {
    private final TextField representsInstitutionField = new TextField();
    private final TextField titleField = new TextField();
    private final Label personLabel = new Label("Personal information: ");
    private final Label phoneNumbersLabel = new Label("Phone numbers: ");
    private final Label worksInSectionLabel = new Label("Works in section: ");

    public String getRepresentsInstitution() {
        return representsInstitutionField.getText();
    }
    public void setRepresentsInstitution(String representsInstitution) {
        representsInstitutionField.setText(representsInstitution);
    }
    public String getTitle() {
        return titleField.getText();
    }
    public void setTitle(String title) {
        titleField.setText(title);
    }
    public TextField getRepresentsInstitutionField() {
        return representsInstitutionField;
    }

    public TextField getTitleField() {
        return titleField;
    }

    public Label getPersonLabel() {
        return personLabel;
    }

    public Label getPhoneNumbersLabel() {
        return phoneNumbersLabel;
    }

    public Label getWorksInSectionLabel() {
        return worksInSectionLabel;
    }
    public void clearForm() {
        getRepresentsInstitutionField().clear();
        getTitleField().clear();
    }
    public void resetFields() {
        getRepresentsInstitutionField().setEditable(true);
        getTitleField().setEditable(true);
    }
    public void fillFormFields(ScientistsEntity scientist) {
        setRepresentsInstitution(scientist.getRepresentInstitute());
        setTitle(scientist.getTitle());
    }
}
