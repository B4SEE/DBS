package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "instances", schema = "public", catalog = "virycele")
@IdClass(InstancesEntityPK.class)
public class InstancesEntity {
    @Id
    @Basic
    @Column(name = "instance_name")
    private String instanceName;
    @Id
    @Column(name = "plant", nullable = false)
    private int plantId;

    @Id
    @Column(name = "section_id", nullable = false)
    private int sectionId;
    @Basic
    @Column(name = "age")
    private int age;

    @ManyToOne
    @JoinColumn(name = "plant", referencedColumnName = "id_plant", nullable = false, insertable = false, updatable = false)
    private PlantsEntity plant;

    @ManyToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id_section", nullable = false, insertable = false, updatable = false)
    private SectionsEntity section;

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public PlantsEntity getPlant() {
        if (plant == null) {
            System.out.println("Plant is null for instance " + instanceName);
        }
        return plant;
    }

    public void setPlant(PlantsEntity plantsByPlant) {
        this.plant = plantsByPlant;
        this.plantId = plantsByPlant.getIdPlant();
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

    public SectionsEntity getSection() {
        return section;
    }

    public void setSection(SectionsEntity sectionsBySectionId) {
        this.section = sectionsBySectionId;
        this.sectionId = sectionsBySectionId.getIdSection();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstancesEntity that = (InstancesEntity) o;
        return plantId == that.plantId &&
                sectionId == that.sectionId &&
                age == that.age &&
                Objects.equals(instanceName, that.instanceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceName, plantId, sectionId, age);
    }
}
