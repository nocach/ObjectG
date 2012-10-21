package cz.nocach.masaryk.objectg.matcher;

import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.matcher.impl.GenerationContextFeatures;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.springframework.util.Assert;

/**
 * <p>
 *     Provides fluent api for creating Matcher<GenerationContext> used in {@link cz.nocach.masaryk.objectg.gen.GenerationRule}
 * </p>
 * <p>
 * User: __nocach
 * Date: 14.10.12
 * </p>
 */
public class ContextMatchers {
    public static <U> ValueTypeHintMatcher<GenerationContext, U> instancesOf(Class<? extends U>... classes){
        Assert.notEmpty(classes, "at least one class object should be provided");
        Assert.noNullElements(classes, "no null elements allowed");
        Matcher[] classMatchers = new Matcher[classes.length];
        for (int i = 0; i < classes.length; i++){
            classMatchers[i] = Matchers.typeCompatibleWith(classes[i]);
        }
        return GenerationContextFeatures.forClass(Matchers.anyOf(classMatchers));
    }
}
