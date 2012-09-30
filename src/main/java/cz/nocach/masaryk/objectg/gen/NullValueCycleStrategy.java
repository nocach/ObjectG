package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;

/**
 * User: __nocach
 * Date: 30.9.12
 */
public class NullValueCycleStrategy implements CycleStrategy {
    @Override
    public Object generateForCycle(GenerationConfiguration configuration, GenerationContext context) {
        return null;
    }
}
