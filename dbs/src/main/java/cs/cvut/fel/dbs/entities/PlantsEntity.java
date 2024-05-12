package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "plants", schema = "public", catalog = "virycele")
public class PlantsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_plant")
    private int idPlant;
    @Basic
    @Column(name = "plant_name")
    private String plantName;
    @Basic
    @Column(name = "plant_type")
    private String plantType;
    @Basic
    @Column(name = "min_temperature")
    private int minTemperature;
    @Basic
    @Column(name = "max_temperature")
    private int maxTemperature;
    @Basic
    @Column(name = "min_light")
    private int minLight;
    @Basic
    @Column(name = "max_light")
    private int maxLight;
    @OneToMany(mappedBy = "plantsByPlant")
    private Collection<InstancesEntity> instancesByIdPlant;
    @ManyToMany
    @JoinTable(
            name = "plantspreferredclimatetypes",
            joinColumns = @JoinColumn(name = "id_plant"),
            inverseJoinColumns = @JoinColumn(name = "id_climate_type")
    )
    private Set<PlantspreferredclimatetypesEntity> plantspreferredclimatetypesByIdPlant;
    @ManyToMany
    @JoinTable(
            name = "plantspreferredsoiltypes",
            joinColumns = @JoinColumn(name = "id_plant"),
            inverseJoinColumns = @JoinColumn(name = "id_soil_type")
    )
    private Set<PlantspreferredsoiltypesEntity> plantspreferredsoiltypesByIdPlant;

    public int getIdPlant() {
        return idPlant;
    }

    public void setIdPlant(int idPlant) {
        this.idPlant = idPlant;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public int getMinLight() {
        return minLight;
    }

    public void setMinLight(int minLight) {
        this.minLight = minLight;
    }

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

    public Collection<InstancesEntity> getInstancesByIdPlant() {
        return instancesByIdPlant;
    }

    public void setInstancesByIdPlant(Collection<InstancesEntity> instancesByIdPlant) {
        this.instancesByIdPlant = instancesByIdPlant;
    }

    public Collection<PlantspreferredclimatetypesEntity> getPlantspreferredclimatetypesByIdPlant() {
        return plantspreferredclimatetypesByIdPlant;
    }

    public void setPlantspreferredclimatetypesByIdPlant(Collection<PlantspreferredclimatetypesEntity> plantspreferredclimatetypesByIdPlant) {
        this.plantspreferredclimatetypesByIdPlant = (Set<PlantspreferredclimatetypesEntity>) plantspreferredclimatetypesByIdPlant;
    }

    public Collection<PlantspreferredsoiltypesEntity> getPlantspreferredsoiltypesByIdPlant() {
        return plantspreferredsoiltypesByIdPlant;
    }

    public void setPlantspreferredsoiltypesByIdPlant(Collection<PlantspreferredsoiltypesEntity> plantspreferredsoiltypesByIdPlant) {
        this.plantspreferredsoiltypesByIdPlant = (Set<PlantspreferredsoiltypesEntity>) plantspreferredsoiltypesByIdPlant;
    }
}
