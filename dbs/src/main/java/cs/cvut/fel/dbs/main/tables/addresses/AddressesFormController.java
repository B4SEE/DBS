package cs.cvut.fel.dbs.main.tables.addresses;

import cs.cvut.fel.dbs.entities.AddressesEntity;
import javafx.scene.control.TextField;

public class AddressesFormController {
    private final TextField addressCityField = new TextField();
    private final TextField addressStreetField = new TextField();
    private final TextField addressHouseNumberField = new TextField();
    private final TextField addressZipCodeField = new TextField();
    public String getAddressCity() {
        return addressCityField.getText();
    }
    public String getAddressStreet() {
        return addressStreetField.getText();
    }
    public String getAddressHouseNumber() {
        return addressHouseNumberField.getText();
    }
    public String getAddressZipCode() {
        return addressZipCodeField.getText();
    }
    public void setAddressCity(String addressCity) {
        addressCityField.setText(addressCity);
    }
    public void setAddressStreet(String addressStreet) {
        addressStreetField.setText(addressStreet);
    }
    public void setAddressHouseNumber(String addressHouseNumber) {
        addressHouseNumberField.setText(addressHouseNumber);
    }
    public void setAddressZipCode(String addressZipCode) {
        addressZipCodeField.setText(addressZipCode);
    }

    public TextField getAddressCityField() {
        return addressCityField;
    }

    public TextField getAddressStreetField() {
        return addressStreetField;
    }

    public TextField getAddressHouseNumberField() {
        return addressHouseNumberField;
    }

    public TextField getAddressZipCodeField() {
        return addressZipCodeField;
    }

    public void clearForm() {
        getAddressCityField().clear();
        getAddressStreetField().clear();
        getAddressHouseNumberField().clear();
        getAddressZipCodeField().clear();
    }
    public void resetFields() {
        getAddressCityField().setEditable(true);
        getAddressStreetField().setEditable(true);
        getAddressHouseNumberField().setEditable(true);
        getAddressZipCodeField().setEditable(true);
    }
    public void fillFormFields(AddressesEntity address) {
        setAddressCity(address.getCity());
        setAddressStreet(address.getStreet());
        setAddressHouseNumber(address.getHouseNumber());
        setAddressZipCode(address.getZipCode());
    }
}
