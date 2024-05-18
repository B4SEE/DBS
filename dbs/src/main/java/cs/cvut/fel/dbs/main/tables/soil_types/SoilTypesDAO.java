package cs.cvut.fel.dbs.main.tables.soil_types;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.SoiltypesEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SoilTypesDAO {
    private static final Logger logger = LogManager.getLogger(SoilTypesDAO.class);

    public static List<SoiltypesEntity> getAllSoilTypes() {
        List<SoiltypesEntity> soilTypes = new ArrayList<>();
        try {
            String query = "SELECT * FROM soiltypes";
            Statement statement = DatabaseConnection.getConnection().createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                SoiltypesEntity soilType = new SoiltypesEntity();
                setSoilTypeInfo(result, soilType);
                soilTypes.add(soilType);
            }
        } catch (Exception e) {
            logger.error("Error while getting all climate types: " + e.getMessage());
        }
        return soilTypes;
    }
    public static SoiltypesEntity getSoilType(ResultSet SoilTypeInfo) {
        if (SoilTypeInfo == null) {
            return null;
        }
        SoiltypesEntity soilType = new SoiltypesEntity();
        try {
            if (SoilTypeInfo.next()) {
                setSoilTypeInfo(SoilTypeInfo, soilType);
            }
        } catch (Exception e) {
            logger.error("Error while getting climate type: " + e.getMessage());
        }
        return soilType;
    }
    private static void setSoilTypeInfo(ResultSet soilTypeInfo,SoiltypesEntity soilType) throws SQLException {
        soilType.setIdSoilType(soilTypeInfo.getInt("id_soil_type"));
        soilType.setSoilName(soilTypeInfo.getString("soil_name"));
        soilType.setSoilDescription(soilTypeInfo.getString("soil_description"));
    }
}
