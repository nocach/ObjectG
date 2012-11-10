package org.objectg.integration;

import org.junit.Test;
import org.objectg.ObjectG;
import org.objectg.fixtures.domain.Departure;
import org.objectg.fixtures.domain.Person;
import org.objectg.fixtures.domain.Person2Address;

import static junit.framework.Assert.*;

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
        Person owner = person2Address.getOwner();
        assertNull("owner should be null, because nullOnCycleStrategy must be used for it", owner);
        assertNotNull("dependentPersons should be set", person2Address.getDependentPersons());
        assertEquals("dependentPersons should be empty", 0, person2Address.getDependentPersons().size());
    }

    @Test
    public void willDiscoverEntitiesReferencingFromCollections(){
        //Departure has List<Reservation> and Reservation points to more classes
        ObjectG.unique(Departure.class);
    }
}
