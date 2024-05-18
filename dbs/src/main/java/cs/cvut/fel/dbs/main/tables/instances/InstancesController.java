package cs.cvut.fel.dbs.main.tables.instances;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.InstancesEntity;
import cs.cvut.fel.dbs.entities.PlantsEntity;
import cs.cvut.fel.dbs.entities.SectionsEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;

public class InstancesController {
    private static final Logger logger = LogManager.getLogger(InstancesController.class);
    protected static boolean checkForm(InstancesFormController form, InstancesEntity instance) {
        InstancesView.showErrorMessage("");
        if (form.getInstanceNameField().getText().isEmpty()) {
            InstancesView.showErrorMessage("Instance name cannot be empty.");
            return false;
        }
        if (form.getInstanceAgeField().getText().isEmpty()) {
            InstancesView.showErrorMessage("Instance age cannot be empty.");
            return false;
        }
        // check if temperature and light values are numbers
        try {
            Integer.parseInt(form.getInstanceAgeField().getText());
        } catch (NumberFormatException e) {
            InstancesView.showErrorMessage("Min light must be a number.");
            return false;
        }
        if (form.getInstanceAge() < 0) {
            InstancesView.showErrorMessage("Age must be a positive number.");
        }
        if (InstancesDAO.selectedPlant == null) {
            if (instance == null || instance.getPlant() == null) {
                InstancesView.showErrorMessage("No plant selected.");
            }
        }
        if (InstancesDAO.selectedSection == null) {
            if (instance == null || instance.getSection() == null) {
                InstancesView.showErrorMessage("No section selected.");
            }
        }
        return InstancesView.isErrorMessageEmpty();
    }
    protected static void unselectPlant(PlantsEntity plant) {
        InstancesDAO.selectedPlant = null;
    }

    protected static void selectPlant(PlantsEntity plant) {
        InstancesDAO.selectedPlant = plant;
    }

    protected static void selectSection(SectionsEntity section) {
        InstancesDAO.selectedSection = section;
    }

    protected static void unselectSection(SectionsEntity section) {
        InstancesDAO.selectedSection = null;
    }

    public static void addNewInstance() {
        if (!checkForm(InstancesDAO.instancesFormController, null)) {
            return;
        }
        try {
            InstancesEntity instance = new InstancesEntity();
            InstancesDAO.updateInstance(instance);

            instance.setPlant(InstancesDAO.selectedPlant);
            instance.setSection(InstancesDAO.selectedSection);

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(instance);
            entityManager.getTransaction().commit();

            InstancesView.showInstancesRecordsList();
            InstancesView.showInstanceInfo(instance);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            InstancesView.showInstancesRecordsList();
            InstancesView.showErrorMessage("Failed: " + e.getMessage());
        }
        InstancesDAO.clearAll();
    }

    public static void editInstance(InstancesEntity instance) {
        if (!checkForm(InstancesDAO.instancesFormController, instance)) {
            return;
        }
        try {
            InstancesDAO.updateInstance(instance);

            instance.setPlant(InstancesDAO.selectedPlant);
            instance.setSection(InstancesDAO.selectedSection);

            EntityManager entityManager = DatabaseConnection.getEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(instance);
            entityManager.getTransaction().commit();

            InstancesView.showInstancesRecordsList();
            InstancesView.showInstanceInfo(instance);
            return;
        } catch (Exception e) {
            logger.error("Failed: " + e.getMessage());
            InstancesView.showInstancesRecordsList();
            InstancesView.showErrorMessage("Failed: " + e.getMessage());
        }
        InstancesDAO.clearAll();
    }
}
