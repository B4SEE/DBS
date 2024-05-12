package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "scientists", schema = "public", catalog = "virycele")
public class ScientistsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_scientist")
    private int idScientist;
    @Basic
    @Column(name = "person_id")
    private int personId;
    @Basic
    @Column(name = "represent_institute")
    private String representInstitute;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "works_in_section")
    private Integer worksInSection;
    @OneToMany(mappedBy = "scientistsByScientistId")
    private Collection<ResearchesEntity> researchesByIdScientist;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id_person", nullable = false)
    private PersonsEntity personsByPersonId;
    @ManyToOne
    @JoinColumn(name = "works_in_section", referencedColumnName = "id_section")
    private SectionsEntity sectionsByWorksInSection;

    public int getIdScientist() {
        return idScientist;
    }

    public void setIdScientist(int idScientist) {
        this.idScientist = idScientist;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getRepresentInstitute() {
        return representInstitute;
    }

    public void setRepresentInstitute(String representInstitute) {
        this.representInstitute = representInstitute;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getWorksInSection() {
        return worksInSection;
    }

    public void setWorksInSection(Integer worksInSection) {
        this.worksInSection = worksInSection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScientistsEntity that = (ScientistsEntity) o;
        return idScientist == that.idScientist && personId == that.personId && Objects.equals(representInstitute, that.representInstitute) && Objects.equals(title, that.title) && Objects.equals(worksInSection, that.worksInSection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idScientist, personId, representInstitute, title, worksInSection);
    }

    public Collection<ResearchesEntity> getResearchesByIdScientist() {
        return researchesByIdScientist;
    }

    public void setResearchesByIdScientist(Collection<ResearchesEntity> researchesByIdScientist) {
        this.researchesByIdScientist = researchesByIdScientist;
    }

    public PersonsEntity getPersonsByPersonId() {
        return personsByPersonId;
    }

    public void setPersonsByPersonId(PersonsEntity personsByPersonId) {
        this.personsByPersonId = personsByPersonId;
    }

    public SectionsEntity getSectionsByWorksInSection() {
        return sectionsByWorksInSection;
    }

    public void setSectionsByWorksInSection(SectionsEntity sectionsByWorksInSection) {
        this.sectionsByWorksInSection = sectionsByWorksInSection;
    }
}
