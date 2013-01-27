package org.objectg.gen.rule.range;

import java.math.BigDecimal;

import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 27.1.13
 */
public class BigDecimalRange extends Range<BigDecimal> {
	private BigDecimal from;
	private BigDecimal to;
	private BigDecimal step;

	BigDecimalRange(BigDecimal from, BigDecimal to, BigDecimal step){
		Assert.notNull(step, "step");
		Assert.notNull(from, "from");
		Assert.notNull(to, "to");
		this.step = step;
		this.from = from;
		this.to = to;
	}
	BigDecimalRange(BigDecimal from, BigDecimal to){
		this(from, to, BigDecimal.ONE);
	}

	public BigDecimalRange step(BigDecimal step){
		Assert.notNull(step, "step");
		this.step = step;
		return this;
	}

	@Override
	public BigDecimal getStart() {
		return from;
	}

	@Override
	public BigDecimal getNext(final BigDecimal currentValue) {
		return currentValue.add(step) ;
	}

	@Override
	public boolean hasNext(final BigDecimal currentValue) {
		return currentValue.add(step).compareTo(to) <= 0;
	}

	@Override
	public boolean hasPrevious(final BigDecimal currentValue) {
		return currentValue.subtract(step).compareTo(from) >= 0;
	}

	@Override
	public BigDecimal getPrevious(final BigDecimal currentValue) {
		return currentValue.subtract(step);
	}

	@Override
	public BigDecimal getEnd() {
		return to;
	}

	public static BigDecimalRange createReverse(BigDecimal from, BigDecimal to){
		Assert.isTrue(from.compareTo(to) >= 0, "from >= to");
		final BigDecimalRange result = new BigDecimalRange(to, from);
		result.setReversed(true);
		return result;
	}

	public static BigDecimalRange create(BigDecimal from, BigDecimal to){
		Assert.isTrue(from.compareTo(to) <= 0, "from <= to");
		return new BigDecimalRange(from, to);
	}
}
