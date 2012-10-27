package cz.nocach.masaryk.objectg.fixtures.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * User: __nocach
 * Date: 29.9.12
 */
@Entity
public class Person implements IPerson {
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getMiddleName() {
        return middleName;
    }

    @Override
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean isCustomer() {
        return isCustomer;
    }

    @Override
    public void setCustomer(boolean customer) {
        isCustomer = customer;
    }

    @Override
    public boolean isAgent() {
        return isAgent;
    }

    @Override
    public void setAgent(boolean agent) {
        isAgent = agent;
    }

    @Override
    public boolean isGuide() {
        return isGuide;
    }

    @Override
    public void setGuide(boolean guide) {
        isGuide = guide;
    }

    @Override
    public List<Person2Address> getEmployee2Addresses() {
        return employee2Addresses;
    }

    @Override
    public void setEmployee2Addresses(List<Person2Address> employee2Addresses) {
        this.employee2Addresses = employee2Addresses;
    }

}
