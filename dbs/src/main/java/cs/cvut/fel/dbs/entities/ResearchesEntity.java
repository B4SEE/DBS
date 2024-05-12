package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "researches", schema = "public", catalog = "virycele")
public class ResearchesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_research")
    private int idResearch;
    @Basic
    @Column(name = "research_name")
    private String researchName;
    @ManyToOne
    @JoinColumn(name = "scientist_id", referencedColumnName = "id_scientist", nullable = false)
    private ScientistsEntity scientistId;

    public int getIdResearch() {
        return idResearch;
    }

    public void setIdResearch(int idResearch) {
        this.idResearch = idResearch;
    }

    public String getResearchName() {
        return researchName;
    }

    public void setResearchName(String researchName) {
        this.researchName = researchName;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchesEntity that = (ResearchesEntity) o;
        return idResearch == that.idResearch && scientistId == that.scientistId && Objects.equals(researchName, that.researchName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idResearch, researchName, scientistId);
    }

    public ScientistsEntity getScientistId() {
        return scientistId;
    }

    public void setScientistId(ScientistsEntity scientistsByScientistId) {
        this.scientistId = scientistsByScientistId;
    }
}
