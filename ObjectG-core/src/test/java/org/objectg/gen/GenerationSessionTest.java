package org.objectg.gen;

import org.junit.BeforeClass;
import org.junit.Test;
import org.objectg.ObjectG;
import org.objectg.conf.OngoingRules;
import org.objectg.fixtures.domain.Person;
import org.objectg.fixtures.domain.Tour;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * User: __nocach
 * Date: 23.1.13
 */
public class GenerationSessionTest{
	private static Tour tourPrototype;

	@Test
	public void assertPersonOneAndTwoEqual(){
		Person person1 = generateInSession(Person.class);
		Person person2 = generateInSession(Person.class);

		assertNotSame("generated persons should be different", person1, person2);
		assertEquals("generated attributes are same between multiple generation sessions",
				person1.getFirstName(), person2.getFirstName());
		assertEquals("generated attributes are same between multiple generation sessions",
				person1.getLastName(), person2.getLastName());
		assertEquals("generated attributes are same between multiple generation sessions",
				person1.getMiddleName(), person2.getMiddleName());
		assertEquals("generated attributes are same between multiple generation sessions",
				person1.getId(), person2.getId());
		assertNotSame("should have differently generated address objects", person1.getEmployee2Addresses().get(0), person2.getEmployee2Addresses().get(0));
		assertEquals("generated attributes are same between multiple generation sessions",
				person1.getEmployee2Addresses().get(0).getName(), person2.getEmployee2Addresses().get(0).getName());
	}

	/**
	 * setupRule(), generateTour1(), generateTour2(), assertTourOneAndTwoEqualInRuledProperty()
	 * show that rule configured ONCE will behave THE SAME way in two different generation sessions
	 */
	@BeforeClass
	public static void setupRule(){
		tourPrototype = ObjectG.prototype(Tour.class);
		tourPrototype.setName(OngoingRules.sequence("one", "two"));
	}

	@Test
	public void assertTourOneAndTwoEqualInRuledProperty(){
		Tour tour1 = generateInSession(tourPrototype);
		Tour tour2 = generateInSession(tourPrototype);

		assertEquals("tour1.name should be one", "one", tour1.getName());
		assertEquals("tour2.name should be one", "one", tour2.getName());
	}

	private <T> T generateInSession(T prototype) {
		ObjectG.setup();
		T result = ObjectG.unique(prototype);
		ObjectG.teardown();
		return result;
	}

	private <T> T generateInSession(Class<T> clazz) {
		ObjectG.setup();
		T result = ObjectG.unique(clazz);
		ObjectG.teardown();
		return result;
	}

}
