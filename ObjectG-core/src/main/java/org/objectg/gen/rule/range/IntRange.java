package org.objectg.gen.rule.range;

import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 27.1.13
 */
public class IntRange extends Range<Integer> {
	private Integer from;
	private Integer to;
	private int step;

	IntRange(Integer from, Integer to, int step){
		this.step = step;
		Assert.notNull(from, "from");
		Assert.notNull(to, "to");
		this.from = from;
		this.to = to;
	}
	IntRange(Integer from, Integer to){
		this(from, to, 1);
	}

	public IntRange step(int step){
		this.step = step;
		return this;
	}

	@Override
	public Integer getStart() {
		return from;
	}

	@Override
	public Integer getNext(final Integer currentValue) {
		return currentValue + step;
	}

	@Override
	public boolean hasNext(final Integer currentValue) {
		return currentValue + step <= to;
	}

	@Override
	public boolean hasPrevious(final Integer currentValue) {
		return currentValue - step >= from;
	}

	@Override
	public Integer getPrevious(final Integer currentValue) {
		return currentValue - step;
	}

	@Override
	public Integer getEnd() {
		return to;
	}

	public static IntRange createReverse(int from, int to){
		Assert.isTrue(from >= to, "from >= to");
		final IntRange result = new IntRange(to, from);
		result.setReversed(true);
		return result;
	}

	public static IntRange create(int from, int to){
		Assert.isTrue(from <= to, "from <= to");
		return new IntRange(from, to);
	}
}
