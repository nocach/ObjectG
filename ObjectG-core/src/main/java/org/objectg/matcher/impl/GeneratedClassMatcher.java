package org.objectg.matcher.impl;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.objectg.gen.GenerationContext;
import org.objectg.matcher.ValueTypeHintMatcher;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class GeneratedClassMatcher<T> extends TypeSafeMatcher<GenerationContext>
                                                implements ValueTypeHintMatcher<GenerationContext, T>
{

	private Matcher<Class<T>> classMatcher;

	/**
	 *
	 * @param classMatcher The matcher to apply against {@link org.objectg.gen.GenerationContext#getClassThatIsGenerated()}
	 */
	public GeneratedClassMatcher(Matcher<Class<T>> classMatcher){
		Assert.notNull(classMatcher, "classMatcher should be not null");
		this.classMatcher = classMatcher;
	}


	@Override
	public boolean matchesSafely(final GenerationContext generationContext) {
		return classMatcher.matches(generationContext.getClassThatIsGenerated());
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("matching class that is generated with ");
		description.appendDescriptionOf(classMatcher);
	}
}
