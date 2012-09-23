package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.conf.OngoingConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationRule;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *     Factory for rules used in configuration through {@link cz.nocach.masaryk.objectg.conf.SetterConfigurator}
 * </p>
 * <p>
 * User: __nocach
 * Date: 15.9.12
 * </p>
 */
public class Rules {
    public static  <T> T fromList(T... values){
        OngoingConfiguration.plannedRule = new FromListGenerationRule(values);
        return null;
    }

    public static GenerationRule specificConfiguration(GenerationConfiguration specificConfiguration) {
        return new SpecificConfigurationGenerationRule(specificConfiguration);
    }

    public static <T> List<T> listDefinition(Class clazzOfObjects) {
        return listDefinition(clazzOfObjects, 1);
    }
    public static <T> List<T> listDefinition(Class clazzOfObjects, int size) {
        Assert.isTrue(size >= 0, "size must be >= 0");
        OngoingConfiguration.plannedRule = new GenericListGenerationRule(clazzOfObjects, size);
        return null;
    }

    public static <T> List<T> listDefinition(Class clazzOfObjects, Object... values) {
        OngoingConfiguration.plannedRule = new SingleValueGenerationRule(Arrays.asList(values));
        return null;
    }
}
