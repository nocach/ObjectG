package org.objectg.matcher;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.objectg.gen.GenerationContext;
import org.objectg.matcher.impl.GenerationContextFeatures;
import org.springframework.util.Assert;

/**
 * <p>
 *     Provides fluent api for creating Matcher<GenerationContext> used in {@link org.objectg.gen.GenerationRule}
 * </p>
 * <p>
 *     class {@link ValueTypeHintMatcher} is only used to allow {@link org.objectg.conf.ConfigurationBuilder} for
 *     type inference.
 * </p>
 * <p>
 * User: __nocach
 * Date: 14.10.12
 * </p>
 */
public class ContextMatchers {
	/**
	 *
	 * @param classes not empty array without null elements.
	 * @param <U>
	 * @return matched that matches {@link GenerationContext} that have {@link org.objectg.gen.GenerationContext#getClassThatIsGenerated()}
	 * 	EXACTLY EQUAL to one of the classes
	 */
    public static <U> ValueTypeHintMatcher<GenerationContext, U> typeOf(Class<? extends U>... classes){
        Assert.notEmpty(classes, "at least one class object should be provided");
        Assert.noNullElements(classes, "no null elements allowed");
        Matcher[] classMatchers = new Matcher[classes.length];
        for (int i = 0; i < classes.length; i++){
            classMatchers[i] = Matchers.equalTo(classes[i]);
        }
        return GenerationContextFeatures.forClass(Matchers.anyOf(classMatchers));
    }

}
