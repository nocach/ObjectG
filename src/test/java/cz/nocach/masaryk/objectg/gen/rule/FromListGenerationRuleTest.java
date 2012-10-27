package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import org.junit.Test;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * User: __nocach
 * Date: 1.9.12
 */
public class FromListGenerationRuleTest {

    @Test
    public void canGenerateByRule(){
        FromListGenerationRule<String> listGenerationRule = new FromListGenerationRule("one", "two", "three");

        String generatedString = listGenerationRule.getValue(new GenerationConfiguration(), GenerationContext.createRoot(String.class));
        assertTrue("returned generator should generate from rule values",
                asList("one", "two", "three").contains(generatedString));
    }

    @Test
    public void valuesGeneratedAreCyclic(){
        FromListGenerationRule listGenerationRule = new FromListGenerationRule(1,2);

        assertEquals(1, listGenerationRule.getValue(new GenerationConfiguration(), GenerationContext.createRoot(Integer.class)));
        assertEquals(2, listGenerationRule.getValue(new GenerationConfiguration(), GenerationContext.createRoot(Integer.class)));
        assertEquals(1, listGenerationRule.getValue(new GenerationConfiguration(), GenerationContext.createRoot(Integer.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIfNoValues(){
        new FromListGenerationRule();
    }

    @Test
    public void noThrowIfNullValuesExist(){
        new FromListGenerationRule(null, "notNull");
    }
}
