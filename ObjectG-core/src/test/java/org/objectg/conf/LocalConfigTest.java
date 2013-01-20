package org.objectg.conf;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.objectg.GoodToHave;
import org.objectg.ObjectG;
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
public class LocalConfigTest {

	@Before
	public void setup(){
		ObjectG.configLocal(ObjectG
				.config()
				.when(IPerson.class).useClass(Person.class)
				.setObjectsInCollection(2)
				.done());
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
			,ObjectG.config().when(Tour.class).setValue(null));

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
	public void localConfigurationWillWorkInDerivedClass(){
		final TestCaseChild testCase = new TestCaseChild();
		//setupParent is in parent
		testCase.setupParent();

		final Tour generatedTourParent = testCase.generateParent();
		assertEquals("generated from parent: parent configuration should be applied",
				TestCaseParent.OBJECTS_IN_COLLECTION, generatedTourParent.getStops().size());
		final Tour generatedTourChild = testCase.generateChild();
		assertEquals("generated from child: parent configuration should be applied",
				TestCaseParent.OBJECTS_IN_COLLECTION, generatedTourChild.getStops().size());
	}

	@Test
	public void localConfigurationCanBeOverridenInDerivedClass(){
		final TestCaseChild testCase = new TestCaseChild();
		testCase.setupParent();
		testCase.setupChild();

		final Tour generatedTourParent = testCase.generateParent();
		assertEquals("generated from parent: child configuration should have no effect on how parent's generation is done",
				TestCaseParent.OBJECTS_IN_COLLECTION, generatedTourParent.getStops().size());
		final Tour generatedTourChild = testCase.generateChild();
		assertEquals("generated from child: child configuration should override parent's configuration",
				TestCaseChild.OBJECTS_IN_COLLECTION, generatedTourChild.getStops().size());
	}


	public static class TestCaseParent{

		public static final int OBJECTS_IN_COLLECTION = 0;

		public void setupParent(){
			ObjectG.configLocal(ObjectG.config().setObjectsInCollection(OBJECTS_IN_COLLECTION));
		}

		public Tour generateParent(){
			return ObjectG.unique(Tour.class);
		}

	}

	public static class TestCaseChild extends TestCaseParent{

		public static final int OBJECTS_IN_COLLECTION = 2;

		public void setupChild(){
			ObjectG.configLocal(ObjectG.config().setObjectsInCollection(OBJECTS_IN_COLLECTION));
		}

		public Tour generateChild(){
			return ObjectG.unique(Tour.class);
		}
	}

}
