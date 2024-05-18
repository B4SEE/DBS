package cs.cvut.fel.dbs.entities;

import cs.cvut.fel.dbs.app_logic.DatabaseConnection;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@javax.persistence.Table(name = "plants", schema = "public", catalog = "virycele")
public class PlantsEntity {
    private static final Logger logger = LogManager.getLogger(PlantsEntity.class);
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @javax.persistence.Column(name = "id_plant")
    private int idPlant;

    public int getIdPlant() {
        return idPlant;
    }

    public void setIdPlant(int idPlant) {
        this.idPlant = idPlant;
    }

    @Basic
    @Column(name = "plant_name")
    private String plantName;

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    @Basic
    @Column(name = "plant_type")
    private String plantType;

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    @Basic
    @Column(name = "min_temperature")
    private int minTemperature;

    public int getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    @Basic
    @Column(name = "max_temperature")
    private int maxTemperature;

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    @Basic
    @Column(name = "min_light")
    private int minLight;

    public int getMinLight() {
        return minLight;
    }

    public void setMinLight(int minLight) {
        this.minLight = minLight;
    }

    @Basic
    @Column(name = "max_light")
    private int maxLight;

    public int getMaxLight() {
        return maxLight;
    }

    public void setMaxLight(int maxLight) {
        this.maxLight = maxLight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantsEntity that = (PlantsEntity) o;
        return idPlant == that.idPlant && minTemperature == that.minTemperature && maxTemperature == that.maxTemperature && minLight == that.minLight && maxLight == that.maxLight && Objects.equals(plantName, that.plantName) && Objects.equals(plantType, that.plantType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlant, plantName, plantType, minTemperature, maxTemperature, minLight, maxLight);
    }

    public List<SoiltypesEntity> getPreferredSoilTypes() {
        String query = "SELECT * FROM soiltypes WHERE id_soil_type IN (SELECT id_soil_type FROM plantspreferredsoiltypes WHERE id_plant = " + idPlant + ")";
        List<SoiltypesEntity> soiltypes = new ArrayList<>();
        ResultSet result;
        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            statement.executeQuery(query);
            result = statement.getResultSet();
            logger.info("Result: " + result);
        } catch (Exception e) {
            logger.error("Error while getting preferred soil types for plant with id " + idPlant+ ":" + e.getMessage());
            return null;
        }
        try {
            while (result.next()) {
                SoiltypesEntity soiltypesEntity = new SoiltypesEntity();
                soiltypesEntity.setIdSoilType(result.getInt("id_soil_type"));
                soiltypesEntity.setSoilName(result.getString("soil_name"));
                soiltypesEntity.setSoilDescription(result.getString("soil_description"));
                soiltypes.add(soiltypesEntity);
                logger.info("Soil type: " + soiltypesEntity.getSoilName());
            }
        } catch (Exception e) {
            logger.error("Error while iterating over preferred soil types for plant with id " + idPlant+ ":" + e.getMessage());
            return null;
        }
        return soiltypes;
    }
    public List<ClimatetypesEntity> getPreferredClimateTypes() {
        String query = "SELECT * FROM climatetypes WHERE id_climate_type IN (SELECT id_climate_type FROM plantspreferredclimatetypes WHERE id_plant = " + idPlant + ")";
        List<ClimatetypesEntity> climatetypes = new ArrayList<>();
        ResultSet result;
        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            statement.executeQuery(query);
            result = statement.getResultSet();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        try {
            while (result.next()) {
                ClimatetypesEntity climatetypesEntity = new ClimatetypesEntity();
                climatetypesEntity.setIdClimateType(result.getInt("id_climate_type"));
                climatetypesEntity.setClimateName(result.getString("climate_name"));
                climatetypesEntity.setClimateDescription(result.getString("climate_description"));
                climatetypes.add(climatetypesEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return climatetypes;
    }
    public void deletePreferredSoilType(SoiltypesEntity soilType) {
        //delete connection between plant and soil type in plantspreferredsoiltypes table
        String query = "DELETE FROM plantspreferredsoiltypes WHERE id_plant = " + idPlant + " AND id_soil_type = " + soilType.getIdSoilType();
        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error("Error while deleting preferred soil type for plant with id " + idPlant + ": " + e.getMessage());
        }
    }
    public void addPreferredSoilType(SoiltypesEntity soilType) {
        // add preferred soil type to database
        String query = "INSERT INTO plantspreferredsoiltypes (id_plant, id_soil_type) VALUES (" + idPlant + ", " + soilType.getIdSoilType() + ")";
        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error("Error while adding preferred soil type for plant with id " + idPlant + ": " + e.getMessage());
        }
    }
    public void deletePreferredClimateType(ClimatetypesEntity climateType) {
        String query = "DELETE FROM plantspreferredclimatetypes WHERE id_plant = " + idPlant + " AND id_climate_type = " + climateType.getIdClimateType();
        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error("Error while deleting preferred climate type for plant with id " + idPlant + ": " + e.getMessage());
        }
    }
    public void addPreferredClimateType(ClimatetypesEntity climateType) {
        // add preferred climate type to database
        String query = "INSERT INTO plantspreferredclimatetypes (id_plant, id_climate_type) VALUES (" + idPlant + ", " + climateType.getIdClimateType() + ")";
        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error("Error while adding preferred climate type for plant with id " + idPlant + ": " + e.getMessage());
        }
    }
}
