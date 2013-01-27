package org.objectg.gen.rule.range;

import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 27.1.13
 */
public class DoubleRange extends Range<Double> {
	private Double from;
	private Double to;
	private double step;

	DoubleRange(Double from, Double to, double step){
		this.step = step;
		Assert.notNull(from, "from");
		Assert.notNull(to, "to");
		this.from = from;
		this.to = to;
	}
	DoubleRange(Double from, Double to){
		this(from, to, 1);
	}

	public DoubleRange step(double step){
		this.step = step;
		return this;
	}

	@Override
	public Double getStart() {
		return from;
	}

	@Override
	public Double getNext(final Double currentValue) {
		return currentValue + step;
	}

	@Override
	public boolean hasNext(final Double currentValue) {
		return currentValue + step <= to;
	}

	@Override
	public boolean hasPrevious(final Double currentValue) {
		return currentValue - step >= from;
	}

	@Override
	public Double getPrevious(final Double currentValue) {
		return currentValue - step;
	}

	@Override
	public Double getEnd() {
		return to;
	}

	public static DoubleRange createReverse(double from, double to){
		Assert.isTrue(from >= to, "from >= to");
		final DoubleRange result = new DoubleRange(to, from);
		result.setReversed(true);
		return result;
	}

	public static DoubleRange create(double from, double to){
		Assert.isTrue(from <= to, "from <= to");
		return new DoubleRange(from, to);
	}
}
