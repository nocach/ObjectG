package cz.nocach.masaryk.objectg.matcher.impl;

import cz.nocach.masaryk.objectg.gen.GenerationContext;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

/**
 * User: __nocach
 * Date: 14.10.12
 */
public class IsRootGenerationContextFeature  extends FeatureMatcher<GenerationContext, Boolean> {

    public IsRootGenerationContextFeature(Matcher<? super Boolean> subMatcher) {
        super(subMatcher, "GenerationContext.isRoot", "if GenerationContext is root");
    }

    @Override
    protected Boolean featureValueOf(GenerationContext actual) {
        return actual.isRoot();
    }
}
