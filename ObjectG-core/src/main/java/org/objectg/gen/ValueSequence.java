package org.objectg.gen;

/**
 * <p>
 *     Sequence that generates values. Need not to be thread-safe.
 * </p>
 * <p>
 * User: __nocach
 * Date: 22.1.13
 * </p>
 */
public interface ValueSequence<T> {
	public T next();
	public void reset();
}
