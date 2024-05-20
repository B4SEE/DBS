package cs.cvut.fel.dbs.main.special;

import cs.cvut.fel.dbs.entities.*;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.sections.SectionsDAO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class ViewSections {
    private static int page = 1;
    private static int recordsPerPage = 15;
    private static SectionsEntity selectedSection = null;
    private static List<String> problems = new ArrayList<>();
    private static void clear() {
        if (CRUD.recordsGrid != null) {
            CRUD.recordsGrid.getChildren().clear();
        }
        CRUD.showCRUDScene();
        CRUD.recordsFormGrid.getChildren().clear();
        CRUD.recordsGridTitle.setVisible(false);
        CRUD.addButton.setVisible(false);

        page = 1;
        recordsPerPage = 15;
        problems.clear();
    }

    public static void getSectionsViewPage() {
        clear();

        int count = 0;
        GridPane grid = new GridPane();
        Label chooseSection = new Label("Choose section:");
        if (selectedSection != null) {
            chooseSection.setText("Selected section: " + selectedSection.getSectionName());
        }

        Button chooseSectionButton = new Button("Choose section");
        chooseSectionButton.setOnAction(e -> {
            List<SectionsEntity> sections = SectionsDAO.getAllSections();
            showSections(sections);
        });

        grid.add(chooseSectionButton, 1, count);
        grid.add(chooseSection, 0, count++);

        //add instances

        if (selectedSection != null) {
            List<InstancesEntity> instances = SectionsDAO.getSectionInstances(selectedSection);
            for (InstancesEntity instance : instances) {
                Label instanceLabel = new Label(instance.getInstanceName());

                checkInstanceInSection(instance, instanceLabel);

                Button instanceInfoButton = new Button("Info");
                instanceInfoButton.setOnAction(e -> {
                    CRUD.recordsFormGrid.getChildren().clear();

                    GridPane instanceInfoGrid = new GridPane();
                    int infoCount = 0;
                    instanceInfoGrid.add(new Label("Instance name: " + instance.getInstanceName()), 0, infoCount++);
                    instanceInfoGrid.add(new Label("Plant name: " + instance.getPlant().getPlantName()), 0, infoCount++);
                    instanceInfoGrid.add(new Label("Plant type: " + instance.getPlant().getPlantType()), 0, infoCount++);
                    instanceInfoGrid.add(new Label("Instance age: " + instance.getAge()), 0, infoCount++);

                    checkInstanceInSection(instance, instanceLabel);

                    if (!problems.isEmpty()) {
                        instanceInfoGrid.add(new Label("Problems:"), 0, infoCount++);
                        for (String problem : problems) {
                            Label problemLabel = new Label(problem);
                            problemLabel.setStyle("-fx-text-fill: #490606;");
                            instanceInfoGrid.add(problemLabel, 0, infoCount++);
                        }
                    }

                    CRUD.recordsFormGrid.add(instanceInfoGrid, 0, 0);
                    showSectionInfo(infoCount);
                });

                grid.add(instanceInfoButton, 2, count);
                grid.add(instanceLabel, 0, count++);
            }

            // show section info
            showSectionInfo(0);
        }

        CRUD.recordsGrid.add(grid, 0, 0);
    }

    private static void showSections(List<SectionsEntity> sections) {
        clear();
        selectedSection = null;

        int count = 0;
        for (SectionsEntity section : sections) {
            Button sectionButton = new Button(section.getSectionName());
            sectionButton.setOnAction(e2 -> {
                selectedSection = section;
                getSectionsViewPage();
            });
            CRUD.recordsGrid.add(sectionButton, 0, count++);
        }
    }

    private static void checkInstanceInSection(InstancesEntity instance, Label instanceLabel) {
        problems.clear();

        List<SoiltypesEntity> instanceSoilTypes = instance.getPlant().getPreferredSoilTypes();
        List<ClimatetypesEntity> instanceClimateTypes = instance.getPlant().getPreferredClimateTypes();
        int instanceMinLight = instance.getPlant().getMinLight();
        int instanceMaxLight = instance.getPlant().getMaxLight();
        int instanceMinTemp = instance.getPlant().getMinTemperature();
        int instanceMaxTemp = instance.getPlant().getMaxTemperature();

        SoiltypesEntity sectionSoilType = selectedSection.getSoilType();
        ClimatetypesEntity sectionClimateType = selectedSection.getClimateType();
        int sectionLight = selectedSection.getLight();
        int sectionTemperature = selectedSection.getTemperature();

        if (!instanceSoilTypes.contains(sectionSoilType)) {
            problems.add("Instance " + instance.getInstanceName() + " prefers different soil type.");
        }

        if (!instanceClimateTypes.contains(sectionClimateType)) {
            problems.add("Instance " + instance.getInstanceName() + " prefers different climate type.");
        }

        if (sectionLight < instanceMinLight) {
            problems.add("Instance " + instance.getInstanceName() + " prefers more light (" + instanceMinLight + " - " + instanceMaxLight + ").");
        }

        if (sectionLight > instanceMaxLight) {
            problems.add("Instance " + instance.getInstanceName() + " prefers less light (" + instanceMinLight + " - " + instanceMaxLight + ").");
        }

        if (sectionTemperature < instanceMinTemp) {
            problems.add("Instance " + instance.getInstanceName() + " prefers higher temperature (" + instanceMinTemp + " - " + instanceMaxTemp + ").");
        }

        if (sectionTemperature > instanceMaxTemp) {
            problems.add("Instance " + instance.getInstanceName() + " prefers lower temperature (" + instanceMinTemp + " - " + instanceMaxTemp + ").");
        }

        if (!problems.isEmpty()) {
            instanceLabel.setStyle("-fx-text-fill: #ffae00;");
        }
    }
    protected static void showSectionInfo(int count) {
        SoiltypesEntity sectionSoilType = selectedSection.getSoilType();
        ClimatetypesEntity sectionClimateType = selectedSection.getClimateType();
        int sectionLight = selectedSection.getLight();
        int sectionTemperature = selectedSection.getTemperature();
        EmployeesEntity sectionEmployee = selectedSection.getIsHandledBy();

        Label sectionInfo = new Label("\nSection info:");
        CRUD.recordsFormGrid.add(sectionInfo, 0, count++);
        CRUD.recordsFormGrid.add(new Label("Soil type: " + sectionSoilType.getSoilName()), 0, count++);
        CRUD.recordsFormGrid.add(new Label("Climate type: " + sectionClimateType.getClimateName()), 0, count++);
        CRUD.recordsFormGrid.add(new Label("Light: " + sectionLight), 0, count++);
        CRUD.recordsFormGrid.add(new Label("Temperature: " + sectionTemperature), 0, count++);
        CRUD.recordsFormGrid.add(new Label("Section manager: " + sectionEmployee.getPerson().getFirstName() + " " + sectionEmployee.getPerson().getLastName()), 0, count);
    }
}
