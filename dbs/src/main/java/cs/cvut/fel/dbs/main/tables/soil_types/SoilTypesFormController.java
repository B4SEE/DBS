package cs.cvut.fel.dbs.main.tables.soil_types;

import cs.cvut.fel.dbs.entities.SoiltypesEntity;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SoilTypesFormController {
    private final TextField soilTypeNameField = new TextField();
    private final TextArea soilTypeDescriptionField = new TextArea();
    public String getSoilTypeName() {
        return soilTypeNameField.getText();
    }
    public String getSoilTypeDescription() {
        return soilTypeDescriptionField.getText();
    }
    public void setSoilTypeName(String soilTypeName) {
        soilTypeNameField.setText(soilTypeName);
    }
    public void setSoilTypeDescription(String soilTypeDescription) {
        soilTypeDescriptionField.setText(soilTypeDescription);
    }

    public TextField getSoilTypeNameField() {
        return soilTypeNameField;
    }

    public TextArea getSoilTypeDescriptionField() {
        return soilTypeDescriptionField;
    }

    public void clearForm() {
        getSoilTypeNameField().clear();
        getSoilTypeDescriptionField().clear();
    }
    public void resetFields() {
        getSoilTypeNameField().setEditable(true);
        getSoilTypeDescriptionField().setEditable(true);
    }
    public void fillFormFields(SoiltypesEntity soilType) {
        setSoilTypeName(soilType.getSoilName());
        setSoilTypeDescription(soilType.getSoilDescription());
    }
}
