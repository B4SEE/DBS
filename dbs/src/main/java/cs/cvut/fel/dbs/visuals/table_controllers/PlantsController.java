package cs.cvut.fel.dbs.visuals.table_controllers;

import cs.cvut.fel.dbs.app_logic.DatabaseConnection;
import cs.cvut.fel.dbs.entities.PlantsEntity;
import cs.cvut.fel.dbs.visuals.CRUD;
import cs.cvut.fel.dbs.visuals.GUI;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
        private static EntityManager entityManager;
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
            PlantsDAO.entityManager = entityManager;
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
            GridPane recordGrid = RecordsController.getRecordGrid(plant.getPlantName());
            GridPane.setRowIndex(recordGrid, count++);
            recordsGrid.getChildren().add(recordGrid);
            logger.info("Plant record added: " + plant.getPlantName() + "at row " + count);
        }
        logger.info("Total rows: " + count);
    }
}
