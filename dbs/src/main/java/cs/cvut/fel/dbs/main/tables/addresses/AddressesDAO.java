package cs.cvut.fel.dbs.main.tables.addresses;

import cs.cvut.fel.dbs.db.DatabaseConnection;
import cs.cvut.fel.dbs.entities.AddressesEntity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressesDAO {
    private static final Logger logger = LogManager.getLogger(AddressesDAO.class);
    public static List<AddressesEntity> getAllAddresses() {
        List<AddressesEntity> addresses = new ArrayList<>();
        try {
            String query = "SELECT * FROM addresses";
            Statement statement = DatabaseConnection.getConnection().createStatement();
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                AddressesEntity address = new AddressesEntity();
                setAddressInfo(result, address);
                addresses.add(address);
            }
        } catch (Exception e) {
            logger.error("Error while getting all addresses: " + e.getMessage());
        }
        return addresses;
    }
    public static AddressesEntity getAddress(ResultSet addressInfo) {
        AddressesEntity address = new AddressesEntity();
        try {
            if (addressInfo.next()) {
                setAddressInfo(addressInfo, address);
            }
        } catch (Exception e) {
            logger.error("Error while getting address: " + e.getMessage());
        }
        return address;
    }
    private static void setAddressInfo(ResultSet addressInfo, AddressesEntity address) {
        try {
            address.setIdAddress(addressInfo.getInt("id_address"));
            address.setStreet(addressInfo.getString("street"));
            address.setCity(addressInfo.getString("city"));
            address.setZipCode(addressInfo.getString("zip_code"));
        } catch (Exception e) {
            logger.error("Error while setting address info: " + e.getMessage());
        }
    }
}
