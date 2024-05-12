package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "sections", schema = "public", catalog = "virycele")
public class SectionsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_section")
    private int idSection;
    @Basic
    @Column(name = "section_name")
    private String sectionName;
    @Basic
    @Column(name = "geographical_coordinates")
    private String geographicalCoordinates;
    @Basic
    @Column(name = "temperature")
    private int temperature;
    @Basic
    @Column(name = "light")
    private int light;
    @OneToMany(mappedBy = "sectionId")
    private Collection<InstancesEntity> instancesByIdSection;
    @OneToMany(mappedBy = "worksInSection")
    private Collection<ScientistsEntity> scientistsByIdSection;
    @ManyToOne
    @JoinColumn(name = "soil_type", referencedColumnName = "id_soil_type", nullable = false)
    private SoiltypesEntity soilType;
    @ManyToOne
    @JoinColumn(name = "climate_type", referencedColumnName = "id_climate_type", nullable = false)
    private ClimatetypesEntity climateType;
    @ManyToOne
    @JoinColumn(name = "is_handled_by", referencedColumnName = "id_employee", nullable = false)
    private EmployeesEntity isHandledBy;

    public int getIdSection() {
        return idSection;
    }

    public void setIdSection(int idSection) {
        this.idSection = idSection;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getGeographicalCoordinates() {
        return geographicalCoordinates;
    }

    public void setGeographicalCoordinates(String geographicalCoordinates) {
        this.geographicalCoordinates = geographicalCoordinates;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionsEntity that = (SectionsEntity) o;
        return idSection == that.idSection && soilType == that.soilType && climateType == that.climateType && temperature == that.temperature && light == that.light && isHandledBy == that.isHandledBy && Objects.equals(sectionName, that.sectionName) && Objects.equals(geographicalCoordinates, that.geographicalCoordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSection, sectionName, geographicalCoordinates, soilType, climateType, temperature, light, isHandledBy);
    }

    public Collection<InstancesEntity> getInstancesByIdSection() {
        return instancesByIdSection;
    }

    public void setInstancesByIdSection(Collection<InstancesEntity> instancesByIdSection) {
        this.instancesByIdSection = instancesByIdSection;
    }

    public Collection<ScientistsEntity> getScientistsByIdSection() {
        return scientistsByIdSection;
    }

    public void setScientistsByIdSection(Collection<ScientistsEntity> scientistsByIdSection) {
        this.scientistsByIdSection = scientistsByIdSection;
    }

    public SoiltypesEntity getSoilType() {
        return soilType;
    }

    public void setSoilType(SoiltypesEntity soiltypesBySoilType) {
        this.soilType = soiltypesBySoilType;
    }

    public ClimatetypesEntity getClimateType() {
        return climateType;
    }

    public void setClimateType(ClimatetypesEntity climatetypesByClimateType) {
        this.climateType = climatetypesByClimateType;
    }

    public EmployeesEntity getIsHandledBy() {
        return isHandledBy;
    }

    public void setIsHandledBy(EmployeesEntity employeesByIsHandledBy) {
        this.isHandledBy = employeesByIsHandledBy;
    }
}
