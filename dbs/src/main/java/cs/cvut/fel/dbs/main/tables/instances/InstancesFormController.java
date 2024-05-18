package cs.cvut.fel.dbs.main.tables.instances;

import cs.cvut.fel.dbs.entities.InstancesEntity;
import cs.cvut.fel.dbs.entities.PlantsEntity;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class InstancesFormController {
    private final TextField instanceNameField = new TextField();
    private final TextField instanceAgeField = new TextField();
    private final Label plantTypeLabel = new Label("Plant: ");
    private final Label sectionLabel = new Label("Section: ");
    public String getInstanceName() {
        return instanceNameField.getText();
    }
    public void setInstanceName(String instanceName) {
        instanceNameField.setText(instanceName);
    }
    public int getInstanceAge() {
        return Integer.parseInt(instanceAgeField.getText());
    }
    public void setInstanceAge(int instanceAge) {
        instanceAgeField.setText(String.valueOf(instanceAge));
    }
    public TextField getInstanceNameField() {
        return instanceNameField;
    }

    public TextField getInstanceAgeField() {
        return instanceAgeField;
    }

    public Label getPlantTypeLabel() {
        return plantTypeLabel;
    }

    public Label getSectionLabel() {
        return sectionLabel;
    }

    public void clearForm() {
        getInstanceNameField().clear();
        getInstanceAgeField().clear();
    }
    public void resetFields() {
        getInstanceNameField().setEditable(true);
        getInstanceAgeField().setEditable(true);
    }
    public void fillFormFields(InstancesEntity instance) {
        setInstanceName(instance.getInstanceName());
        setInstanceAge(instance.getAge());
    }
}
