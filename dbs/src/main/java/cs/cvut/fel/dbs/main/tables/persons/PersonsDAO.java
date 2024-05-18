package cs.cvut.fel.dbs.main.tables.persons;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.PersonsEntity;
import cs.cvut.fel.dbs.main.tables.addresses.AddressesDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonsDAO {
    private static final Logger logger = LogManager.getLogger(PersonsDAO.class);
    public static List<PersonsEntity> getAllPersons() {
        List<PersonsEntity> persons = new ArrayList<>();
        try {
            String query = "SELECT * FROM persons";
            Statement statement = DatabaseConnection.getConnection().createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                PersonsEntity person = new PersonsEntity();
                setPersonInfo(result, person);
                persons.add(person);
            }
        } catch (Exception e) {
            logger.error("Error while getting all persons: " + e.getMessage());
        }
        return persons;
    }
    public static PersonsEntity getPerson(ResultSet personInfo) {
        if (personInfo == null) {
            return null;
        }
        PersonsEntity person = new PersonsEntity();
        try {
            if (personInfo.next()) {
                setPersonInfo(personInfo, person);
            }
        } catch (Exception e) {
            logger.error("Error while getting person: " + e.getMessage());
        }
        return person;
    }
    private static void setPersonInfo(ResultSet personInfo, PersonsEntity person) {
        try {
            person.setIdPerson(personInfo.getInt("id_person"));
            person.setFirstName(personInfo.getString("first_name"));
            person.setLastName(personInfo.getString("last_name"));
            person.setDateOfBirth(personInfo.getDate("date_of_birth"));
            String addressQuery = "SELECT * FROM person_address WHERE id_address = " + personInfo.getInt("address_id");
            Statement addressStatement = DatabaseConnection.getConnection().createStatement();
            ResultSet addressResult = addressStatement.executeQuery(addressQuery);
            person.setAddressId(AddressesDAO.getAddress(addressResult));
        } catch (Exception e) {
            logger.error("Error while setting person info: " + e.getMessage());
        }
    }
}
