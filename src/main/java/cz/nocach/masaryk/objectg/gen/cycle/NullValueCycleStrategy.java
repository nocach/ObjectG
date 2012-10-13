package cz.nocach.masaryk.objectg.gen.cycle;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;

/**
 * User: __nocach
 * Date: 30.9.12
 */
public class NullValueCycleStrategy implements CycleStrategy {
    @Override
    public Object generateForCycle(GenerationConfiguration configuration, GenerationContext context) {
        return null;
    }

    @Override
    public boolean shouldGenerateValueInCollection() {
        return false;
    }
}
