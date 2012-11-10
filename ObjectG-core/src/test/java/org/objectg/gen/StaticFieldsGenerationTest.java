package org.objectg.gen;

import org.junit.Test;
import org.objectg.ObjectG;
import org.objectg.fixtures.domain.Person;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * User: __nocach
 * Date: 21.10.12
 */
public class StaticFieldsGenerationTest {

    @Test
    public void staticFieldsAreSkipped(){
        ClassWithStatics generated = ObjectG.unique(ClassWithStatics.class);

        assertNull("static object field should be skipped", ClassWithStatics.getStaticPerson());
        assertNull("static primitive field should be skipped", ClassWithStatics.getStaticString());
        assertNotNull("not static field should be generated", generated.getNotStaticString());
    }

    public static class ClassWithStatics{
        private static String staticString;
        private static Person staticPerson;
        private String notStaticString;

        public static String getStaticString() {
            return staticString;
        }

        public static void setStaticString(String staticString) {
            ClassWithStatics.staticString = staticString;
        }

        public static Person getStaticPerson() {
            return staticPerson;
        }

        public static void setStaticPerson(Person staticPerson) {
            ClassWithStatics.staticPerson = staticPerson;
        }

        public String getNotStaticString() {
            return notStaticString;
        }

        public void setNotStaticString(String notStaticString) {
            this.notStaticString = notStaticString;
        }
    }
}
