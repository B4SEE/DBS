package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "employees", schema = "public", catalog = "virycele")
public class EmployeesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_employee")
    private int idEmployee;
    @Basic
    @Column(name = "person_id")
    private int personId;
    @Basic
    @Column(name = "employee_number")
    private String employeeNumber;
    @Basic
    @Column(name = "is_supervised_by")
    private Integer isSupervisedBy;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id_person", nullable = false)
    private PersonsEntity personsByPersonId;
    @ManyToOne
    @JoinColumn(name = "is_supervised_by", referencedColumnName = "id_employee")
    private EmployeesEntity employeesByIsSupervisedBy;
    @OneToMany(mappedBy = "employeesByIsSupervisedBy")
    private Collection<EmployeesEntity> employeesByIdEmployee;
    @OneToMany(mappedBy = "employeesByIsHandledBy")
    private Collection<SectionsEntity> sectionsByIdEmployee;

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Integer getIsSupervisedBy() {
        return isSupervisedBy;
    }

    public void setIsSupervisedBy(Integer isSupervisedBy) {
        this.isSupervisedBy = isSupervisedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeesEntity that = (EmployeesEntity) o;
        return idEmployee == that.idEmployee && personId == that.personId && Objects.equals(employeeNumber, that.employeeNumber) && Objects.equals(isSupervisedBy, that.isSupervisedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmployee, personId, employeeNumber, isSupervisedBy);
    }

    public PersonsEntity getPersonsByPersonId() {
        return personsByPersonId;
    }

    public void setPersonsByPersonId(PersonsEntity personsByPersonId) {
        this.personsByPersonId = personsByPersonId;
    }

    public EmployeesEntity getEmployeesByIsSupervisedBy() {
        return employeesByIsSupervisedBy;
    }

    public void setEmployeesByIsSupervisedBy(EmployeesEntity employeesByIsSupervisedBy) {
        this.employeesByIsSupervisedBy = employeesByIsSupervisedBy;
    }

    public Collection<EmployeesEntity> getEmployeesByIdEmployee() {
        return employeesByIdEmployee;
    }

    public void setEmployeesByIdEmployee(Collection<EmployeesEntity> employeesByIdEmployee) {
        this.employeesByIdEmployee = employeesByIdEmployee;
    }

    public Collection<SectionsEntity> getSectionsByIdEmployee() {
        return sectionsByIdEmployee;
    }

    public void setSectionsByIdEmployee(Collection<SectionsEntity> sectionsByIdEmployee) {
        this.sectionsByIdEmployee = sectionsByIdEmployee;
    }
}
