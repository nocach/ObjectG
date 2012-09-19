package cz.nocach.masaryk.objectg.gen.conf;

import cz.nocach.masaryk.objectg.gen.GenerationRule;

/**
 * <p>
 *     Holds planned objects for the configuration. After configuration was applied {@link #clear()} must be called.
 * </p>
 * <p>
 * User: __nocach
 * Date: 15.9.12
 * </p>
 */
public class OngoingConfiguration {
    //TODO: what about multithread? threadLocal?
    public static volatile GenerationRule plannedRule;
    public static volatile Object plannedConfigurationObject;

    public static void clear() {
        plannedRule = null;
        plannedConfigurationObject = null;
    }
}
