package cs.cvut.fel.dbs.visuals.table_controllers.plants_controller;

import cs.cvut.fel.dbs.app_logic.DatabaseConnection;
import cs.cvut.fel.dbs.entities.ClimatetypesEntity;
import cs.cvut.fel.dbs.entities.PlantsEntity;
import cs.cvut.fel.dbs.entities.SoiltypesEntity;
import cs.cvut.fel.dbs.visuals.CRUD;
import cs.cvut.fel.dbs.visuals.table_controllers.RecordsController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlantsController {

    private static final Logger logger = LogManager.getLogger(PlantsController.class);
    public static class PlantsDAO {
        private static String query = "SELECT * FROM plants";
        private static Statement statement;

        static {
            try {
                statement = DatabaseConnection.getConnection().createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        private static ResultSet result;

        static {
            try {
                result = statement.executeQuery(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        private static List<PlantsEntity> plants = new ArrayList<>();

        public static void setEntityManager(EntityManager entityManager) {
        }

        public static List<PlantsEntity> getAllPlants() throws SQLException {
            while (result.next()) {
                PlantsEntity plant = new PlantsEntity();
                plant.setIdPlant(result.getInt("id_plant"));
                plant.setPlantName(result.getString("plant_name"));
                plant.setPlantType(result.getString("plant_type"));
                plant.setMaxTemperature(result.getInt("max_temperature"));
                plant.setMinTemperature(result.getInt("min_temperature"));
                plant.setMaxLight(result.getInt("max_light"));
                plant.setMinLight(result.getInt("min_light"));
                plants.add(plant);
            }
            return plants;
        }
    }

    public static void setPlantsRecordsList(GridPane recordsGrid) {
        // Load fxml file
        logger.info("Initializing plants list...");
        // load all records
        List<PlantsEntity> plants = new ArrayList<>();
        try {
            PlantsDAO.setEntityManager(DatabaseConnection.getEntityManager());
            plants = PlantsDAO.getAllPlants();
        } catch (SQLException e) {
            logger.error("Failed to load plants: " + e.getMessage());
        }
        // create GridPane for all records
        int count = recordsGrid.getChildren().stream()
                .map(GridPane::getRowIndex)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(-1) + 1;

        for (PlantsEntity plant : plants) {
            RecordsController recordsController = new RecordsController();
            GridPane recordGrid = recordsController.getRecordGrid(plant.getPlantName());
            GridPane.setRowIndex(recordGrid, count++);
            recordsController.infoButton.setOnAction(event -> {
                setPlantInfo(CRUD.recordsFormGrid, plant);
            });
            recordsController.editButton.setOnAction(event -> {
                setPlantEditForm(CRUD.recordsFormGrid, plant);
            });
            recordsController.deleteButton.setOnAction(event -> {
                EntityManager entityManager = DatabaseConnection.getEntityManager();
                entityManager.getTransaction().begin();

                // Fetch the plant from the database to get the latest state
                PlantsEntity plantInDb = entityManager.find(PlantsEntity.class, plant.getIdPlant());

                // Check if there are any associated instances with the plant
                String query = "SELECT * FROM instances WHERE plant = " + plantInDb.getIdPlant();
                ResultSet result;
                try {
                    Statement statement = DatabaseConnection.getConnection().createStatement();
                    statement.executeQuery(query);
                    result = statement.getResultSet();
                    logger.info("Result: " + result);
                } catch (Exception e) {
                    logger.error("Error while getting instances for plant with id " + plantInDb.getIdPlant() + ":" + e.getMessage());
                    return;
                }

                try {
                    if (result.next()) {
                        CRUD.showCRUDScene();
                        PlantsController.setPlantsRecordsList(CRUD.recordsGrid);
                        CRUD.addErrorMessageAndAddButton();
                        CRUD.errorMessage.setText("Failed to delete plant: There are associated instances with this plant.");
                        CRUD.errorMessage.setStyle("-fx-text-fill: red");
                        CRUD.errorMessage.setVisible(true);
                        return;
                    }
                } catch (SQLException e) {
                    logger.error("Failed to delete plant: " + e.getMessage());
                    return;
                }

                entityManager.remove(plantInDb);
                entityManager.getTransaction().commit();
                setPlantsRecordsList(CRUD.recordsGrid);
            });
            recordsGrid.getChildren().add(recordGrid);
            logger.info("Plant record added: " + plant.getPlantName() + "at row " + count);
        }
        logger.info("Total rows: " + count);
        CRUD.addButton.setOnAction(event -> {
            setEmptyForm(CRUD.recordsFormGrid);
        });
    }

    public static void setPlantInfo(GridPane infoFormGrid, PlantsEntity plant) {
        infoFormGrid.getChildren().clear();

        FormController formController = new FormController();
        fillFormFields(formController, plant);

        formController.getPlantNameField().setEditable(false);
        formController.getPlantTypeField().setEditable(false);
        formController.getMinTemperatureField().setEditable(false);
        formController.getMaxTemperatureField().setEditable(false);
        formController.getMinLightField().setEditable(false);
        formController.getMaxLightField().setEditable(false);

        Label formTitle = new Label("Plant Info");
        infoFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(infoFormGrid, formController, 1);

        infoFormGrid.add(formController.getPreferredSoilTypesLabel(), 0, count++);
        List<SoiltypesEntity> preferredSoilTypes = plant.getPreferredSoilTypes();
        for (SoiltypesEntity soilType : preferredSoilTypes) {
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(new Label(soilType.getSoilName()));
            infoFormGrid.add(stackPane, 0, count++);
        }

        infoFormGrid.add(formController.getPreferredClimateTypesLabel(), 0, count++);
        List<ClimatetypesEntity> preferredClimateTypes = plant.getPreferredClimateTypes();
        for (ClimatetypesEntity climateType : preferredClimateTypes) {
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(new Label(climateType.getClimateName()));
            infoFormGrid.add(stackPane, 0, count++);
        }
    }
    public static void setPlantEditForm(GridPane editFormGrid, PlantsEntity plant) {
        editFormGrid.getChildren().clear();

        FormController formController = new FormController();
        fillFormFields(formController, plant);

        Label formTitle = new Label("Edit Plant");
        editFormGrid.add(formTitle, 0, 0);
        int count = setFormFields(editFormGrid, formController, 1);

        Label attentionLabel = new Label("Attention: Deleting a preferred soil type or climate type is performed immediately after clicking the delete button.\nAdding a preferred soil type or climate type also adds it immediately.");
        attentionLabel.setTextFill(javafx.scene.paint.Color.RED);
        attentionLabel.setMaxWidth(200);
        attentionLabel.setWrapText(true); // wrap text if it's too long
        //merge cells to fit the text
        GridPane.setColumnSpan(attentionLabel, 2);
        editFormGrid.add(attentionLabel, 0, count++);

        editFormGrid.add(formController.getPreferredSoilTypesLabel(), 0, count++);
        // add list of preferred soil types with delete button
        List<SoiltypesEntity> preferredSoilTypes = plant.getPreferredSoilTypes();
        for (SoiltypesEntity soilType : preferredSoilTypes) {
            Label label = new Label(soilType.getSoilName());
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> deletePreferredSoilType(plant, soilType));
            editFormGrid.add(label, 0, count);
            editFormGrid.add(deleteButton, 1, count++);
        }
        // add addSoilType button
        Button addSoilTypeButton = getAddSoilTypeButton(plant);
        editFormGrid.add(addSoilTypeButton, 0, count++);

        editFormGrid.add(formController.getPreferredClimateTypesLabel(), 0, count++);
        List<ClimatetypesEntity> preferredClimateTypes = plant.getPreferredClimateTypes();
        for (ClimatetypesEntity climateType : preferredClimateTypes) {
            Label label = new Label(climateType.getClimateName());
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> deletePreferredClimateType(plant, climateType));
            editFormGrid.add(label, 0, count);
            editFormGrid.add(deleteButton, 1, count++);
        }
        // add addClimateType button
        Button addClimateTypeButton = getAddClimateTypeButton(plant);
        editFormGrid.add(addClimateTypeButton, 0, count++);

        Button submitButton = getSubmitFormButton(plant, formController);
        Button cancelButton = getCancelButton();

        editFormGrid.add(submitButton, 0, count);
        editFormGrid.add(cancelButton, 1, count);
    }

    private static void fillFormFields(FormController formController, PlantsEntity plant) {
        formController.setPlantName(plant.getPlantName());
        formController.setPlantType(plant.getPlantType());
        formController.setMinTemperature(plant.getMinTemperature());
        formController.setMaxTemperature(plant.getMaxTemperature());
        formController.setMinLight(plant.getMinLight());
        formController.setMaxLight(plant.getMaxLight());
    }

    private static Button getAddSoilTypeButton(PlantsEntity plant) {
        Button addSoilTypeButton = new Button("Add Soil Type");
        addSoilTypeButton.setOnAction(event -> {
            // replace records with soil types (label with add button)
            // don't show soil types that are already in the list
            CRUD.recordsGrid.getChildren().clear();
            List<SoiltypesEntity> availableSoilTypes = new ArrayList<>();
            try {
                String query = "SELECT * FROM soiltypes WHERE id_soil_type NOT IN (SELECT id_soil_type FROM plantspreferredsoiltypes WHERE id_plant = " + plant.getIdPlant() + ")";
                Statement statement = DatabaseConnection.getConnection().createStatement();
                statement.executeQuery(query);
                ResultSet result = statement.getResultSet();
                while (result.next()) {
                    SoiltypesEntity soilType = new SoiltypesEntity();
                    soilType.setIdSoilType(result.getInt("id_soil_type"));
                    soilType.setSoilName(result.getString("soil_name"));
                    soilType.setSoilDescription(result.getString("soil_description"));
                    availableSoilTypes.add(soilType);
                }
            } catch (Exception e) {
                logger.error("Failed to load soil types: " + e.getMessage());
            }
            int countSoilTypes = 0;
            for (SoiltypesEntity soilType : availableSoilTypes) {
                RecordsController recordsController = new RecordsController();
                GridPane recordGrid = recordsController.getRecordGrid(soilType.getSoilName());
                GridPane.setRowIndex(recordGrid, countSoilTypes++);
                recordsController.recordInfo.setText(soilType.getSoilName());
                recordsController.infoButton.setOnAction(event1 -> {
                    // add soil type to plant
                    plant.addPreferredSoilType(soilType);
                    // update form
                    CRUD.showCRUDScene();
                    setPlantsRecordsList(CRUD.recordsGrid);
                    CRUD.addErrorMessageAndAddButton();
                    setPlantEditForm(CRUD.recordsFormGrid, plant);
                });
                recordsController.infoButton.setText("Add");
                recordsController.editButton.setVisible(false);
                recordsController.deleteButton.setVisible(false);
                CRUD.recordsGrid.getChildren().add(recordGrid);
            }
        });
        return addSoilTypeButton;
    }

    private static Button getAddClimateTypeButton(PlantsEntity plant) {
        Button addClimateTypeButton = new Button("Add Climate Type");
        addClimateTypeButton.setOnAction(event -> {
            // replace records with climate types (label with add button)
            // don't show climate types that are already in the list
            CRUD.recordsGrid.getChildren().clear();
            List<ClimatetypesEntity> availableClimateTypes = new ArrayList<>();
            try {
                String query = "SELECT * FROM climatetypes WHERE id_climate_type NOT IN (SELECT id_climate_type FROM plantspreferredclimatetypes WHERE id_plant = " + plant.getIdPlant() + ")";
                Statement statement = DatabaseConnection.getConnection().createStatement();
                statement.executeQuery(query);
                ResultSet result = statement.getResultSet();
                while (result.next()) {
                    ClimatetypesEntity climateType = new ClimatetypesEntity();
                    climateType.setIdClimateType(result.getInt("id_climate_type"));
                    climateType.setClimateName(result.getString("climate_name"));
                    climateType.setClimateDescription(result.getString("climate_description"));
                    availableClimateTypes.add(climateType);
                }
            } catch (Exception e) {
                logger.error("Failed to load climate types: " + e.getMessage());
            }
            int countClimateTypes = 0;
            for (ClimatetypesEntity climateType : availableClimateTypes) {
                RecordsController recordsController = new RecordsController();
                GridPane recordGrid = recordsController.getRecordGrid(climateType.getClimateName());
                GridPane.setRowIndex(recordGrid, countClimateTypes++);
                recordsController.recordInfo.setText(climateType.getClimateName());
                recordsController.infoButton.setOnAction(event1 -> {
                    // add climate type to plant
                    plant.addPreferredClimateType(climateType);
                    // update form
                    CRUD.showCRUDScene();
                    setPlantsRecordsList(CRUD.recordsGrid);
                    CRUD.addErrorMessageAndAddButton();
                    setPlantEditForm(CRUD.recordsFormGrid, plant);
                });
                recordsController.infoButton.setText("Add");
                recordsController.editButton.setVisible(false);
                recordsController.deleteButton.setVisible(false);
                CRUD.recordsGrid.getChildren().add(recordGrid);
            }
        });
        return addClimateTypeButton;
    }
    private static Button getSubmitFormButton(PlantsEntity plant, FormController formController) {
        // add submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            try {
                updatePlant(plant, formController);
                EntityManager entityManager = DatabaseConnection.getEntityManager();
                entityManager.getTransaction().begin();
                entityManager.merge(plant);
                entityManager.getTransaction().commit();
                CRUD.showCRUDScene();
                PlantsController.setPlantsRecordsList(CRUD.recordsGrid);
                CRUD.addErrorMessageAndAddButton();
                setPlantInfo(CRUD.recordsFormGrid, plant);
            } catch (javax.persistence.RollbackException e) {
                logger.error("Transaction failed and has been rolled back: " + e.getMessage());
                CRUD.showCRUDScene();
                PlantsController.setPlantsRecordsList(CRUD.recordsGrid);
                CRUD.addErrorMessageAndAddButton();
                CRUD.errorMessage.setText("Failed: " + e.getMessage());
            } catch (Exception e) {
                logger.error("Failed: " + e.getMessage());
                CRUD.showCRUDScene();
                PlantsController.setPlantsRecordsList(CRUD.recordsGrid);
                CRUD.addErrorMessageAndAddButton();
                CRUD.errorMessage.setText("Failed: " + e.getMessage());
            }
        });
        return submitButton;
    }
    private static Button getCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> {
            CRUD.showCRUDScene();
            PlantsController.setPlantsRecordsList(CRUD.recordsGrid);
            CRUD.addErrorMessageAndAddButton();
        });
        return cancelButton;
    }
    private static void deletePreferredSoilType(PlantsEntity plant, SoiltypesEntity soilType) {
        plant.deletePreferredSoilType(soilType);
        // update form
        setPlantEditForm(CRUD.recordsFormGrid, plant);
    }
    private static void deletePreferredClimateType(PlantsEntity plant, ClimatetypesEntity climateType) {
        plant.deletePreferredClimateType(climateType);
        // update form
        setPlantEditForm(CRUD.recordsFormGrid, plant);
    }
    private static int setFormFields(GridPane formGrid, FormController formController, int count) {
        formGrid.add(new Label("Plant Name:"), 0, count);
        formGrid.add(formController.getPlantNameField(), 1, count++);
        formGrid.add(new Label("Plant Type:"), 0, count);
        formGrid.add(formController.getPlantTypeField(), 1, count++);
        formGrid.add(new Label("Min Temperature:"), 0, count);
        formGrid.add(formController.getMinTemperatureField(), 1, count++);
        formGrid.add(new Label("Max Temperature:"), 0, count);
        formGrid.add(formController.getMaxTemperatureField(), 1, count++);
        formGrid.add(new Label("Min Light:"), 0, count);
        formGrid.add(formController.getMinLightField(), 1, count++);
        formGrid.add(new Label("Max Light:"), 0, count);
        formGrid.add(formController.getMaxLightField(), 1, count++);
        return count;
    }

    public static void setEmptyForm(GridPane formGrid) {
        formGrid.getChildren().clear();

        Label formTitle = new Label("Add Plant");
        formGrid.add(formTitle, 0, 0);

        FormController formController = new FormController();
        int count = setFormFields(formGrid, formController, 1);

        Button addButton = new Button("Add");

        addButton.setOnAction(event -> {
            try {
                PlantsEntity plant = new PlantsEntity();
                updatePlant(plant, formController);

                EntityManager entityManager = DatabaseConnection.getEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(plant);
                entityManager.getTransaction().commit();
                CRUD.showCRUDScene();
                setPlantsRecordsList(CRUD.recordsGrid);
                CRUD.addErrorMessageAndAddButton();
                setPlantEditForm(CRUD.recordsFormGrid, plant);
            } catch (Exception e) {
                logger.error("Failed: " + e.getMessage());
                CRUD.showCRUDScene();
                PlantsController.setPlantsRecordsList(CRUD.recordsGrid);
                CRUD.addErrorMessageAndAddButton();
                CRUD.errorMessage.setText("Failed: " + e.getMessage());
            }
        });

        Button cancelButton = getCancelButton();

        formGrid.add(addButton, 0, count);
        formGrid.add(cancelButton, 1, count);
    }
    private static void updatePlant(PlantsEntity plant, FormController formController) {
        plant.setPlantName(formController.getPlantName());
        plant.setPlantType(formController.getPlantType());
        plant.setMinTemperature(formController.getMinTemperature());
        plant.setMaxTemperature(formController.getMaxTemperature());
        plant.setMinLight(formController.getMinLight());
        plant.setMaxLight(formController.getMaxLight());
    }
}
