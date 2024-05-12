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
    @Column(name = "employee_number")
    private String employeeNumber;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id_person", nullable = false)
    private PersonsEntity personId;
    @ManyToOne
    @JoinColumn(name = "is_supervised_by", referencedColumnName = "id_employee")
    private EmployeesEntity isSupervisedBy;
    @OneToMany(mappedBy = "isSupervisedBy")
    private Collection<EmployeesEntity> employeesByIdEmployee;
    @OneToMany(mappedBy = "isHandledBy")
    private Collection<SectionsEntity> sectionsByIdEmployee;

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
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

    public PersonsEntity getPersonId() {
        return personId;
    }

    public void setPersonId(PersonsEntity personsByPersonId) {
        this.personId = personsByPersonId;
    }

    public EmployeesEntity getIsSupervisedBy() {
        return isSupervisedBy;
    }

    public void setIsSupervisedBy(EmployeesEntity employeesByIsSupervisedBy) {
        this.isSupervisedBy = employeesByIsSupervisedBy;
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
