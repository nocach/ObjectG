package cz.nocach.masaryk.objectg.gen.cycle;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.GenerationContext;

/**
 * User: __nocach
 * Date: 30.9.12
 */
public interface CycleStrategy {
    public Object generateForCycle(GenerationConfiguration configuration, GenerationContext context);
}
