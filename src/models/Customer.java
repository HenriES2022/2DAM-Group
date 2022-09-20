package models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author yeguo
 */
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String streetName;
    private String city;
    private String state;
    private Integer phone;
    private Integer zip;
    private String email;
    private List<?> customerAccounts;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public Integer getPhone() {
        return phone;
    }

    public Integer getZip() {
        return zip;
    }

    public String getEmail() {
        return email;
    }

    public List<?> getCustomerAccounts() {
        return customerAccounts;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCustomerAccounts(List<?> customerAccounts) {
        this.customerAccounts = customerAccounts;
    }

    public Customer() {
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.firstName);
        hash = 17 * hash + Objects.hashCode(this.lastName);
        hash = 17 * hash + Objects.hashCode(this.middleName);
        hash = 17 * hash + Objects.hashCode(this.streetName);
        hash = 17 * hash + Objects.hashCode(this.city);
        hash = 17 * hash + Objects.hashCode(this.state);
        hash = 17 * hash + Objects.hashCode(this.phone);
        hash = 17 * hash + Objects.hashCode(this.zip);
        hash = 17 * hash + Objects.hashCode(this.email);
        hash = 17 * hash + Objects.hashCode(this.customerAccounts);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Customer other = (Customer) obj;
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.middleName, other.middleName)) {
            return false;
        }
        if (!Objects.equals(this.streetName, other.streetName)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.phone, other.phone)) {
            return false;
        }
        if (!Objects.equals(this.zip, other.zip)) {
            return false;
        }
        return Objects.equals(this.customerAccounts, other.customerAccounts);
    }

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", middleName=" + middleName + ", streetName=" + streetName + ", city=" + city + ", state=" + state + ", phone=" + phone + ", zip=" + zip + ", email=" + email + ", customerAccounts=" + customerAccounts + '}';
    }

}
