package cz.nocach.masaryk.objectg.fixtures.domain;

import java.util.List;

/**
 * User: __nocach
 * Date: 23.10.12
 */
public interface IPerson {
    Long getId();

    void setId(Long id);

    String getFirstName();

    void setFirstName(String firstName);

    String getMiddleName();

    void setMiddleName(String middleName);

    String getLastName();

    void setLastName(String lastName);

    boolean isCustomer();

    void setCustomer(boolean customer);

    boolean isAgent();

    void setAgent(boolean agent);

    boolean isGuide();

    void setGuide(boolean guide);

    List<Person2Address> getEmployee2Addresses();

    void setEmployee2Addresses(List<Person2Address> employee2Addresses);
}
