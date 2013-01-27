package org.objectg.gen.rule.range;

import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 27.1.13
 */
public class LongRange extends Range<Long>{
	private Long from;
	private Long to;
	private long step;

	LongRange(Long from, Long to, long step){
		this.step = step;
		Assert.notNull(from, "from");
		Assert.notNull(to, "to");
		this.from = from;
		this.to = to;
	}
	LongRange(Long from, Long to){
		this(from, to, 1);
	}

	public LongRange step(long step){
		this.step = step;
		return this;
	}

	@Override
	public Long getStart() {
		return from;
	}

	@Override
	public Long getNext(final Long currentValue) {
		return currentValue + step;
	}

	@Override
	public boolean hasNext(final Long currentValue) {
		return currentValue + step <= to;
	}

	@Override
	public boolean hasPrevious(final Long currentValue) {
		return currentValue - step >= from;
	}

	@Override
	public Long getPrevious(final Long currentValue) {
		return currentValue - step;
	}

	@Override
	public Long getEnd() {
		return to;
	}

	public static LongRange createReverse(long from, long to){
		Assert.isTrue(from >= to, "from >= to");
		final LongRange result = new LongRange(to, from);
		result.setReversed(true);
		return result;
	}

	public static LongRange create(long from, long to){
		Assert.isTrue(from <= to, "from <= to");
		return new LongRange(from, to);
	}
}
