package cs.cvut.fel.dbs.main.tables.scientists;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.PersonsEntity;
import cs.cvut.fel.dbs.entities.ScientistsEntity;
import cs.cvut.fel.dbs.entities.SectionsEntity;
import cs.cvut.fel.dbs.main.CRUD;

import javax.persistence.EntityManager;

public class ScientistsController {
    protected static boolean checkForm(ScientistsFormController form, ScientistsEntity scientist) {
        CRUD.showErrorMessage("");
        if (form.getTitle().isEmpty()) {
            CRUD.showErrorMessage("Title cannot be empty.");
            return false;
        }
        if (ScientistsDAO.selectedPerson == null) {
            if (scientist == null || scientist.getPersonId() == null) {
                CRUD.showErrorMessage("No person selected.");
            }
        }
        return CRUD.isErrorMessageEmpty();
    }

    protected static void unselectPerson() {
        ScientistsDAO.selectedPerson = null;
    }
    protected static void selectPerson(PersonsEntity person) {
        ScientistsDAO.selectedPerson = person;
    }
    protected static void unselectSection() {
        ScientistsDAO.selectedSection = null;
    }
    protected static void deleteSection(SectionsEntity section) {
        ScientistsDAO.sectionToDelete = section;
    }
    protected static void cancelDeleteSection() {
        ScientistsDAO.sectionToDelete = null;
    }
    protected static void addNewScientist() {
        try {
            if (!checkForm(ScientistsDAO.scientistsFormController, null)) {
                return;
            }
            ScientistsEntity scientist = new ScientistsEntity();
            ScientistsDAO.updateScientist(scientist);

            if (ScientistsDAO.selectedPerson != null) {
                scientist.setPersonId(ScientistsDAO.selectedPerson);
            }
            if (ScientistsDAO.selectedSection != null) {
                scientist.setWorksInSection(ScientistsDAO.selectedSection);
            }
            if (ScientistsDAO.sectionToDelete != null) {
                if (scientist.getWorksInSection() != null && scientist.getWorksInSection().getIdSection() == ScientistsDAO.sectionToDelete.getIdSection()) {
                    scientist.setWorksInSection(null);
                }
            }

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(scientist);
            entityManager.getTransaction().commit();

            ScientistsView.showScientistsRecordsList();
            ScientistsView.showScientistInfo(scientist);
            return;
        } catch (Exception e) {
        ScientistsView.showScientistsRecordsList();
        CRUD.showErrorMessage("Failed: " + e.getMessage());
    }
        ScientistsDAO.clearAll();
    }
    protected static void editScientist(ScientistsEntity scientist) {
        try {
            if (!checkForm(ScientistsDAO.scientistsFormController, scientist)) {
                return;
            }
            ScientistsDAO.updateScientist(scientist);

            if (ScientistsDAO.selectedPerson != null) {
                scientist.setPersonId(ScientistsDAO.selectedPerson);
            }
            if (ScientistsDAO.selectedSection != null) {
                scientist.setWorksInSection(ScientistsDAO.selectedSection);
            }
            if (ScientistsDAO.sectionToDelete != null) {
                if (scientist.getWorksInSection() != null && scientist.getWorksInSection().getIdSection() == ScientistsDAO.sectionToDelete.getIdSection()) {
                    scientist.setWorksInSection(null);
                }
            }

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(scientist);
            entityManager.getTransaction().commit();

            ScientistsView.showScientistsRecordsList();
            ScientistsView.showScientistInfo(scientist);
            return;
        } catch (Exception e) {
            ScientistsView.showScientistsRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        ScientistsDAO.clearAll();
    }

    public static void selectSection(SectionsEntity section) {
        ScientistsDAO.selectedSection = section;
    }
}

