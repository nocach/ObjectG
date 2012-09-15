package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.gen.Generator;
import org.junit.Test;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.any;

/**
 * User: __nocach
 * Date: 1.9.12
 */
public class FromListGenerationRuleTest {

    @Test
    public void canGenerateByRule(){
        FromListGenerationRule listGenerationRule = new FromListGenerationRule("one", "two", "three");

        Generator generator = listGenerationRule.getGenerator();

        assertTrue("returned generator should support String", generator.supportsType(String.class));
        assertFalse("returned generator should not support other classes then String",
                generator.supportsType(Integer.class));

        String generatedString = generator.generate(String.class);
        assertTrue("returned generator should generate from rule values",
                asList("one", "two", "three").contains(generatedString));
    }

    @Test
    public void valuesGeneratedAreCyclic(){
        FromListGenerationRule listGenerationRule = new FromListGenerationRule(1,2);
        Generator generator = listGenerationRule.getGenerator();

        assertEquals((Integer)1, generator.generate(Integer.class));
        assertEquals((Integer)2, generator.generate(Integer.class));
        assertEquals((Integer)1, generator.generate(Integer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfNoValues(){
        new FromListGenerationRule();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfNoNotNullValues(){
        new FromListGenerationRule((String)null);
    }

    @Test
    public void noThrowIfNullValuesExist(){
        new FromListGenerationRule(null, "notNull");
    }
}
