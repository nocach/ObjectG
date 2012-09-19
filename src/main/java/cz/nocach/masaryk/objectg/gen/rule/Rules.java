package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.gen.conf.OngoingConfiguration;

/**
 * <p>
 *     Factory for rules used in configuration through {@link cz.nocach.masaryk.objectg.gen.conf.SetterConfigurator}
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
}
