package cs.cvut.fel.dbs.main.tables.sections;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.*;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.climate_types.ClimateTypesDAO;
import cs.cvut.fel.dbs.main.tables.employees.EmployeesDAO;
import cs.cvut.fel.dbs.main.tables.soil_types.SoilTypesDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SectionsDAO {
    private static final Logger logger = LogManager.getLogger(SectionsDAO.class);
    protected static SoiltypesEntity selectedSoilType = null;
    protected static ClimatetypesEntity selectedClimateType = null;
    protected static EmployeesEntity selectedSectionManager = null;
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
    protected static List<SoiltypesEntity> getListOfAvailableSoilTypes(SectionsEntity section) {
        StringBuilder query = new StringBuilder("SELECT * FROM soiltypes");
        if (section != null) {
            query.append(" WHERE id_soil_type != ");
            if (selectedSoilType != null) {
                query.append(selectedSoilType.getIdSoilType());
                logger.info("Query: " + query);
            } else {
                query.append(section.getSoilType().getIdSoilType());
                logger.info("Query: " + query);
            }
        }

        List<SoiltypesEntity> availableSoilTypes = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query.toString());
            while (!result.isAfterLast()) {
                availableSoilTypes.add(SoilTypesDAO.getSoilType(result));
            }
        } catch (Exception e) {
            logger.error("Failed to load soil types: " + e.getMessage());
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    logger.error("Failed to close result set: " + e.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error("Failed to close statement: " + e.getMessage());
                }
            }
        }

        return availableSoilTypes;
    }
    protected static List<ClimatetypesEntity> getListOfAvailableClimateTypes(SectionsEntity section) {
        StringBuilder query = new StringBuilder("SELECT * FROM climatetypes");
        if (section != null) {
            query.append(" WHERE id_climate_type != ");
            if (selectedClimateType != null) {
                query.append(selectedClimateType.getIdClimateType());
                logger.info("Query: " + query);
            } else {
                query.append(section.getClimateType().getIdClimateType());
                logger.info("Query: " + query);
            }
        }

        List<ClimatetypesEntity> availableClimateTypes = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query.toString());
            while (!result.isAfterLast()) {
                availableClimateTypes.add(ClimateTypesDAO.getClimateType(result));
            }
        } catch (Exception e) {
            logger.error("Failed to load climate types: " + e.getMessage());
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    logger.error("Failed to close result set: " + e.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error("Failed to close statement: " + e.getMessage());
                }
            }
        }

        return availableClimateTypes;
    }
    protected static List<EmployeesEntity> getListOfAvailableEmployees(SectionsEntity section) {
        StringBuilder query = new StringBuilder("SELECT * FROM employees");
        if (section != null) {
            query.append(" WHERE id_employee != ");
            if (selectedSectionManager != null) {
                query.append(selectedSectionManager.getIdEmployee());
                logger.info("Query: " + query);
            } else {
                query.append(section.getIsHandledBy().getIdEmployee());
                logger.info("Query: " + query);
            }
        }

        List<EmployeesEntity> availableEmployees = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query.toString());
            while (!result.isAfterLast()) {
                availableEmployees.add(EmployeesDAO.getEmployee(result));
            }
        } catch (Exception e) {
            logger.error("Failed to load employees: " + e.getMessage());
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    logger.error("Failed to close result set: " + e.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error("Failed to close statement: " + e.getMessage());
                }
            }
        }

        return availableEmployees;
    }

    protected static void deleteSection(SectionsEntity section) {
        // check if there are any instances in the section
        EntityManager entityManager = DatabaseConnection.getEntityManager();
        List<InstancesEntity> instances = entityManager.createQuery("SELECT i FROM InstancesEntity i WHERE i.section = :section", InstancesEntity.class)
                .setParameter("section", section)
                .getResultList();
        if (!instances.isEmpty()) {
            logger.error("Cannot delete section with instances in it");
            return;
        }
        entityManager.getTransaction().begin();
        entityManager.remove(section);
        entityManager.getTransaction().commit();
        logger.info("Section deleted successfully.");
    }
    protected static boolean checkSectionNameAndCoordinatesAreUnique(SectionsEntity section) {
        logger.info("Checking if section name and geographical coordinates are unique");
        EntityManager entityManager = DatabaseConnection.getEntityManager();
        // both section name and geographical coordinates must be unique
        List<SectionsEntity> sections = entityManager.createQuery("SELECT s FROM SectionsEntity s WHERE s.sectionName = :sectionName OR s.geographicalCoordinates = :geographicalCoordinates", SectionsEntity.class)
                .setParameter("sectionName", section.getSectionName())
                .setParameter("geographicalCoordinates", section.getGeographicalCoordinates())
                .getResultList();

        return sections.isEmpty();
    }
}
