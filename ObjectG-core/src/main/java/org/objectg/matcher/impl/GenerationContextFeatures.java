package org.objectg.matcher.impl;

import org.hamcrest.Matcher;
import org.objectg.gen.GenerationContext;
import org.objectg.matcher.ValueTypeHintMatcher;

/**
 * <p>
 *     Factory for creating instances of {@link org.hamcrest.FeatureMatcher<GenerationContext, ?>}
 * </p>
 * <p>
 * User: __nocach
 * Date: 14.10.12
 * </p>
 */
public class GenerationContextFeatures {
	/**
	 *
	 * @param classMatcher matcher that will be applied to {@link org.objectg.gen.GenerationContext#getClassThatIsGenerated()}
	 * @param <T>
	 * @return matcher matching {@link org.objectg.gen.GenerationContext#getClassThatIsGenerated()}
	 */
    public static <T> ValueTypeHintMatcher<GenerationContext, T> forClass(Matcher<? super Class<T>> classMatcher){
        return new ClassGenerationContextFeature(classMatcher);
    }

	/**
	 *
	 * @param booleanMatcher matcher that will be applied to {@link org.objectg.gen.GenerationContext#isRoot()} ()}
	 * @return matcher matching {@link org.objectg.gen.GenerationContext#isRoot()}
	 */
    public static ValueTypeHintMatcher<GenerationContext, ?> forIsRoot(Matcher<Boolean> booleanMatcher){
        return new IsRootGenerationContextFeature(booleanMatcher);
    }
}
