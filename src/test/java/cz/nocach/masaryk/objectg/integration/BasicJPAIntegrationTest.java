package cz.nocach.masaryk.objectg.integration;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.fixtures.Person;
import cz.nocach.masaryk.objectg.fixtures.Person2Address;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

/**
 * User: __nocach
 * Date: 12.10.12
 */
public class BasicJPAIntegrationTest {

    @Test
    public void basic(){
        Person generated = ObjectG.unique(Person.class, ObjectG.config().backReferenceCycle().done());
        Person2Address person2Address = generated.getEmployee2Addresses().get(0);
        Person person = person2Address.getPerson();
        assertEquals("should use mappedBy jpa hint", generated, person);
        Person owner = person2Address.getOwner();
        assertNotSame("for @OneToOne at Person2Address.owner distinct Person should have been generated",
                generated, owner);
        Person dependentPerson = person2Address.getDependentPersons().iterator().next();
        assertNotSame("for relations without hind new entities should be generated. But was the same as generated"
                , generated, dependentPerson);
        assertNotSame("for relations without hind new entities should be generated. But was the same as owner"
                , owner, dependentPerson);

    }
}
