package cz.nocach.masaryk.objectg.matcher.impl;

import cz.nocach.masaryk.objectg.gen.GenerationContext;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class ClassGenerationContextFeature<T> extends FeatureMatcher<GenerationContext, Class<T>> {
    /**
     * Constructor
     *
     * @param subMatcher         The matcher to apply to the feature
     */
    public ClassGenerationContextFeature(Matcher<? super Class> subMatcher) {
        super(subMatcher, "GenerationContext.classThatIsGenerated", "class that is generated");
    }

    @Override
    protected Class featureValueOf(GenerationContext actual) {
        return actual.getClassThatIsGenerated();
    }
}
