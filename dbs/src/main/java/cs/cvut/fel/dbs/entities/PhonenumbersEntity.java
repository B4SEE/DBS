package cs.cvut.fel.dbs.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "phonenumbers", schema = "public", catalog = "virycele")
public class PhonenumbersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "phone_number")
    private String phoneNumber;
    @Basic
    @Column(name = "person_id")
    private int personId;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id_person", nullable = false)
    private PersonsEntity personsByPersonId;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhonenumbersEntity that = (PhonenumbersEntity) o;
        return personId == that.personId && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber, personId);
    }

    public PersonsEntity getPersonsByPersonId() {
        return personsByPersonId;
    }

    public void setPersonsByPersonId(PersonsEntity personsByPersonId) {
        this.personsByPersonId = personsByPersonId;
    }
}
