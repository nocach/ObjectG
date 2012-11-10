package org.objectg.matcher.impl;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.objectg.gen.GenerationContext;
import org.objectg.matcher.ValueTypeHintMatcher;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class ClassGenerationContextFeature<T> extends FeatureMatcher<GenerationContext, Class<T>>
                                                implements ValueTypeHintMatcher<GenerationContext, T>
{
    /**
     * Constructor
     *
     * @param subMatcher         The matcher to apply to the feature
     */
    public ClassGenerationContextFeature(Matcher<? super Class<T>> subMatcher) {
        super(subMatcher, "GenerationContext.classThatIsGenerated", "class that is generated");
    }

    @Override
    protected Class featureValueOf(GenerationContext actual) {
        return actual.getClassThatIsGenerated();
    }
}
