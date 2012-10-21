package cz.nocach.masaryk.objectg.matcher;

import org.hamcrest.Matcher;

/**
 * <p>
 *     Matcher for some class that provides hint on what values will be used when this matcher
 *     has matched
 * </p>
 * <p>
 *     @param <T> type of the matcher
 *     @param <U> type of the variable
 * </p>
 * <p>
 * User: __nocach
 * Date: 21.10.12
 * </p>
 *
 */
public interface ValueTypeHintMatcher<T, U> extends Matcher<T>, ValueTypeHint<U> {
}
