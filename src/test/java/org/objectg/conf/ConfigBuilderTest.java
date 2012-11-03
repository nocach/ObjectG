package org.objectg.conf;

import java.util.Map;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Ignore;
import org.junit.Test;
import org.objectg.ObjectG;
import org.objectg.conf.exception.ConfigurationException;
import org.objectg.fixtures.domain.IPerson;
import org.objectg.fixtures.domain.Person;
import org.objectg.fixtures.domain.Tour;
import org.objectg.fixtures.domain.TourSeason;
import org.objectg.gen.rule.Rules;
import org.objectg.matcher.ContextMatchers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertThat;

/**
 * User: __nocach
 * Date: 12.10.12
 */
public class ConfigBuilderTest {

    @Test(expected = ConfigurationException.class)
    public void throwsWhenTryingToPassNotPrototypeObject(){
        //user passes object that was not created by ObjectG.prototype()
        ObjectG.unique(String.class, new PrototypeConfigTest.Person());
    }

    @Test
    public void canConfigureRuleWithMatcher(){
        Person generated = ObjectG.unique(Person.class,
                ObjectG.config().when(ContextMatchers.instancesOf(String.class)).rule(Rules.value("someValue")));

        assertEquals("someValue", generated.getFirstName());
        assertEquals("someValue", generated.getMiddleName());
        assertEquals("someValue", generated.getLastName());

    }

    @Test
    public void canConfigureNullObjects(){
        Tour tour = ObjectG.unique(Tour.class, ObjectG.config().noObjects());

        assertNull("not primitive should be null", tour.getSeason());
        assertEquals("collections should be empty", 0, tour.getStops().size());
    }

    @Test
    public void canConfigureNullObjectsClassWithMap(){
        ClassWithMap classWithMap = ObjectG.unique(ClassWithMap.class, ObjectG.config().noObjects());

        assertEquals("map should be empty", 0, classWithMap.getMap().size());
    }

    @Test
    public void canSimplyConfigValueForClass(){
        TourSeason expectedSeason = new TourSeason();
        Tour generated = ObjectG.unique(Tour.class,
                ObjectG.config().forClass(TourSeason.class).value(expectedSeason));

        assertEquals(expectedSeason, generated.getSeason());
    }

    @Test
    public void canSimplyConfigNullForClass(){
        Tour generated = ObjectG.unique(Tour.class,
                ObjectG.config().forClass(TourSeason.class).setNull());

        assertNull(generated.getSeason());
    }

    @Test
    public void canUsePropertyExpressionToSetValue(){
        Person unique = ObjectG.unique(Person.class
                , ObjectG.config()
                .backReferenceCycle()
                .when("employee2Addresses[0].owner.firstName")
                .value("setByExpression"));

        assertEquals("setByExpression", unique.getEmployee2Addresses().get(0).getOwner().getFirstName());
    }

    @Test
    public void canUsePropertyExpressionToUsePrototype(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.setFirstName("fromPrototype");
        Person unique = ObjectG.unique(Person.class
                , ObjectG.config()
                .when("employee2Addresses[0].owner")
                .usePrototype(prototype));

        assertEquals("fromPrototype", unique.getEmployee2Addresses().get(0).getOwner().getFirstName());
    }

    @Test
    public void canUsePropertyExpressionToSpecifyRule(){
        Person prototype = ObjectG.prototype(Person.class);
        prototype.setFirstName("fromPrototype");
        Person unique = ObjectG.unique(Person.class
                , ObjectG.config()
                .when("employee2Addresses")
                .rule(Rules.emptyCollection()));

        assertEquals(0, unique.getEmployee2Addresses().size());
    }

	@Test
	public void canSpecifyImplementation(){
		ClassWithIPerson classWithIPerson = ObjectG.unique(ClassWithIPerson.class,
				ObjectG.config().when(IPerson.class).useClass(Person.class));

		assertThat(classWithIPerson.getPerson(), IsInstanceOf.instanceOf(Person.class));
	}

    @Test
    @Ignore
    public void canInferPropertyTypeForExpressionMatchingCollection(){
        fail("when(employee2Addresses[0]) should work");
    }

	public static class ClassWithIPerson{
		private IPerson person;

		public IPerson getPerson() {
			return person;
		}

		public void setPerson(final IPerson person) {
			this.person = person;
		}
	}

    public static class ClassWithMap{
        private Map map;

        public Map getMap() {
            return map;
        }

        public void setMap(Map map) {
            this.map = map;
        }
    }
}
