package cs.cvut.fel.dbs.main.tables.instances;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.InstancesEntity;
import cs.cvut.fel.dbs.entities.PlantsEntity;
import cs.cvut.fel.dbs.entities.SectionsEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.plants.PlantsDAO;
import cs.cvut.fel.dbs.main.tables.sections.SectionsDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InstancesDAO {
    public static int page = 1;
    public static int recordsPerPage = 15;
    private static final Logger logger = LogManager.getLogger(InstancesDAO.class);
    protected static PlantsEntity selectedPlant = null;
    protected static SectionsEntity selectedSection = null;
    protected static InstancesFormController instancesFormController = new InstancesFormController();
    public static List<InstancesEntity> getAllInstances() {
        List<InstancesEntity> instances = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            String query = "SELECT * FROM instances ORDER BY instance_name LIMIT " + recordsPerPage + " OFFSET " + (page - 1) * recordsPerPage;
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query);
            while (result.next()) {
                InstancesEntity instance = new InstancesEntity();
                setInstanceInfo(instance, result);
                instances.add(instance);
            }
        } catch (SQLException e) {
            logger.error("Error while getting all instances: " + e.getMessage());
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
        return instances;
    }
    public static InstancesEntity getInstance(ResultSet instanceInfo) {
        if (instanceInfo == null) {
            return null;
        }
        InstancesEntity instance = new InstancesEntity();
        try {
            if (instanceInfo.next()) {
                setInstanceInfo(instance, instanceInfo);
            }
        } catch (Exception e) {
            logger.error("Error while getting instance: " + e.getMessage());
        }
        return instance;
    }
    private static void setInstanceInfo(InstancesEntity instance, ResultSet instanceInfo) {
        Statement plantStatement = null;
        ResultSet plantResult = null;
        Statement sectionStatement = null;
        ResultSet sectionResult = null;
        try {
            instance.setInstanceName(instanceInfo.getString("instance_name"));
            instance.setAge(instanceInfo.getInt("age"));
            // get plant
            String plantQuery = "SELECT * FROM plants WHERE id_plant = " + instanceInfo.getInt("plant");
            plantStatement = DatabaseConnection.getConnection().createStatement();
            plantResult = plantStatement.executeQuery(plantQuery);
            instance.setPlant(PlantsDAO.getPlant(plantResult));
            // get section
            String sectionQuery = "SELECT * FROM sections WHERE id_section = " + instanceInfo.getInt("section_id");
            sectionStatement = DatabaseConnection.getConnection().createStatement();
            sectionResult = sectionStatement.executeQuery(sectionQuery);
            instance.setSection(SectionsDAO.getSection(sectionResult));
        } catch (SQLException e) {
            logger.error("Error while setting instance info: " + e.getMessage());
        } finally {
            if (plantResult != null) {
                try {
                    plantResult.close();
                } catch (SQLException e) {
                    logger.error("Failed to close plant result set: " + e.getMessage());
                }
            }
            if (plantStatement != null) {
                try {
                    plantStatement.close();
                } catch (SQLException e) {
                    logger.error("Failed to close plant statement: " + e.getMessage());
                }
            }
            if (sectionResult != null) {
                try {
                    sectionResult.close();
                } catch (SQLException e) {
                    logger.error("Failed to close section result set: " + e.getMessage());
                }
            }
            if (sectionStatement != null) {
                try {
                    sectionStatement.close();
                } catch (SQLException e) {
                    logger.error("Failed to close section statement: " + e.getMessage());
                }
            }
        }
    }
    protected static void deleteInstance(InstancesEntity instance) {
        InstancesDAO.clearAll();
        EntityManager entityManager = DatabaseConnection.getEntityManager();
        entityManager.getTransaction().begin();

        TypedQuery<InstancesEntity> query = entityManager.createQuery("SELECT i FROM InstancesEntity i WHERE i.instanceName = :instanceName AND i.plant = :plant AND i.section = :section", InstancesEntity.class);
        query.setParameter("instanceName", instance.getInstanceName());
        query.setParameter("plant", instance.getPlant());
        query.setParameter("section", instance.getSection());

        InstancesEntity instanceInDb = query.getSingleResult();

        entityManager.remove(instanceInDb);
        entityManager.getTransaction().commit();

        InstancesView.showInstancesRecordsList();
        clearAll();
        logger.info("Instance deleted: " + instanceInDb.getInstanceName());
    }
    protected static void clearAll() {
        selectedPlant = null;
        selectedSection = null;
        instancesFormController.clearForm();
        instancesFormController.resetFields();
        CRUD.errorMessage.setVisible(false);
    }
    protected static List<PlantsEntity> getListOfAvailablePlantTypes(InstancesEntity instance) {
        StringBuilder query = new StringBuilder("SELECT * FROM plants");
        if (instance != null) {
            query.append(" WHERE id_plant != ");
            if (selectedPlant != null) {
                query.append(selectedPlant.getIdPlant());
                logger.info("Query: " + query);
            } else {
                query.append(instance.getPlant().getIdPlant());
                logger.info("Query: " + query);
            }
        }
        List<PlantsEntity> availablePlantTypes = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query.toString());
            while (!result.isAfterLast()) {
                availablePlantTypes.add(PlantsDAO.getPlant(result));
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

        return availablePlantTypes;
    }
    protected static List<SectionsEntity> getListOfAvailableSections(InstancesEntity instance) {
        StringBuilder query = new StringBuilder("SELECT * FROM sections");
        if (instance != null) {
            query.append(" WHERE id_section != ");
            if (selectedSection != null) {
                query.append(selectedSection.getIdSection());
            } else {
                query.append(instance.getSection().getIdSection());
            }
        }
        List<SectionsEntity> availableSections = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query.toString());
            while (!result.isAfterLast()) {
                availableSections.add(SectionsDAO.getSection(result));
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

        return availableSections;
    }
    protected static void updateInstance(InstancesEntity instance) {
        instance.setInstanceName(instancesFormController.getInstanceName());
        instance.setAge(instancesFormController.getInstanceAge());
    }
}
