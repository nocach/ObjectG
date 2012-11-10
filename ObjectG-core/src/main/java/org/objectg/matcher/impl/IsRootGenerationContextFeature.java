package org.objectg.matcher.impl;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.objectg.gen.GenerationContext;
import org.objectg.matcher.ValueTypeHintMatcher;

/**
 * User: __nocach
 * Date: 14.10.12
 */
public class IsRootGenerationContextFeature<U>  extends FeatureMatcher<GenerationContext, Boolean>
                                                implements ValueTypeHintMatcher<GenerationContext, U>{

    public IsRootGenerationContextFeature(Matcher<? super Boolean> subMatcher) {
        super(subMatcher, "GenerationContext.isRoot", "if GenerationContext is root");
    }

    @Override
    protected Boolean featureValueOf(GenerationContext actual) {
        return actual.isRoot();
    }
}
