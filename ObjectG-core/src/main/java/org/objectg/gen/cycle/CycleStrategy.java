package org.objectg.gen.cycle;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;

/**
 * User: __nocach
 * Date: 30.9.12
 */
public interface CycleStrategy {
	/**
	 *
	 * @param configuration
	 * @param context
	 * @return
	 * @throws MustIgnoreCycleException can be thrown if this cycle should be ignored
	 */
    public Object generateForCycle(GenerationConfiguration configuration, GenerationContext context) throws MustIgnoreCycleException;

    /**
     *
     * @return true if can use this CycleStrategy to generate value for the collection on cycle
     *          false otherwise.
     *          Example: ClassA->ClassB->ClassC->List<ClassB>
     *          if you want for ClassC->List<ClassB> to be empty on cycle, then return false
     */
    public boolean shouldGenerateValueInCollection();
}
