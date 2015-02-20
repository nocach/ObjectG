package org.objectg.conf;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;
import org.objectg.fixtures.domain.Person;

/**
 * User: kaloshin
 * Date: 6.3.13
 */
public class DepthTest extends BaseObjectGTest {

	@Test
	public void onDepthEdgeListIsPopulated() {
		final ClassWithCollection unique = ObjectG.unique(ClassWithCollection.class, ObjectG.config().depth(1));

		assertEquals("collection should not be empty", 1, unique.getPersonsList().size());
		assertNotNull("collection should have not null elements", unique.getPersonsList().get(0));
	}

	@Test
	public void onDepthEdgeSetIsPopulated() {
		final ClassWithCollection unique = ObjectG.unique(ClassWithCollection.class, ObjectG.config().depth(1));

		assertEquals("collection should not be empty", 1, unique.getPersonsSet().size());
		assertNotNull("collection should have not null elements", unique.getPersonsSet().iterator().next());
	}

	@Test
	public void onDepthEdgeMapIsPopulated() {
		final ClassWithCollection unique = ObjectG.unique(ClassWithCollection.class, ObjectG.config().depth(1));

		assertEquals("collection should not be empty", 1, unique.getPersonsMap().size());
		assertNotNull("map should have not null keys", unique.getPersonsMap().entrySet().iterator().next());
		assertNotNull("map should have not null values", unique.getPersonsMap().values().iterator().next());
	}

	@Test
	public void objectsAfterEdgeHaveEmptyCollections() {
		final ClassWithCollection unique = ObjectG.unique(ClassWithCollection.class, ObjectG.config().depth(1));

		assertTrue(unique.getPersonsList().get(0).getEmployee2Addresses().isEmpty());
	}

	@Test
	public void depthIsNotEffectedByCollections() {
		ClassWithCollection unique = ObjectG.unique(ClassWithCollection.class, ObjectG.config().depth(2));
		//unique = depth(0)
		//unique.getPersonsList().get(0) = depth(1)
		//unique.getPersonsList().get(0).getEmployee2Addresses().get(0) = depth(2)
		assertTrue(unique.getPersonsList().get(0).getEmployee2Addresses().get(0).getDependentPersons().isEmpty());
	}

	public static class ClassWithCollection {
		private List<Person> personsList;
		private Set<Person> personsSet;
		private Map<Person, Person> personsMap;

		public List<Person> getPersonsList() {
			return personsList;
		}

		public void setPersonsList(final List<Person> personsList) {
			this.personsList = personsList;
		}

		public Set<Person> getPersonsSet() {
			return personsSet;
		}

		public void setPersonsSet(final Set<Person> personsSet) {
			this.personsSet = personsSet;
		}

		public Map<Person, Person> getPersonsMap() {
			return personsMap;
		}

		public void setPersonsMap(final Map<Person, Person> personsMap) {
			this.personsMap = personsMap;
		}
	}

}
