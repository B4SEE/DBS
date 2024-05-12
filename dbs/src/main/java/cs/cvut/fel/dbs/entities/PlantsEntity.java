package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@javax.persistence.Table(name = "plants", schema = "public", catalog = "virycele")
public class PlantsEntity {
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

    @Basic
    @Column(name = "plant_name")
    private String plantName;

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    @Basic
    @Column(name = "plant_type")
    private String plantType;

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    @Basic
    @Column(name = "min_temperature")
    private int minTemperature;

    public int getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    @Basic
    @Column(name = "max_temperature")
    private int maxTemperature;

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    @Basic
    @Column(name = "min_light")
    private int minLight;

    public int getMinLight() {
        return minLight;
    }

    public void setMinLight(int minLight) {
        this.minLight = minLight;
    }

    @Basic
    @Column(name = "max_light")
    private int maxLight;

    public int getMaxLight() {
        return maxLight;
    }

    public void setMaxLight(int maxLight) {
        this.maxLight = maxLight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantsEntity that = (PlantsEntity) o;
        return idPlant == that.idPlant && minTemperature == that.minTemperature && maxTemperature == that.maxTemperature && minLight == that.minLight && maxLight == that.maxLight && Objects.equals(plantName, that.plantName) && Objects.equals(plantType, that.plantType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlant, plantName, plantType, minTemperature, maxTemperature, minLight, maxLight);
    }
}
