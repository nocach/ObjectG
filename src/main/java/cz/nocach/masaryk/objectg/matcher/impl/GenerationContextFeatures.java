package cz.nocach.masaryk.objectg.matcher.impl;

import cz.nocach.masaryk.objectg.gen.GenerationContext;
import org.hamcrest.Matcher;

/**
 * User: __nocach
 * Date: 14.10.12
 */
public class GenerationContextFeatures {
    public static Matcher<GenerationContext> forClass(Matcher<? super Class<?>> classMatcher){
        return new ClassGenerationContextFeature(classMatcher);
    }

    public static Matcher<GenerationContext> forIsRoot(Matcher<Boolean> booleanMatcher){
        return new IsRootGenerationContextFeature(booleanMatcher);
    }
}
