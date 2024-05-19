package cs.cvut.fel.dbs.main.tables.researches;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.ResearchesEntity;
import cs.cvut.fel.dbs.entities.ScientistsEntity;
import cs.cvut.fel.dbs.main.CRUD;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;

public class ResearchesController {
    private static final Logger logger = LogManager.getLogger(ResearchesController.class);
    protected static boolean checkForm(ResearchesFormController form, ResearchesEntity research) {
        CRUD.showErrorMessage("");
        if (form.getResearchNameField().getText().isEmpty()) {
            CRUD.showErrorMessage("Research name cannot be empty.");
            return false;
        }
        if (ResearchesDAO.selectedScientist == null) {
            if (research == null || research.getScientistId() == null) {
                CRUD.showErrorMessage("No section selected.");
            }
        }
        return CRUD.isErrorMessageEmpty();
    }
    protected static void selectScientist(ScientistsEntity scientist) {
        ResearchesDAO.selectedScientist = scientist;
    }
    protected static void unselectScientist() {
        ResearchesDAO.selectedScientist = null;
    }
    protected static void addNewResearch() {
        if (!checkForm(ResearchesDAO.researchesFormController, null)) {
            return;
        }
        try {
            ResearchesEntity research = new ResearchesEntity();
            ResearchesDAO.updateResearch(research);

            if (!ResearchesDAO.checkResearchNameIsUnique(research)) {
                CRUD.showErrorMessage("Research name must be unique.");
                return;
            }

            if (ResearchesDAO.selectedScientist != null) {
                research.setScientistId(ResearchesDAO.selectedScientist);
            }

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(research);
            entityManager.getTransaction().commit();

            ResearchesView.showResearchesRecordsList();
            ResearchesView.showResearchInfo(research);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            ResearchesView.showResearchesRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        ResearchesDAO.clearAll();
    }
    protected static void editResearch(ResearchesEntity research) {
        if (!checkForm(ResearchesDAO.researchesFormController, research)) {
            return;
        }
        try {
            ResearchesDAO.updateResearch(research);

            if (!ResearchesDAO.checkResearchNameIsUnique(research)) {
                CRUD.showErrorMessage("Research name must be unique.");
                return;
            }

            if (ResearchesDAO.selectedScientist != null) {
                research.setScientistId(ResearchesDAO.selectedScientist);
            }

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(research);
            entityManager.getTransaction().commit();

            ResearchesView.showResearchesRecordsList();
            ResearchesView.showResearchInfo(research);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            ResearchesView.showResearchesRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        ResearchesDAO.clearAll();
    }
}
