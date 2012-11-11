package org.objectg.integration;

import org.junit.Test;
import org.objectg.ObjectG;
import org.objectg.fixtures.domain.Departure;
import org.objectg.fixtures.domain.Person;
import org.objectg.fixtures.domain.Person2Address;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * User: __nocach
 * Date: 12.10.12
 */
public class BasicJPAIntegrationTest {

    @Test
    public void basic(){
        Person generated = ObjectG.unique(Person.class);
        Person2Address person2Address = generated.getEmployee2Addresses().get(0);
        Person person = person2Address.getPerson();
        assertEquals("should use mappedBy jpa hint", generated, person);
        assertNotNull("dependentPersons should be set", person2Address.getDependentPersons());
    }

    @Test
    public void willDiscoverEntitiesReferencingFromCollections(){
        //Departure has List<Reservation> and Reservation points to more classes
        ObjectG.unique(Departure.class);
    }
}
