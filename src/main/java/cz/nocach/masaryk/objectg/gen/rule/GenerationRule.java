package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.gen.Generator;
import org.hamcrest.Matcher;

/**
 * User: __nocach
 * Date: 1.9.12
 */
public abstract class GenerationRule {
    public void when(Matcher<?> matcher) {
        throw new RuntimeException("not implemented yet");
    }

    protected abstract Generator getGenerator();
}
