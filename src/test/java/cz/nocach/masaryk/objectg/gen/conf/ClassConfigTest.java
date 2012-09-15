package cz.nocach.masaryk.objectg.gen.conf;

import cz.nocach.masaryk.objectg.ObjectG;
import org.junit.Test;

import static cz.nocach.masaryk.objectg.gen.rule.Rules.fromList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * User: __nocach
 * Date: 15.9.12
 */
public class ClassConfigTest {
    @Test
    public void basic(){
            fail("to be done");
        Person building = ObjectG.config(Person.class);
        building.setName(fromList("configuredValue"));
//
        //TODO: config uvnitr bude aktualizovat hodnoty konfigurace v GeneratorRegistry

        Person person = ObjectG.unique(Person.class);
        assertEquals("configuredValue", person.getName());
    }



    public static class Person{
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
