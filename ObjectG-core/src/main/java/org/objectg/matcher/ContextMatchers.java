package org.objectg.matcher;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.objectg.gen.GenerationContext;
import org.objectg.matcher.impl.GenerationContextFeatures;
import org.springframework.util.Assert;

import static org.hamcrest.Matchers.is;

/**
 * <p>
 *     Provides fluent api for creating Matcher<GenerationContext> used in {@link org.objectg.gen.GenerationRule}
 * </p>
 * <p>
 * User: __nocach
 * Date: 14.10.12
 * </p>
 */
public class ContextMatchers {
    public static <U> ValueTypeHintMatcher<GenerationContext, U> typeOf(Class<? extends U>... classes){
        Assert.notEmpty(classes, "at least one class object should be provided");
        Assert.noNullElements(classes, "no null elements allowed");
        Matcher[] classMatchers = new Matcher[classes.length];
        for (int i = 0; i < classes.length; i++){
            classMatchers[i] = Matchers.equalTo(classes[i]);
        }
        return GenerationContextFeatures.forClass(Matchers.anyOf(classMatchers));
    }

    public static ValueTypeHintMatcher<GenerationContext, ?> expression(String expression){
        return GenerationContextFeatures.forIsRoot(is(true));
    }
}
