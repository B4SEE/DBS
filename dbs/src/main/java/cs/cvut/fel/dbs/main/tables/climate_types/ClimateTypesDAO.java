package cs.cvut.fel.dbs.main.tables.climate_types;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.ClimatetypesEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClimateTypesDAO {
    private static final Logger logger = LogManager.getLogger(ClimateTypesDAO.class);

    public static List<ClimatetypesEntity> getAllClimateTypes() {
        List<ClimatetypesEntity> climateTypes = new ArrayList<>();
        try {
            String query = "SELECT * FROM climatetypes";
            Statement statement = DatabaseConnection.getConnection().createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                ClimatetypesEntity climateType = new ClimatetypesEntity();
                setClimateTypeInfo(result, climateType);
                climateTypes.add(climateType);
            }
        } catch (Exception e) {
            logger.error("Error while getting all climate types: " + e.getMessage());
        }
        return climateTypes;
    }
    public static ClimatetypesEntity getClimateType(ResultSet climateTypeInfo) {
        if (climateTypeInfo == null) {
            return null;
        }
        ClimatetypesEntity climateType = new ClimatetypesEntity();
        try {
            if (climateTypeInfo.next()) {
                setClimateTypeInfo(climateTypeInfo, climateType);
            }
        } catch (Exception e) {
            logger.error("Error while getting climate type: " + e.getMessage());
        }
        return climateType;
    }
    private static void setClimateTypeInfo(ResultSet climateTypeInfo, ClimatetypesEntity climateType) throws SQLException {
        climateType.setIdClimateType(climateTypeInfo.getInt("id_climate_type"));
        climateType.setClimateName(climateTypeInfo.getString("climate_name"));
        climateType.setClimateDescription(climateTypeInfo.getString("climate_description"));
    }
}
