package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "plantspreferredsoiltypes", schema = "public", catalog = "virycele")
@IdClass(PlantspreferredsoiltypesEntityPK.class)
public class PlantspreferredsoiltypesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_plant")
    private int idPlant;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_soil_type")
    private int idSoilType;
    @ManyToMany
    private PlantsEntity plantsByIdPlant;
    @ManyToOne
    @JoinColumn(name = "id_soil_type", referencedColumnName = "id_soil_type", nullable = false)
    private SoiltypesEntity soiltypesByIdSoilType;

    public int getIdPlant() {
        return idPlant;
    }

    public void setIdPlant(int idPlant) {
        this.idPlant = idPlant;
    }

    public int getIdSoilType() {
        return idSoilType;
    }

    public void setIdSoilType(int idSoilType) {
        this.idSoilType = idSoilType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantspreferredsoiltypesEntity that = (PlantspreferredsoiltypesEntity) o;
        return idPlant == that.idPlant && idSoilType == that.idSoilType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlant, idSoilType);
    }

    public PlantsEntity getPlantsByIdPlant() {
        return plantsByIdPlant;
    }

    public void setPlantsByIdPlant(PlantsEntity plantsByIdPlant) {
        this.plantsByIdPlant = plantsByIdPlant;
    }

    public SoiltypesEntity getSoiltypesByIdSoilType() {
        return soiltypesByIdSoilType;
    }

    public void setSoiltypesByIdSoilType(SoiltypesEntity soiltypesByIdSoilType) {
        this.soiltypesByIdSoilType = soiltypesByIdSoilType;
    }
}
