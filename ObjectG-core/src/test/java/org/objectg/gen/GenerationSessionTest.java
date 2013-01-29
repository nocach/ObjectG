package org.objectg.gen;

import org.junit.BeforeClass;
import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;
import org.objectg.conf.OngoingRules;
import org.objectg.fixtures.domain.Person;
import org.objectg.fixtures.domain.Tour;

/**
 * User: __nocach
 * Date: 23.1.13
 */
public class GenerationSessionTest extends BaseObjectGTest{
	private static Person person1;
	private static Person person2;
	private static Tour tour1;
	private static Tour tour2;
	private static Tour tourPrototype;

	@Test
	public void generatePersonOne(){
		person1 = ObjectG.unique(Person.class);
	}

	@Test
	public void generatePersonTwo(){
		person2 = ObjectG.unique(Person.class);
	}

	@Test
	public void assertPersonOneAndTwoEqual(){
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
	public void generateTour1(){
		tour1 = ObjectG.unique(tourPrototype);
	}

	@Test
	public void generateTour2(){
		tour2 = ObjectG.unique(tourPrototype);
	}

	@Test
	public void assertTourOneAndTwoEqualInRuledProperty(){
		assertEquals("tour1.name should be one", "one", tour1.getName());
		assertEquals("tour2.name should be one", "one", tour2.getName());
	}

}
