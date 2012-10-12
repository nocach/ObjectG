package cz.nocach.masaryk.objectg.fixtures;

import javax.persistence.*;
import java.util.List;

/**
 * User: __nocach
 * Date: 29.9.12
 */
@Entity
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private boolean isCustomer;
    private boolean isAgent;
    private boolean isGuide;
    @OneToMany(mappedBy = "person")
    private List<Person2Address> employee2Addresses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isCustomer() {
        return isCustomer;
    }

    public void setCustomer(boolean customer) {
        isCustomer = customer;
    }

    public boolean isAgent() {
        return isAgent;
    }

    public void setAgent(boolean agent) {
        isAgent = agent;
    }

    public boolean isGuide() {
        return isGuide;
    }

    public void setGuide(boolean guide) {
        isGuide = guide;
    }

    public List<Person2Address> getEmployee2Addresses() {
        return employee2Addresses;
    }

    public void setEmployee2Addresses(List<Person2Address> employee2Addresses) {
        this.employee2Addresses = employee2Addresses;
    }

}
