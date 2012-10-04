package cz.nocach.masaryk.objectg.matcher;

import cz.nocach.masaryk.objectg.GenerationContext;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class ClassGenerationContextFeature extends FeatureMatcher<GenerationContext, Class> {
    /**
     * Constructor
     *
     * @param subMatcher         The matcher to apply to the feature
     */
    public ClassGenerationContextFeature(Matcher<? super Class> subMatcher) {
        super(subMatcher, "GenerationContext", "class that is generated");
    }

    @Override
    protected Class featureValueOf(GenerationContext actual) {
        return actual.getClassThatIsGenerated();
    }
}
