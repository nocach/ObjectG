package cz.nocach.masaryk.objectg.gen.cycle;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;

/**
 * <p>
 *     Strategy that will use previously generated object if it is generated again in same generation session.
 * </p>
 * <p>
 * User: __nocach
 * Date: 2.10.12
 * </p>
 */
public class BackReferenceCycleStrategy implements CycleStrategy {
    public Object generateForCycle(GenerationConfiguration configuration, GenerationContext context) {
        return context.getLastGeneratedObject(context.getClassThatIsGenerated());
    }

    @Override
    public boolean shouldGenerateValueInCollection() {
        return true;
    }
}
