package org.objectg.conf;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.objectg.ObjectG;
import org.objectg.conf.localconf.ConfigurationProvider;
import org.objectg.fixtures.ClassWithIPerson;
import org.objectg.fixtures.domain.IPerson;
import org.objectg.fixtures.domain.Person;
import org.objectg.fixtures.domain.Tour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * User: __nocach
 * Date: 3.11.12
 */
public class LocalConfTest {
	@ConfigurationProvider
	private GenerationConfiguration setupConfiguration(){
		return ObjectG
				.config()
				.when(IPerson.class).useClass(Person.class)
				.setObjectsInCollection(2)
				.done();
	}

	@Before
	public void setup(){
		ObjectG.setupConfig(this);
	}

	//testOne and testTwo show that configuration is applied to all test methods
	@Test
	public void testOne(){
		assertLocalConfigurationWasApplied(ObjectG.unique(ClassWithIPerson.class));
	}

	//testOne and testTwo show that configuration is applied to all test methods
	@Test
	public void testTwo(){
		assertLocalConfigurationWasApplied(ObjectG.unique(ClassWithIPerson.class));
	}

	@Test
	public void testConfigurationWillBeMerged(){
		final ClassWithIPerson classWithPerson = ObjectG.unique(ClassWithIPerson.class
			,ObjectG.config().when(Tour.class).value(null));

		assertLocalConfigurationWasApplied(classWithPerson);
		assertNull(classWithPerson.getTour());
	}

	private void assertLocalConfigurationWasApplied(final ClassWithIPerson classWithPerson) {

		assertTrue("must be configured to be instance of Person",
				classWithPerson.getPerson() instanceof Person);
		assertNotNull("instance must be generated",
				classWithPerson.getPerson() instanceof Person);
	}

	@Test
	public void localConfigurationCanDefineObjectsInCollections(){
		final Person unique = ObjectG.unique(Person.class);
		assertEquals(2, unique.getEmployee2Addresses().size());
	}

	@Test
	public void localConfigurationObjectsInCollectionsCanBeOverridenWithNotDefaultValue(){
		final Person uniqueSize3 = ObjectG.unique(Person.class, ObjectG.config().setObjectsInCollection(3));

		assertEquals("uniqueSize3", 3, uniqueSize3.getEmployee2Addresses().size());
	}
	@Test
	public void localConfigurationObjectsInCollectionsCanBeOverridenWithDefaultValue(){
		final Person uniqueSize1 = ObjectG.unique(Person.class, ObjectG.config().setObjectsInCollection(1));

		assertEquals("uniqueSize1", 1, uniqueSize1.getEmployee2Addresses().size());
	}

	@Test
	@Ignore
	public void localConfigurationIsPresentInDerivedClass(){
		fail("TestCase.A has localConfiguration, TestCase.B extending A will also have localConfiguration from A");
	}

	@Test
	@Ignore
	public void multipleLocalConfigurationDefinitions(){
		fail("think if to disallow or allow such behaviour");
	}

}
