package org.objectg.gen.rule.range;

/**
 * <p>
 *     Class defining Range
 * </p>
 * <p>
 * User: __nocach
 * Date: 27.1.13
 * </p>
 */
public abstract class Range<T> {
	/**
	 * if true, then values must be used in reverse order. So in range (1,5) we will start from 5 and go to 1.
	 */
	private boolean reversed = false;
	public boolean isReversed() {
		return reversed;
	}

	public void setReversed(final boolean reversed) {
		this.reversed = reversed;
	}

	/**
	 * @return value from which this range is started
	 */
	public abstract T getStart();

	/**
	 *
	 * @param currentValue not null
	 * @return value that will follow currentValue in this range.
	 */
	public abstract T getNext(T currentValue);

	/**
	 *
	 * @param currentValue not null
	 * @return true if this range can return next value for the passed {@code currentValue}
	 */
	public abstract boolean hasNext(T currentValue);

	/**
	 *
	 * @param currentValue not null
	 * @return true if this range can return previous value for the passed {@code currentValue}
	 */
	public abstract boolean hasPrevious(T currentValue);

	/**
	 *
	 * @param currentValue not null
	 * @return value that was previous to the passed currentValue
	 */
	public abstract T getPrevious(T currentValue);

	/**
	 *
	 * @return value on which this range ends
	 */
	public abstract T getEnd();

}
