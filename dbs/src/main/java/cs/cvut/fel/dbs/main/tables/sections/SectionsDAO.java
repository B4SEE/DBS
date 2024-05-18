package cs.cvut.fel.dbs.main.tables.sections;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.EmployeesEntity;
import cs.cvut.fel.dbs.entities.SectionsEntity;
import cs.cvut.fel.dbs.main.tables.climate_types.ClimateTypesDAO;
import cs.cvut.fel.dbs.main.tables.employees.EmployeesDAO;
import cs.cvut.fel.dbs.main.tables.soil_types.SoilTypesDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.Statement;

public class SectionsDAO {
    private static final Logger logger = LogManager.getLogger(SectionsDAO.class);
    public static SectionsEntity getSection(ResultSet sectionInfo) {
        if (sectionInfo == null) {
            return null;
        }
        SectionsEntity section = new SectionsEntity();
        try {
            if (sectionInfo.next()) {

                section.setIdSection(sectionInfo.getInt("id_section"));
                section.setSectionName(sectionInfo.getString("section_name"));
                section.setGeographicalCoordinates(sectionInfo.getString("geographical_coordinates"));
                section.setTemperature(sectionInfo.getInt("temperature"));
                section.setLight(sectionInfo.getInt("light"));
                // get soil type
                String soilTypeQuery = "SELECT * FROM soiltypes WHERE id_soil_type = " + sectionInfo.getInt("soil_type");
                Statement statement = DatabaseConnection.getConnection().createStatement();
                ResultSet soilTypeResult = statement.executeQuery(soilTypeQuery);
                section.setSoilType(SoilTypesDAO.getSoilType(soilTypeResult));
                // get climate type
                String climateTypeQuery = "SELECT * FROM climatetypes WHERE id_climate_type = " + sectionInfo.getInt("climate_type");
                Statement climateTypeStatement = DatabaseConnection.getConnection().createStatement();
                ResultSet climateTypeResult = climateTypeStatement.executeQuery(climateTypeQuery);
                section.setClimateType(ClimateTypesDAO.getClimateType(climateTypeResult));
                // get section manager
                String sectionManagerQuery = "SELECT * FROM employees WHERE id_employee = " + sectionInfo.getInt("is_handled_by");
                Statement sectionManagerStatement = DatabaseConnection.getConnection().createStatement();
                ResultSet sectionManagerResult = sectionManagerStatement.executeQuery(sectionManagerQuery);
                EmployeesEntity sectionManager = EmployeesDAO.getEmployee(sectionManagerResult);
                section.setIsHandledBy(sectionManager);
            }
        } catch (Exception e) {
            logger.error("Error while getting section: " + e.getMessage());
        }
        return section;
    }
}
