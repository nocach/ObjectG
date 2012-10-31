package org.objectg.gen.cycle;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;

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
