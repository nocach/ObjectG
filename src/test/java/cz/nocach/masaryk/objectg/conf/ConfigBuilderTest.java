package cz.nocach.masaryk.objectg.conf;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.conf.exception.ConfigurationException;
import cz.nocach.masaryk.objectg.fixtures.Person;
import cz.nocach.masaryk.objectg.fixtures.Tour;
import cz.nocach.masaryk.objectg.gen.rule.Rules;
import cz.nocach.masaryk.objectg.matcher.ContextMatchers;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;

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
