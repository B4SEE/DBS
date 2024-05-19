package cs.cvut.fel.dbs.main.tables.researches;

import cs.cvut.fel.dbs.entities.ResearchesEntity;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ResearchesFormController {
    private final TextField researchNameField = new TextField();
    private final Label scientistLabel = new Label("Scientist: ");

    public String getResearchName() {
        return researchNameField.getText();
    }
    public void setResearchName(String researchName) {
        researchNameField.setText(researchName);
    }
    public TextField getResearchNameField() {
        return researchNameField;
    }
    public Label getScientistLabel() {
        return scientistLabel;
    }
    public void clearForm() {
        getResearchNameField().clear();
    }
    public void resetFields() {
        getResearchNameField().setEditable(true);
    }
    public void fillFormFields(ResearchesEntity research) {
        setResearchName(research.getResearchName());
    }
}
