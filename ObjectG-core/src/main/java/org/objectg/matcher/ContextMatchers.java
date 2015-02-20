package org.objectg.matcher;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.objectg.gen.GenerationContext;
import org.objectg.matcher.impl.GeneratedClassMatcher;
import org.objectg.matcher.impl.IsRootMatcher;
import org.objectg.util.Types;
import org.springframework.util.Assert;

/**
 * <p>
 * Provides fluent api for creating Matcher<GenerationContext> used in {@link org.objectg.gen.GenerationRule}
 * </p>
 * <p>
 * class {@link ValueTypeHintMatcher} is only used to allow {@link org.objectg.conf.ConfigurationBuilder} for
 * type inference.
 * </p>
 * <p>
 * User: __nocach
 * Date: 14.10.12
 * </p>
 */
public class ContextMatchers {
	/**
	 * @param classes not empty array without null elements.
	 * @param <U>
	 * @return matched that matches {@link GenerationContext} that have {@link org.objectg.gen.GenerationContext#getClassThatIsGenerated()}
	 *         EXACTLY EQUAL to one of the classes
	 */
	public static <U> ValueTypeHintMatcher<GenerationContext, U> typeOf(Class<? extends U>... classes) {
		Assert.notEmpty(classes, "at least one class object should be provided");
		Assert.noNullElements(classes, "no null elements allowed");
		Matcher[] classMatchers = new Matcher[classes.length];
		for (int i = 0; i < classes.length; i++) {
			classMatchers[i] = Matchers.equalTo(classes[i]);
		}
		return matchingGeneratingClass(AnyOf.anyOf(classMatchers));
	}

	/**
	 * @param isRoot if matching context must be root
	 * @return matcher that will check if given context is root context
	 */
	public static Matcher<GenerationContext> isRoot(boolean isRoot) {
		return new IsRootMatcher(isRoot);
	}

	public static <U> ValueTypeHintMatcher<GenerationContext, U> matchingGeneratingClass(
			Matcher<Class<U>> classMatcher) {
		return new GeneratedClassMatcher<U>(classMatcher);
	}

	public static Matcher<GenerationContext> collectionOrMapOfType(Class elementType) {
		return new BagElementTypeMatcher(elementType);
	}

	private static class BagElementTypeMatcher extends TypeSafeMatcher<GenerationContext> {
		private Class elementType;

		public BagElementTypeMatcher(Class elementType) {
			this.elementType = elementType;
		}

		@Override
		public boolean matchesSafely(final GenerationContext generationContext) {
			int typeVarIndex = 0;
			Type genericType = null;
			if (generationContext.getFieldPropertyDescriptor() != null) {
				genericType = generationContext.getFieldPropertyDescriptor()
						.getGenericTypeFromMethodsSignature(typeVarIndex);
			}
			if (genericType == null && generationContext.getPropertyAccessor() != null) {
				genericType = generationContext.getPropertyAccessor().getGenericType(typeVarIndex);
			}
			if (genericType == null && Types.isArray(generationContext.getClassThatIsGenerated())) {
				try {
					genericType = Types.getArrayElementClass(generationContext.getClassThatIsGenerated());
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
			return (Collection.class.isAssignableFrom(generationContext.getClassThatIsGenerated())
					|| Map.class.isAssignableFrom(generationContext.getClassThatIsGenerated())
					|| Types.isArray(generationContext.getClassThatIsGenerated()))
					&& (genericType != null && elementType.isAssignableFrom((Class) genericType));
		}

		@Override
		public void describeTo(final Description description) {
			description.appendText("matching collection with elements of type=" + elementType);
		}
	}
}
