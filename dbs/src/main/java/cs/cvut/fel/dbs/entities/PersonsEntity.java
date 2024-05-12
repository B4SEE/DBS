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
    @Basic
    @Column(name = "address_id")
    private int addressId;
    @OneToMany(mappedBy = "personsByPersonId")
    private Collection<EmployeesEntity> employeesByIdPerson;
    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id_address", nullable = false)
    private AddressesEntity addressesByAddressId;
    @OneToMany(mappedBy = "personsByPersonId")
    private Collection<PhonenumbersEntity> phonenumbersByIdPerson;
    @OneToMany(mappedBy = "personsByPersonId")
    private Collection<ScientistsEntity> scientistsByIdPerson;

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

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
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

    public Collection<EmployeesEntity> getEmployeesByIdPerson() {
        return employeesByIdPerson;
    }

    public void setEmployeesByIdPerson(Collection<EmployeesEntity> employeesByIdPerson) {
        this.employeesByIdPerson = employeesByIdPerson;
    }

    public AddressesEntity getAddressesByAddressId() {
        return addressesByAddressId;
    }

    public void setAddressesByAddressId(AddressesEntity addressesByAddressId) {
        this.addressesByAddressId = addressesByAddressId;
    }

    public Collection<PhonenumbersEntity> getPhonenumbersByIdPerson() {
        return phonenumbersByIdPerson;
    }

    public void setPhonenumbersByIdPerson(Collection<PhonenumbersEntity> phonenumbersByIdPerson) {
        this.phonenumbersByIdPerson = phonenumbersByIdPerson;
    }

    public Collection<ScientistsEntity> getScientistsByIdPerson() {
        return scientistsByIdPerson;
    }

    public void setScientistsByIdPerson(Collection<ScientistsEntity> scientistsByIdPerson) {
        this.scientistsByIdPerson = scientistsByIdPerson;
    }
}
