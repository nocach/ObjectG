package org.objectg.gen.rule;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.rule.range.Range;
import org.objectg.gen.session.GenerationSession;
import org.objectg.gen.session.SessionState;
import org.objectg.gen.session.SessionStateDescription;

/**
 * <p>
 * simple rule to generating range of numbers.
 * </p>
 * <p>
 * User: __nocach
 * Date: 22.1.13
 * </p>
 */
class RangeGenerationRule<T> extends GenerationRule<T> {

	private final SessionState<T> currentValue;
	private Range<T> range;


	public RangeGenerationRule(final Range<T> range) {
		this.range = range;
		currentValue = GenerationSession.createManagedState(this, new SessionStateDescription<T>() {
			@Override
			public T getInitValue() {
				return range.isReversed() ? range.getEnd() : range.getStart();
			}
		});
	}

	@Override
	public T getValueInner(final GenerationConfiguration currentConfiguration, final GenerationContext context) {
		T result = currentValue.get();
		updateCurrentValue(result);
		return result;
	}

	private void updateCurrentValue(final T currentValue) {
		if (!range.isReversed()) {
			updateWithNextValue(currentValue);
		} else {
			updateWithPreviousValue(currentValue);
		}
	}

	private void updateWithPreviousValue(final T currentValue) {
		if (range.hasPrevious(currentValue)) {
			this.currentValue.set(range.getPrevious(currentValue));
		} else {
			this.currentValue.set(range.getEnd());
		}
	}

	private void updateWithNextValue(final T currentValue) {
		if (range.hasNext(currentValue)) {
			this.currentValue.set(range.getNext(currentValue));
		} else {
			this.currentValue.set(range.getStart());
		}
	}
}
