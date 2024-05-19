package cs.cvut.fel.dbs.entities;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "persons", schema = "public", catalog = "virycele")
public class PersonsEntity {
    private static final Logger logger = LogManager.getLogger(PersonsEntity.class);
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
    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id_address", nullable = false)
    private AddressesEntity addressId;

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


    public AddressesEntity getAddressId() {
        return addressId;
    }

    public void setAddressId(AddressesEntity addressesByAddressId) {
        this.addressId = addressesByAddressId;
    }
    public void setPhoneNumber(String phoneNumber) {
        PhonenumbersEntity phone = new PhonenumbersEntity();
        phone.setPhoneNumber(phoneNumber);
        phone.setPersonId(this);
        // create record in phone_numbers table
        String query = "INSERT INTO phonenumbers (phone_number, person_id) VALUES ('" + phoneNumber + "', " + this.getIdPerson() + ")";
        Statement statement = null;
        try {
            statement = DatabaseConnection.getConnection().createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            logger.error("Error while setting phone numbers: " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                logger.error("Error while closing statement: " + e.getMessage());
            }
        }
    }
    public List<String> getPhoneNumbersList() {
        List<String> phoneNumbers = new ArrayList<>();
        Statement statement = null;
        ResultSet result = null;
        try {
            String query = "SELECT phone_number FROM phonenumbers WHERE person_id = " + this.getIdPerson();
            statement = DatabaseConnection.getConnection().createStatement();
            result = statement.executeQuery(query);
            while (result.next()) {
                phoneNumbers.add(result.getString("phone_number"));
            }
        } catch (Exception e) {
            logger.error("Error while getting phone numbers: " + e.getMessage());
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                logger.error("Error while closing statement: " + e.getMessage());
            }
        }
        return phoneNumbers;
    }
}
