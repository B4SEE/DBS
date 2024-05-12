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
    @Column(name = "soil_type")
    private int soilType;
    @Basic
    @Column(name = "climate_type")
    private int climateType;
    @Basic
    @Column(name = "temperature")
    private int temperature;
    @Basic
    @Column(name = "light")
    private int light;
    @Basic
    @Column(name = "is_handled_by")
    private int isHandledBy;
    @OneToMany(mappedBy = "sectionsBySectionId")
    private Collection<InstancesEntity> instancesByIdSection;
    @OneToMany(mappedBy = "sectionsByWorksInSection")
    private Collection<ScientistsEntity> scientistsByIdSection;
    @ManyToOne
    @JoinColumn(name = "soil_type", referencedColumnName = "id_soil_type", nullable = false)
    private SoiltypesEntity soiltypesBySoilType;
    @ManyToOne
    @JoinColumn(name = "climate_type", referencedColumnName = "id_climate_type", nullable = false)
    private ClimatetypesEntity climatetypesByClimateType;
    @ManyToOne
    @JoinColumn(name = "is_handled_by", referencedColumnName = "id_employee", nullable = false)
    private EmployeesEntity employeesByIsHandledBy;

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

    public int getSoilType() {
        return soilType;
    }

    public void setSoilType(int soilType) {
        this.soilType = soilType;
    }

    public int getClimateType() {
        return climateType;
    }

    public void setClimateType(int climateType) {
        this.climateType = climateType;
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

    public int getIsHandledBy() {
        return isHandledBy;
    }

    public void setIsHandledBy(int isHandledBy) {
        this.isHandledBy = isHandledBy;
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

    public SoiltypesEntity getSoiltypesBySoilType() {
        return soiltypesBySoilType;
    }

    public void setSoiltypesBySoilType(SoiltypesEntity soiltypesBySoilType) {
        this.soiltypesBySoilType = soiltypesBySoilType;
    }

    public ClimatetypesEntity getClimatetypesByClimateType() {
        return climatetypesByClimateType;
    }

    public void setClimatetypesByClimateType(ClimatetypesEntity climatetypesByClimateType) {
        this.climatetypesByClimateType = climatetypesByClimateType;
    }

    public EmployeesEntity getEmployeesByIsHandledBy() {
        return employeesByIsHandledBy;
    }

    public void setEmployeesByIsHandledBy(EmployeesEntity employeesByIsHandledBy) {
        this.employeesByIsHandledBy = employeesByIsHandledBy;
    }
}
