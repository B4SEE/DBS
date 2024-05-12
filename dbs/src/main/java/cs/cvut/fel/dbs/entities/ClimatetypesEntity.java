package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "climatetypes", schema = "public", catalog = "virycele")
public class ClimatetypesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_climate_type")
    private int idClimateType;
    @Basic
    @Column(name = "climate_name")
    private String climateName;
    @Basic
    @Column(name = "climate_description")
    private String climateDescription;
    @OneToMany(mappedBy = "climatetypesByIdClimateType")
    private Collection<PlantspreferredclimatetypesEntity> plantspreferredclimatetypesByIdClimateType;
    @OneToMany(mappedBy = "climatetypesByClimateType")
    private Collection<SectionsEntity> sectionsByIdClimateType;

    public int getIdClimateType() {
        return idClimateType;
    }

    public void setIdClimateType(int idClimateType) {
        this.idClimateType = idClimateType;
    }

    public String getClimateName() {
        return climateName;
    }

    public void setClimateName(String climateName) {
        this.climateName = climateName;
    }

    public String getClimateDescription() {
        return climateDescription;
    }

    public void setClimateDescription(String climateDescription) {
        this.climateDescription = climateDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClimatetypesEntity that = (ClimatetypesEntity) o;
        return idClimateType == that.idClimateType && Objects.equals(climateName, that.climateName) && Objects.equals(climateDescription, that.climateDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idClimateType, climateName, climateDescription);
    }

    public Collection<PlantspreferredclimatetypesEntity> getPlantspreferredclimatetypesByIdClimateType() {
        return plantspreferredclimatetypesByIdClimateType;
    }

    public void setPlantspreferredclimatetypesByIdClimateType(Collection<PlantspreferredclimatetypesEntity> plantspreferredclimatetypesByIdClimateType) {
        this.plantspreferredclimatetypesByIdClimateType = plantspreferredclimatetypesByIdClimateType;
    }

    public Collection<SectionsEntity> getSectionsByIdClimateType() {
        return sectionsByIdClimateType;
    }

    public void setSectionsByIdClimateType(Collection<SectionsEntity> sectionsByIdClimateType) {
        this.sectionsByIdClimateType = sectionsByIdClimateType;
    }
}
