package cs.cvut.fel.dbs.main.tables.scientists;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.PersonsEntity;
import cs.cvut.fel.dbs.entities.ResearchesEntity;
import cs.cvut.fel.dbs.entities.ScientistsEntity;
import cs.cvut.fel.dbs.entities.SectionsEntity;
import cs.cvut.fel.dbs.main.CRUD;
import cs.cvut.fel.dbs.main.tables.persons.PersonsDAO;
import cs.cvut.fel.dbs.main.tables.sections.SectionsDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ScientistsDAO {
    private static final Logger logger = LogManager.getLogger(ScientistsDAO.class);
    protected static SectionsEntity selectedSection = null;
    protected static SectionsEntity sectionToDelete = null;
    protected static PersonsEntity selectedPerson = null;
    protected static ScientistsFormController scientistsFormController = new ScientistsFormController();
    public static List<ScientistsEntity> getAllScientists() {
        List<ScientistsEntity> scientists = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            String query = "SELECT * FROM scientists";
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query);
            while (result.next()) {
                ScientistsEntity scientist = new ScientistsEntity();
                setScientistInfo(scientist, result);
                scientists.add(scientist);
            }
        } catch (SQLException e) {
            logger.error("Error while getting all scientists: " + e.getMessage());
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
        return scientists;
    }
    public static ScientistsEntity getScientist(ResultSet scientistInfo) {
        logger.info("Getting scientist...");
        if (scientistInfo == null) {
            logger.error("Scientist info is null.");
            return null;
        }
        ScientistsEntity scientist = new ScientistsEntity();
        try {
            if (scientistInfo.next()) {
                logger.info("Setting scientist info...");
                setScientistInfo(scientist, scientistInfo);
            }
        } catch (SQLException e) {
            logger.error("Error while getting scientist: " + e.getMessage());
        }
        return scientist;
    }
    public static void setScientistInfo(ScientistsEntity scientist, ResultSet result) {
        try {
            scientist.setIdScientist(result.getInt("id_scientist"));
            //get person
            Statement personStatement = null;
            ResultSet personResult = null;
            try {
                logger.info("Getting person...");
                String query = "SELECT * FROM persons WHERE id_person = " + result.getInt("person_id");
                personStatement = DatabaseConnection.getConnection().createStatement();
                personResult = personStatement.executeQuery(query);
                logger.info("Query: " + query);
                scientist.setPersonId(PersonsDAO.getPerson(personResult));

            } catch (SQLException e) {
                logger.error("Error while getting person: " + e.getMessage());
            } finally {
                if (personResult != null) {
                    try {
                        personResult.close();
                    } catch (SQLException e) {
                        logger.error("Failed to close person result set: " + e.getMessage());
                    }
                }
                if (personStatement != null) {
                    try {
                        personStatement.close();
                    } catch (SQLException e) {
                        logger.error("Failed to close person statement: " + e.getMessage());
                    }
                }
            }

            scientist.setTitle(result.getString("title"));
            scientist.setRepresentInstitute(result.getString("represent_institute"));
            // find section
            Statement sectionStatement = null;
            ResultSet sectionResult = null;
            try {
                String query = "SELECT * FROM sections WHERE id_section = " + result.getInt("works_in_section");
                sectionStatement = DatabaseConnection.getConnection().createStatement();
                sectionResult = sectionStatement.executeQuery(query);
                SectionsEntity section = SectionsDAO.getSection(sectionResult);
                scientist.setWorksInSection(section);
            } catch (SQLException e) {
                logger.error("Error while getting section: " + e.getMessage());
            } finally {
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
        } catch (SQLException e) {
            logger.error("Error while setting scientist info: " + e.getMessage());
        }
    }
    protected static void deleteScientist(ScientistsEntity scientist) {
        EntityManager entityManager = DatabaseConnection.getEntityManager();
        List<ResearchesEntity> researches = entityManager.createQuery("SELECT r FROM ResearchesEntity r WHERE r.scientistId = :scientistId", ResearchesEntity.class)
                .setParameter("scientistId", scientist)
                .getResultList();

        if (!researches.isEmpty()) {
            logger.error("Cannot delete scientist, because there are researches assigned to this scientist.");
            CRUD.showErrorMessage("Cannot delete scientist, because there are researches assigned to this scientist.");
            return;
        }
        entityManager.getTransaction().begin();
        entityManager.remove(scientist);
        entityManager.getTransaction().commit();

        ScientistsView.showScientistsRecordsList();
        clearAll();
        logger.info("Scientist deleted: " + scientist.getPersonId().getFirstName() + " " + scientist.getPersonId().getLastName());
    }

    public static void clearAll() {
        selectedPerson = null;
        selectedSection = null;
        scientistsFormController.clearForm();
        scientistsFormController.resetFields();
    }

    public static void updateScientist(ScientistsEntity scientist) {
        scientist.setTitle(scientistsFormController.getTitle());
        scientist.setRepresentInstitute(scientistsFormController.getRepresentsInstitution());
    }

    public static List<PersonsEntity> getListOfAvailablePersons(ScientistsEntity scientist) {
        StringBuilder query = new StringBuilder("SELECT * FROM persons WHERE id_person NOT IN (SELECT person_id FROM scientists UNION SELECT person_id FROM employees)");
        if (scientist != null) {
            query.append(" AND id_person != ");
            if (selectedPerson != null) {
                query.append(selectedPerson.getIdPerson());
                logger.info("Query: " + query);
            } else {
                query.append(scientist.getPersonId().getIdPerson());
                logger.info("Query: " + query);
            }
        }
        List<PersonsEntity> availablePersons = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query.toString());
            while (!result.isAfterLast()) {
                availablePersons.add(PersonsDAO.getPerson(result));
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
        return availablePersons;
    }

    public static List<SectionsEntity> getListOfAvailableSections(ScientistsEntity scientist) {
        StringBuilder query = new StringBuilder("SELECT * FROM sections");
        if (scientist != null) {
            query.append(" WHERE id_section != ");
            if (selectedSection != null) {
                query.append(selectedSection.getIdSection());
            } else {
                query.append(scientist.getWorksInSection().getIdSection());
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
}
