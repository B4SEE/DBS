package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "plantspreferredclimatetypes", schema = "public", catalog = "virycele")
@IdClass(PlantspreferredclimatetypesEntityPK.class)
public class PlantspreferredclimatetypesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_plant")
    private int idPlant;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_climate_type")
    private int idClimateType;
    @ManyToOne
    @JoinColumn(name = "id_plant", referencedColumnName = "id_plant", nullable = false)
    private PlantsEntity plantsByIdPlant;
    @ManyToOne
    @JoinColumn(name = "id_climate_type", referencedColumnName = "id_climate_type", nullable = false)
    private ClimatetypesEntity climatetypesByIdClimateType;

    public int getIdPlant() {
        return idPlant;
    }

    public void setIdPlant(int idPlant) {
        this.idPlant = idPlant;
    }

    public int getIdClimateType() {
        return idClimateType;
    }

    public void setIdClimateType(int idClimateType) {
        this.idClimateType = idClimateType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantspreferredclimatetypesEntity that = (PlantspreferredclimatetypesEntity) o;
        return idPlant == that.idPlant && idClimateType == that.idClimateType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlant, idClimateType);
    }

    public PlantsEntity getPlantsByIdPlant() {
        return plantsByIdPlant;
    }

    public void setPlantsByIdPlant(PlantsEntity plantsByIdPlant) {
        this.plantsByIdPlant = plantsByIdPlant;
    }

    public ClimatetypesEntity getClimatetypesByIdClimateType() {
        return climatetypesByIdClimateType;
    }

    public void setClimatetypesByIdClimateType(ClimatetypesEntity climatetypesByIdClimateType) {
        this.climatetypesByIdClimateType = climatetypesByIdClimateType;
    }
}
