package org.objectg.gen;

import org.objectg.conf.GenerationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *     Interface used to generate values for specific types.
 * </p>
 * <p>
 * User: __nocach
 * Date: 26.8.12
 * </p>
 */
public abstract class Generator {
    private static final Logger logger = LoggerFactory.getLogger(Generator.class);
    /**
     * generate new value for the passed type
     *
     * @param configuration current configuration for generation
     * @param context context under which to generate value
     * @return new value
     */
    public final <T> T generate(GenerationConfiguration configuration, GenerationContext<T> context){
        try{
			configuration.init();
            final GenerationRule rule = configuration.getRule(context);
            if (rule != null){
                return returnFromRule(configuration, context, rule);
            }

            if (context.isCycle()) {
                return returnFromCycle(configuration, context);
            }

            T result = generateValue(configuration, context);
            return result;
        }
        finally {
            if (context.isPushed()) context.pop();
        }
    }

    private <T> T returnFromCycle(GenerationConfiguration configuration, GenerationContext<T> context) {
        if (logger.isDebugEnabled()){
            logger.debug("found cycle when generating class="+context.getClassThatIsGenerated()
                    +" in hierarchy=\n" + context.dumpHierarchy());
        }
        Object valueForCycle = configuration.getCycleStrategy().generateForCycle(configuration, context);
        return (T)valueForCycle;
    }

    private <T> T returnFromRule(GenerationConfiguration configuration, GenerationContext<T> context, GenerationRule rule) {
        logger.debug("found rule for conf="+configuration+" context="+context);
        return (T)rule.getValue(configuration, context);
    }

    /**
     *
     * @param configuration configuration under which to generate value
     * @param context type of the value to generate
     * @param <T>
     * @return new generated value for the passed configuration
     */
    protected abstract <T> T generateValue(GenerationConfiguration configuration, GenerationContext<T> context);

    /**
     *
     * @param type
     * @return true if this generator can generate value for the passed type,
     *          false otherwise
     */
    public abstract boolean supportsType(Class type);
}
