package cz.nocach.masaryk.objectg.gen.context;

import cz.nocach.masaryk.objectg.gen.rule.GenerationRule;

/**
 * <p>
 *     Defines data under which Generation is performed. Used by {@link cz.nocach.masaryk.objectg.gen.GeneratorRegistry}.
 * </p>
 * <p>
 * User: __nocach
 * Date: 28.8.12
 * </p>
 */
public class GenerationContext {
    public boolean isUnique;

    public void addRule(GenerationRule rule) {
        throw new RuntimeException("not implemented yet");
    }
}
