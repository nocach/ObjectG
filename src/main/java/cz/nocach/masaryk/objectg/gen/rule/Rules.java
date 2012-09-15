package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.gen.conf.OngoingConfiguration;

/**
 * User: __nocach
 * Date: 15.9.12
 */
public class Rules {
    public static  <T> T fromList(T... values){
        OngoingConfiguration.plannedRule = new FromListGenerationRule(values);
        return null;
    }
}
