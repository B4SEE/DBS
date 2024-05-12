package cs.cvut.fel.dbs.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class PlantspreferredclimatetypesEntityPK implements Serializable {
    @Column(name = "id_plant")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPlant;
    @Column(name = "id_climate_type")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idClimateType;

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
        PlantspreferredclimatetypesEntityPK that = (PlantspreferredclimatetypesEntityPK) o;
        return idPlant == that.idPlant && idClimateType == that.idClimateType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlant, idClimateType);
    }
}
