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
    @Basic
    @Column(name = "age")
    private int age;
    @ManyToOne
    @JoinColumn(name = "plant", referencedColumnName = "id_plant", nullable = false)
    private PlantsEntity plant;
    @ManyToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id_section", nullable = false)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstancesEntity that = (InstancesEntity) o;
        return plant == that.plant && section == that.section && age == that.age && Objects.equals(instanceName, that.instanceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceName, plant, section, age);
    }

    public PlantsEntity getPlant() {
        return plant;
    }

    public void setPlant(PlantsEntity plantsByPlant) {
        this.plant = plantsByPlant;
    }

    public SectionsEntity getSection() {
        return section;
    }

    public void setSection(SectionsEntity sectionsBySectionId) {
        this.section = sectionsBySectionId;
    }
}
