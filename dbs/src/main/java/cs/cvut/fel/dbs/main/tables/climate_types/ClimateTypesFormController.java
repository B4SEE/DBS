package cs.cvut.fel.dbs.main.tables.climate_types;

import cs.cvut.fel.dbs.entities.ClimatetypesEntity;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ClimateTypesFormController {
    private final TextField climateTypeNameField = new TextField();
    private final TextArea climateTypeDescriptionField = new TextArea();
    public String getClimateTypeName() {
        return climateTypeNameField.getText();
    }
    public String getClimateTypeDescription() {
        return climateTypeDescriptionField.getText();
    }
    public void setClimateTypeName(String climateTypeName) {
        climateTypeNameField.setText(climateTypeName);
    }
    public void setClimateTypeDescription(String climateTypeDescription) {
        climateTypeDescriptionField.setText(climateTypeDescription);
    }

    public TextField getClimateTypeNameField() {
        return climateTypeNameField;
    }

    public TextArea getClimateTypeDescriptionField() {
        return climateTypeDescriptionField;
    }

    public void clearForm() {
        getClimateTypeNameField().clear();
        getClimateTypeDescriptionField().clear();
    }
    public void resetFields() {
        getClimateTypeNameField().setEditable(true);
        getClimateTypeDescriptionField().setEditable(true);
    }
    public void fillFormFields(ClimatetypesEntity climateType) {
        setClimateTypeName(climateType.getClimateName());
        setClimateTypeDescription(climateType.getClimateDescription());
    }
}
