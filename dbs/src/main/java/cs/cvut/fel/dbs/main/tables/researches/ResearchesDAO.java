package cs.cvut.fel.dbs.main.tables.researches;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.ResearchesEntity;
import cs.cvut.fel.dbs.entities.ScientistsEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.scientists.ScientistsDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ResearchesDAO {
    private static final Logger logger = LogManager.getLogger(ResearchesDAO.class);
    protected static ScientistsEntity selectedScientist = null;
    protected static ResearchesFormController researchesFormController = new ResearchesFormController();
    public static List<ResearchesEntity> getAllResearches() {
        List<ResearchesEntity> researches = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            String query = "SELECT * FROM researches";
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query);
            while (result.next()) {
                ResearchesEntity research = new ResearchesEntity();
                setResearchInfo(research, result);
                researches.add(research);
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
        return researches;
    }
    public static ResearchesEntity getResearch(ResultSet researchInfo) {
        if (researchInfo == null) {
            return null;
        }
        ResearchesEntity research = new ResearchesEntity();
        try {
            if (researchInfo.next()) {
                setResearchInfo(research, researchInfo);
            }
        } catch (Exception e) {
            logger.error("Error while getting instance: " + e.getMessage());
        }
        return research;
    }
    private static void setResearchInfo(ResearchesEntity research, ResultSet researchInfo) {
        Statement scientistStatement = null;
        ResultSet scientistResult = null;
        try {
            research.setIdResearch(researchInfo.getInt("id_research"));
            research.setResearchName(researchInfo.getString("research_name"));
            // get scientist
            String scientistQuery = "SELECT * FROM scientists WHERE id_scientist = " + researchInfo.getInt("scientist_id");
            scientistStatement = DatabaseConnection.getConnection().createStatement();
            scientistResult = scientistStatement.executeQuery(scientistQuery);
            logger.info("Query: " + scientistQuery);
            logger.info("Result: " + scientistResult);
            research.setScientistId(ScientistsDAO.getScientist(scientistResult));
        } catch (SQLException e) {
            logger.error("Error while setting instance info: " + e.getMessage());
        } finally {
            if (scientistResult != null) {
                try {
                    scientistResult.close();
                } catch (SQLException e) {
                    logger.error("Failed to close plant result set: " + e.getMessage());
                }
            }
            if (scientistStatement != null) {
                try {
                    scientistStatement.close();
                } catch (SQLException e) {
                    logger.error("Failed to close plant statement: " + e.getMessage());
                }
            }
        }
    }
    protected static void clearAll() {
        selectedScientist = null;
        researchesFormController.clearForm();
        researchesFormController.resetFields();
        CRUD.errorMessage.setVisible(false);
    }
    protected static void updateResearch(ResearchesEntity research) {
        research.setResearchName(researchesFormController.getResearchNameField().getText());
    }
    protected static boolean checkResearchNameIsUnique(ResearchesEntity research) {
        EntityManager entityManager = DatabaseConnection.getEntityManager();
        List<ResearchesEntity> researches = entityManager.createQuery("SELECT r FROM ResearchesEntity r WHERE r.researchName = :researchName AND r.idResearch != :idResearch", ResearchesEntity.class)
                .setParameter("researchName", research.getResearchName())
                .setParameter("idResearch", research.getIdResearch())
                .getResultList();
        return researches.isEmpty();
    }
    protected static void deleteResearch(ResearchesEntity research) {
        try {
            // research can be deleted without checking (no foreign keys)
            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.remove(research);
            entityManager.getTransaction().commit();
            ResearchesView.showResearchesRecordsList();
            ResearchesView.showResearchInfo(null);

            ResearchesView.showResearchesRecordsList();
            clearAll();
            logger.info("Research deleted successfully");
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            ResearchesView.showResearchesRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
    }

    public static List<ScientistsEntity> getListOfAvailableScientists(ResearchesEntity research) {
        StringBuilder query = new StringBuilder("SELECT * FROM scientists");
        if (research != null) {
            query.append(" WHERE id_scientist != ");
            if (selectedScientist != null) {
                query.append(selectedScientist.getIdScientist());
                logger.info("Query: " + query);
            } else {
                query.append(research.getScientistId().getIdScientist());
                logger.info("Query: " + query);
            }
        }
        List<ScientistsEntity> availableScientists = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query.toString());
            while (!result.isAfterLast()) {
                availableScientists.add(ScientistsDAO.getScientist(result));
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

        return availableScientists;
    }
}
