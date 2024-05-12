package cs.cvut.fel.dbs.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
@javax.persistence.Table(name = "plantspreferredclimatetypes", schema = "public", catalog = "virycele")
public class PlantspreferredclimatetypesEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @javax.persistence.Column(name = "id_plant")
    private int idPlant;

    public int getIdPlant() {
        return idPlant;
    }

    public void setIdPlant(int idPlant) {
        this.idPlant = idPlant;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @javax.persistence.Column(name = "id_climate_type")
    private int idClimateType;

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
}
