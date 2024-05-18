package cs.cvut.fel.dbs.main.table_controllers.plants_controller;

import cs.cvut.fel.dbs.entities.PlantsEntity;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PlantsFormController {
    private final TextField plantNameField = new TextField();
    private final TextField plantTypeField = new TextField();
    private final TextField minTemperatureField = new TextField();
    private final TextField maxTemperatureField = new TextField();
    private final TextField minLightField = new TextField();
    private final TextField maxLightField = new TextField();
    private final Label preferredSoilTypesLabel = new Label("Preferred Soil Types: ");
    private final Label preferredClimateTypesLabel = new Label("Preferred Climate Types: ");

    public String getPlantName() {
        return plantNameField.getText();
    }
    public String getPlantType() {
        return plantTypeField.getText();
    }
    public int getMinTemperature() {
        return Integer.parseInt(minTemperatureField.getText());
    }
    public int getMaxTemperature() {
        return Integer.parseInt(maxTemperatureField.getText());
    }
    public int getMinLight() {
        return Integer.parseInt(minLightField.getText());
    }
    public int getMaxLight() {
        return Integer.parseInt(maxLightField.getText());
    }
    public void setPlantName(String plantName) {
        plantNameField.setText(plantName);
    }
    public void setPlantType(String plantType) {
        plantTypeField.setText(plantType);
    }
    public void setMinTemperature(int minTemperature) {
        minTemperatureField.setText(String.valueOf(minTemperature));
    }
    public void setMaxTemperature(int maxTemperature) {
        maxTemperatureField.setText(String.valueOf(maxTemperature));
    }
    public void setMinLight(int minLight) {
        minLightField.setText(String.valueOf(minLight));
    }
    public void setMaxLight(int maxLight) {
        maxLightField.setText(String.valueOf(maxLight));
    }

    public TextField getPlantNameField() {
        return plantNameField;
    }

    public TextField getPlantTypeField() {
        return plantTypeField;
    }

    public TextField getMinTemperatureField() {
        return minTemperatureField;
    }

    public TextField getMaxTemperatureField() {
        return maxTemperatureField;
    }

    public TextField getMinLightField() {
        return minLightField;
    }

    public TextField getMaxLightField() {
        return maxLightField;
    }

    public Label getPreferredSoilTypesLabel() {
        return preferredSoilTypesLabel;
    }

    public Label getPreferredClimateTypesLabel() {
        return preferredClimateTypesLabel;
    }
    public void clearForm() {
        getPlantNameField().clear();
        getPlantTypeField().clear();
        getMinTemperatureField().clear();
        getMaxTemperatureField().clear();
        getMinLightField().clear();
        getMaxLightField().clear();
    }
    public void resetFields() {
        getPlantNameField().setEditable(true);
        getPlantTypeField().setEditable(true);
        getMinTemperatureField().setEditable(true);
        getMaxTemperatureField().setEditable(true);
        getMinLightField().setEditable(true);
        getMaxLightField().setEditable(true);
    }
    public void fillFormFields(PlantsEntity plant) {
        setPlantName(plant.getPlantName());
        setPlantType(plant.getPlantType());
        setMinTemperature(plant.getMinTemperature());
        setMaxTemperature(plant.getMaxTemperature());
        setMinLight(plant.getMinLight());
        setMaxLight(plant.getMaxLight());
    }
}
