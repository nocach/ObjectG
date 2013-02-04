package org.objectg.conf;

import org.objectg.gen.GenerationRule;

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
    private static ThreadLocal<GenerationRule> plannedRule = new ThreadLocal<GenerationRule>();
    private static ThreadLocal<Object> plannedPrototype = new ThreadLocal<Object>();

    public static void clear() {
		plannedRule.remove();
		plannedPrototype.remove();
    }

	public static GenerationRule getPlannedRule() {
		return plannedRule.get();
	}

	public static void setPlannedRule(final GenerationRule plannedRule) {
		OngoingConfiguration.plannedRule.set(plannedRule);
	}

	public static Object getPlannedPrototype() {
		return plannedPrototype.get();
	}

	public static void setPlannedPrototype(final Object plannedPrototype) {
		OngoingConfiguration.plannedPrototype.set(plannedPrototype);
	}
}
