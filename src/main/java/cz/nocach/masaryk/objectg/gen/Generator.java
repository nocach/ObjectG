package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
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
        final GenerationRule rule = configuration.getRule(context);
        if (rule != null){
            logger.debug("found rule for conf="+configuration+" context="+context);
            return (T)rule.getValue(configuration, context);
        }
        return (T) generateValue(configuration, context);
    }

    /**
     *
     * @param configuration configuration under which to generate value
     * @param type type of the value to generate
     * @param <T>
     * @return new generated value for the passed configuration
     */
    protected abstract <T> T generateValue(GenerationConfiguration configuration, GenerationContext<T> type);

    /**
     *
     * @param type
     * @return true if this generator can generate value for the passed type,
     *          false otherwise
     */
    public abstract boolean supportsType(Class type);
}
