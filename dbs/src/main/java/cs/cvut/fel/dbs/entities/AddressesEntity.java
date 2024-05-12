package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "addresses", schema = "public", catalog = "virycele")
public class AddressesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_address")
    private int idAddress;
    @Basic
    @Column(name = "city")
    private String city;
    @Basic
    @Column(name = "street")
    private String street;
    @Basic
    @Column(name = "house_number")
    private String houseNumber;
    @Basic
    @Column(name = "zip_code")
    private String zipCode;
    @OneToMany(mappedBy = "addressesByAddressId")
    private Collection<PersonsEntity> personsByIdAddress;

    public int getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(int idAddress) {
        this.idAddress = idAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressesEntity that = (AddressesEntity) o;
        return idAddress == that.idAddress && Objects.equals(city, that.city) && Objects.equals(street, that.street) && Objects.equals(houseNumber, that.houseNumber) && Objects.equals(zipCode, that.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAddress, city, street, houseNumber, zipCode);
    }

    public Collection<PersonsEntity> getPersonsByIdAddress() {
        return personsByIdAddress;
    }

    public void setPersonsByIdAddress(Collection<PersonsEntity> personsByIdAddress) {
        this.personsByIdAddress = personsByIdAddress;
    }
}
