package org.objectg.gen.rule.range;

import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 27.1.13
 */
public class FloatRange extends Range<Float>{
	private Float from;
	private Float to;
	private float step;

	FloatRange(Float from, Float to, float step){
		this.step = step;
		Assert.notNull(from, "from");
		Assert.notNull(to, "to");
		this.from = from;
		this.to = to;
	}
	FloatRange(Float from, Float to){
		this(from, to, 1);
	}

	public FloatRange step(float step){
		this.step = step;
		return this;
	}

	@Override
	public Float getStart() {
		return from;
	}

	@Override
	public Float getNext(final Float currentValue) {
		return currentValue + step;
	}

	@Override
	public boolean hasNext(final Float currentValue) {
		return currentValue + step <= to;
	}

	@Override
	public boolean hasPrevious(final Float currentValue) {
		return currentValue - step >= from;
	}

	@Override
	public Float getPrevious(final Float currentValue) {
		return currentValue - step;
	}

	@Override
	public Float getEnd() {
		return to;
	}

	public static FloatRange createReverse(float from, float to){
		Assert.isTrue(from >= to, "from >= to");
		final FloatRange result = new FloatRange(to, from);
		result.setReversed(true);
		return result;
	}

	public static FloatRange create(float from, float to){
		Assert.isTrue(from <= to, "from <= to");
		return new FloatRange(from, to);
	}
}
