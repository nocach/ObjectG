package cz.nocach.masaryk.objectg.matcher.impl;

import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.matcher.ValueTypeHintMatcher;
import org.hamcrest.Matcher;

/**
 * User: __nocach
 * Date: 14.10.12
 */
public class GenerationContextFeatures {
    public static <T> ValueTypeHintMatcher<GenerationContext, T> forClass(Matcher<? super Class<T>> classMatcher){
        return new ClassGenerationContextFeature(classMatcher);
    }

    public static ValueTypeHintMatcher<GenerationContext, ?> forIsRoot(Matcher<Boolean> booleanMatcher){
        return new IsRootGenerationContextFeature(booleanMatcher);
    }
}
