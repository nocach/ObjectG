package org.objectg.matcher.impl;

import org.hamcrest.Matcher;
import org.objectg.gen.GenerationContext;
import org.objectg.matcher.ValueTypeHintMatcher;

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
