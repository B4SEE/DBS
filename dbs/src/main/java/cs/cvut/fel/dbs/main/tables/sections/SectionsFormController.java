package cs.cvut.fel.dbs.main.tables.sections;

import cs.cvut.fel.dbs.entities.PlantsEntity;
import cs.cvut.fel.dbs.entities.SectionsEntity;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SectionsFormController {
    private final TextField sectionNameField = new TextField();
    private final TextField geographicalCoordinatesField = new TextField();
    private final TextField temperatureField = new TextField();
    private final TextField lightField = new TextField();
    private final Label soilTypeLabel = new Label("Soil Type: ");
    private final Label climateTypeLabel = new Label("Climate Type: ");
    private final Label isHandledByLabel = new Label("Is Handled By: ");

    public String getSectionName() {
        return sectionNameField.getText();
    }
    public String getGeographicalCoordinates() {
        return geographicalCoordinatesField.getText();
    }
    public int getTemperature() {
        return Integer.parseInt(temperatureField.getText());
    }
    public int getLight() {
        return Integer.parseInt(lightField.getText());
    }
    public void setSectionName(String sectionName) {
        sectionNameField.setText(sectionName);
    }
    public void setGeographicalCoordinates(String geographicalCoordinates) {
        geographicalCoordinatesField.setText(geographicalCoordinates);
    }
    public void setTemperature(int temperature) {
        temperatureField.setText(String.valueOf(temperature));
    }
    public void setLight(int light) {
        lightField.setText(String.valueOf(light));
    }

    public TextField getSectionNameField() {
        return sectionNameField;
    }

    public TextField getGeographicalCoordinatesField() {
        return geographicalCoordinatesField;
    }

    public TextField getTemperatureField() {
        return temperatureField;
    }

    public TextField getLightField() {
        return lightField;
    }

    public Label getSoilTypeLabel() {
        return soilTypeLabel;
    }

    public Label getClimateTypeLabel() {
        return climateTypeLabel;
    }

    public Label getIsHandledByLabel() {
        return isHandledByLabel;
    }
    public void clearForm() {
        getSectionNameField().clear();
        getGeographicalCoordinatesField().clear();
        getTemperatureField().clear();
        getLightField().clear();
    }
    public void resetFields() {
        getSectionNameField().setEditable(true);
        getGeographicalCoordinatesField().setEditable(true);
        getTemperatureField().setEditable(true);
        getLightField().setEditable(true);
    }
    public void fillFormFields(SectionsEntity section) {
        setSectionName(section.getSectionName());
        setGeographicalCoordinates(section.getGeographicalCoordinates());
        setTemperature(section.getTemperature());
        setLight(section.getLight());
    }
}
