package org.objectg.gen.rule;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.session.GenerationSession;
import org.objectg.gen.session.SessionState;
import org.objectg.gen.session.SessionStateDescription;

/**
 * <p>
 *     simple rule to generating range of numbers.
 * </p>
 * <p>
 * User: __nocach
 * Date: 22.1.13
 * </p>
 */
class RangeGenerationRule extends GenerationRule<Integer> {

	private final int from;
	private final int to;
	private final SessionState<Integer> currentValue;


	public RangeGenerationRule(final int from, int to){
		this.from = from;
		this.to = to;
		currentValue = GenerationSession.createManagedState(this, Integer.class, new SessionStateDescription<Integer>() {
			@Override
			public Integer getInitValue() {
				return from;
			}
		});
	}

	@Override
	public Integer getValue(final GenerationConfiguration currentConfiguration, final GenerationContext context) {
		Integer result = currentValue.get();
		updateCurrentValue(result);
		return result;
	}

	private void updateCurrentValue(final Integer result) {
		if (result >= to) {
			currentValue.set(from);
		}
		else{
			currentValue.set(result+1);
		}
	}
}
