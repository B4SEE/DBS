package cs.cvut.fel.dbs.entities;

import java.io.Serializable;
import java.util.Objects;

public class InstancesEntityPK implements Serializable {
    private String instanceName;
    private int plantId;
    private int sectionId;

    public InstancesEntityPK() {
    }
    public InstancesEntityPK(String instanceName, int idPlant, int idSection) {
        this.instanceName = instanceName;
        this.plantId = idPlant;
        this.sectionId = idSection;
    }

    // Getters and setters
    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public int getPlantId() {
        return plantId;
    }

    public void setPlantId(int plantId) {
        this.plantId = plantId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstancesEntityPK that = (InstancesEntityPK) o;
        return plantId == that.plantId &&
                sectionId == that.sectionId &&
                Objects.equals(instanceName, that.instanceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceName, plantId, sectionId);
    }
}
