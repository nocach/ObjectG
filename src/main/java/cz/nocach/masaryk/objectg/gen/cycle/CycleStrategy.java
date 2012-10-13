package cz.nocach.masaryk.objectg.gen.cycle;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;

/**
 * User: __nocach
 * Date: 30.9.12
 */
public interface CycleStrategy {
    public Object generateForCycle(GenerationConfiguration configuration, GenerationContext context);

    /**
     *
     * @return true if can use this CycleStrategy to generate value for the collection on cycle
     *          false otherwise.
     *          Example: ClassA->ClassB->ClassC->List<ClassB>
     *          if you want for ClassC->List<ClassB> to be empty on cycle, then return false
     */
    public boolean shouldGenerateValueInCollection();
}
