package cs.cvut.fel.dbs.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class PlantspreferredsoiltypesEntityPK implements Serializable {
    @Column(name = "id_plant")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPlant;
    @Column(name = "id_soil_type")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSoilType;

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
        PlantspreferredsoiltypesEntityPK that = (PlantspreferredsoiltypesEntityPK) o;
        return idPlant == that.idPlant && idSoilType == that.idSoilType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlant, idSoilType);
    }
}
