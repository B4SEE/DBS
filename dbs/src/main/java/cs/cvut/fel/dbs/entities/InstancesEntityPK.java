package cs.cvut.fel.dbs.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class InstancesEntityPK implements Serializable {
    @Column(name = "instance_name")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String instanceName;
    @Column(name = "plant")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int plant;
    @Column(name = "section_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sectionId;

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public int getPlant() {
        return plant;
    }

    public void setPlant(int plant) {
        this.plant = plant;
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
        return plant == that.plant && sectionId == that.sectionId && Objects.equals(instanceName, that.instanceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceName, plant, sectionId);
    }
}
