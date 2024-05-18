package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "persons", schema = "public", catalog = "virycele")
public class PersonsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_person")
    private int idPerson;
    @Basic
    @Column(name = "last_name")
    private String lastName;
    @Basic
    @Column(name = "first_name")
    private String firstName;
    @Basic
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @OneToMany(mappedBy = "person")
    private Collection<EmployeesEntity> employees;
    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id_address", nullable = false)
    private AddressesEntity addressId;
    @OneToMany(mappedBy = "personId")
    private Collection<PhonenumbersEntity> phoneNumbers;
    @OneToMany(mappedBy = "personId")
    private Collection<ScientistsEntity> scientists;

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonsEntity that = (PersonsEntity) o;
        return idPerson == that.idPerson && addressId == that.addressId && Objects.equals(lastName, that.lastName) && Objects.equals(firstName, that.firstName) && Objects.equals(dateOfBirth, that.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPerson, lastName, firstName, dateOfBirth, addressId);
    }

    public Collection<EmployeesEntity> getEmployees() {
        return employees;
    }

    public void setEmployees(Collection<EmployeesEntity> employeesByIdPerson) {
        this.employees = employeesByIdPerson;
    }

    public AddressesEntity getAddressId() {
        return addressId;
    }

    public void setAddressId(AddressesEntity addressesByAddressId) {
        this.addressId = addressesByAddressId;
    }

    public Collection<PhonenumbersEntity> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Collection<PhonenumbersEntity> phonenumbersByIdPerson) {
        this.phoneNumbers = phonenumbersByIdPerson;
    }

    public Collection<ScientistsEntity> getScientists() {
        return scientists;
    }

    public void setScientists(Collection<ScientistsEntity> scientistsByIdPerson) {
        this.scientists = scientistsByIdPerson;
    }
}
