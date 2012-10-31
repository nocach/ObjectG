package org.objectg.gen;

import org.junit.Test;
import org.objectg.ObjectG;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: __nocach
 * Date: 21.10.12
 */
public class FinalFieldsGenerationTest {

    @Test
    public void finalFieldsAreSkipped(){
        ClassWithFinals generated = ObjectG.unique(ClassWithFinals.class);

        assertEquals("final field should be skipped", "default", generated.finalString);
        assertEquals("final static field should be skipped", "default", ClassWithFinals.finalStaticString);
        assertNotNull("not final field should be generated", generated.notFinalString);
    }

    public static class ClassWithFinals{
        public static final String finalStaticString = "default";
        public final String finalString = "default";
        public String notFinalString;
    }
}
