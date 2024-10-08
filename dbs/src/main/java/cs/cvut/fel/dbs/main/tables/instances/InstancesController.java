package cs.cvut.fel.dbs.main.tables.instances;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.InstancesEntity;
import cs.cvut.fel.dbs.entities.InstancesEntityPK;
import cs.cvut.fel.dbs.entities.PlantsEntity;
import cs.cvut.fel.dbs.entities.SectionsEntity;
import cs.cvut.fel.dbs.main.CRUD;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;

public class InstancesController {
    private static final Logger logger = LogManager.getLogger(InstancesController.class);
    protected static boolean checkForm(InstancesFormController form, InstancesEntity instance) {
        CRUD.showErrorMessage("");
        if (form.getInstanceNameField().getText().isEmpty()) {
            CRUD.showErrorMessage("Instance name cannot be empty.");
            return false;
        }
        if (form.getInstanceAgeField().getText().isEmpty()) {
            CRUD.showErrorMessage("Instance age cannot be empty.");
            return false;
        }
        // check if temperature and light values are numbers
        try {
            Integer.parseInt(form.getInstanceAgeField().getText());
        } catch (NumberFormatException e) {
            CRUD.showErrorMessage("Age must be a number.");
            return false;
        }
        if (form.getInstanceAge() < 0) {
            CRUD.showErrorMessage("Age must be a positive number.");
        }
        if (InstancesDAO.selectedPlant == null) {
            if (instance == null || instance.getPlant() == null) {
                CRUD.showErrorMessage("No plant selected.");
            }
        }
        if (InstancesDAO.selectedSection == null) {
            if (instance == null || instance.getSection() == null) {
                CRUD.showErrorMessage("No section selected.");
            }
        }
        return CRUD.isErrorMessageEmpty();
    }
    protected static void unselectPlant() {
        InstancesDAO.selectedPlant = null;
    }

    protected static void selectPlant(PlantsEntity plant) {
        InstancesDAO.selectedPlant = plant;
    }

    protected static void selectSection(SectionsEntity section) {
        InstancesDAO.selectedSection = section;
    }

    protected static void unselectSection() {
        InstancesDAO.selectedSection = null;
    }

    protected static void addNewInstance() {
        if (!checkForm(InstancesDAO.instancesFormController, null)) {
            return;
        }
        try {
            EntityManager entityManager = DatabaseConnection.getEntityManager();

            InstancesEntity instance = new InstancesEntity();
            InstancesDAO.updateInstance(instance);

            //already checked in checkForm
            if (InstancesDAO.selectedSection != null) {
                instance.setSection(InstancesDAO.selectedSection);
            }
            if (InstancesDAO.selectedPlant != null) {
                instance.setPlant(InstancesDAO.selectedPlant);
            }

            // try to find existing instance (name + plant + section should be unique)
            InstancesEntity instanceInDb = entityManager.find(InstancesEntity.class, new InstancesEntityPK(instance.getInstanceName(), instance.getPlant().getIdPlant(), instance.getSection().getIdSection()));
            // if the instance already exists, show error message
            if (instanceInDb != null) {
                CRUD.showErrorMessage("Same instance already exists.");
                return;
            }

            entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(instance);
            entityManager.getTransaction().commit();

            InstancesView.showInstancesRecordsList();
            InstancesView.showInstanceInfo(instance);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            InstancesView.showInstancesRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        InstancesDAO.clearAll();
    }

    protected static void editInstance(InstancesEntity instance) {
        if (!checkForm(InstancesDAO.instancesFormController, instance)) {
            return;
        }
        try {
            EntityManager entityManager = DatabaseConnection.getEntityManager();

            InstancesEntity oldInstance = entityManager.find(InstancesEntity.class, new InstancesEntityPK(instance.getInstanceName(), instance.getPlant().getIdPlant(), instance.getSection().getIdSection()));

            InstancesDAO.updateInstance(instance);

            if (InstancesDAO.selectedSection != null) {
                instance.setSection(InstancesDAO.selectedSection);
            }
            if (InstancesDAO.selectedPlant != null) {
                instance.setPlant(InstancesDAO.selectedPlant);
            }

            // try to find existing instance (name + plant + section should be unique)
            InstancesEntity instanceInDb = entityManager.find(InstancesEntity.class, new InstancesEntityPK(instance.getInstanceName(), instance.getPlant().getIdPlant(), instance.getSection().getIdSection()));
            if (instanceInDb != null) {
                // if the instance is the same as the one we are editing, it is ok
                if (!instanceInDb.equals(oldInstance)) {
                    CRUD.showErrorMessage("Same instance already exists.");
                    return;
                }
            }

            entityManager.getTransaction().begin();
            entityManager.merge(instance);
            if (oldInstance != null) {
                entityManager.remove(oldInstance);
            }
            entityManager.getTransaction().commit();

            InstancesView.showInstancesRecordsList();
            InstancesView.showInstanceInfo(instance);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            InstancesView.showInstancesRecordsList();
            CRUD.showErrorMessage("Failed: " + e.getMessage());
        }
        InstancesDAO.clearAll();
    }
}
