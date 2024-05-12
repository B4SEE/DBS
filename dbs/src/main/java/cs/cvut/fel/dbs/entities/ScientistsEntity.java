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
    @Column(name = "represent_institute")
    private String representInstitute;
    @Basic
    @Column(name = "title")
    private String title;
    @OneToMany(mappedBy = "scientistId")
    private Collection<ResearchesEntity> researchesByIdScientist;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id_person", nullable = false)
    private PersonsEntity personId;
    @ManyToOne
    @JoinColumn(name = "works_in_section", referencedColumnName = "id_section")
    private SectionsEntity worksInSection;

    public int getIdScientist() {
        return idScientist;
    }

    public void setIdScientist(int idScientist) {
        this.idScientist = idScientist;
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

    public PersonsEntity getPersonId() {
        return personId;
    }

    public void setPersonId(PersonsEntity personsByPersonId) {
        this.personId = personsByPersonId;
    }

    public SectionsEntity getWorksInSection() {
        return worksInSection;
    }

    public void setWorksInSection(SectionsEntity sectionsByWorksInSection) {
        this.worksInSection = sectionsByWorksInSection;
    }
}
