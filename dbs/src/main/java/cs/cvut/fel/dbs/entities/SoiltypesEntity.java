package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "soiltypes", schema = "public", catalog = "virycele")
public class SoiltypesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_soil_type")
    private int idSoilType;
    @Basic
    @Column(name = "soil_name")
    private String soilName;
    @Basic
    @Column(name = "soil_description")
    private String soilDescription;
    @OneToMany(mappedBy = "soiltypesByIdSoilType")
    private Collection<PlantspreferredsoiltypesEntity> plantspreferredsoiltypesByIdSoilType;
    @OneToMany(mappedBy = "soiltypesBySoilType")
    private Collection<SectionsEntity> sectionsByIdSoilType;

    public int getIdSoilType() {
        return idSoilType;
    }

    public void setIdSoilType(int idSoilType) {
        this.idSoilType = idSoilType;
    }

    public String getSoilName() {
        return soilName;
    }

    public void setSoilName(String soilName) {
        this.soilName = soilName;
    }

    public String getSoilDescription() {
        return soilDescription;
    }

    public void setSoilDescription(String soilDescription) {
        this.soilDescription = soilDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoiltypesEntity that = (SoiltypesEntity) o;
        return idSoilType == that.idSoilType && Objects.equals(soilName, that.soilName) && Objects.equals(soilDescription, that.soilDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSoilType, soilName, soilDescription);
    }

    public Collection<PlantspreferredsoiltypesEntity> getPlantspreferredsoiltypesByIdSoilType() {
        return plantspreferredsoiltypesByIdSoilType;
    }

    public void setPlantspreferredsoiltypesByIdSoilType(Collection<PlantspreferredsoiltypesEntity> plantspreferredsoiltypesByIdSoilType) {
        this.plantspreferredsoiltypesByIdSoilType = plantspreferredsoiltypesByIdSoilType;
    }

    public Collection<SectionsEntity> getSectionsByIdSoilType() {
        return sectionsByIdSoilType;
    }

    public void setSectionsByIdSoilType(Collection<SectionsEntity> sectionsByIdSoilType) {
        this.sectionsByIdSoilType = sectionsByIdSoilType;
    }
}
