package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "instances", schema = "public", catalog = "virycele")
@IdClass(InstancesEntityPK.class)
public class InstancesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "instance_name")
    private String instanceName;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "plant")
    private int plant;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "section_id")
    private int sectionId;
    @Basic
    @Column(name = "age")
    private int age;
    @ManyToOne
    @JoinColumn(name = "plant", referencedColumnName = "id_plant", nullable = false)
    private PlantsEntity plantsByPlant;
    @ManyToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id_section", nullable = false)
    private SectionsEntity sectionsBySectionId;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstancesEntity that = (InstancesEntity) o;
        return plant == that.plant && sectionId == that.sectionId && age == that.age && Objects.equals(instanceName, that.instanceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceName, plant, sectionId, age);
    }

    public PlantsEntity getPlantsByPlant() {
        return plantsByPlant;
    }

    public void setPlantsByPlant(PlantsEntity plantsByPlant) {
        this.plantsByPlant = plantsByPlant;
    }

    public SectionsEntity getSectionsBySectionId() {
        return sectionsBySectionId;
    }

    public void setSectionsBySectionId(SectionsEntity sectionsBySectionId) {
        this.sectionsBySectionId = sectionsBySectionId;
    }
}
