package org.objectg.gen.rule;

import java.util.Arrays;
import java.util.List;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.session.GenerationSession;
import org.objectg.gen.session.SessionState;
import org.objectg.gen.session.SessionStateDescription;
import org.springframework.util.Assert;

/**
 * <p>
 * GenerationRule specifying that values will be generated from pre-defined list of objects.
 * </p>
 * <p>
 * User: __nocach
 * Date: 1.9.12
 * </p>
 */
class FromListGenerationRule<T> extends GenerationRule<T> {
	private List<T> values;
	private FromListGenerator fromListGenerator;

	public FromListGenerationRule(T... values) {
		this(Arrays.asList(values));

	}

	public FromListGenerationRule(List<T> values) {
		Assert.notNull(values, "at least one value must be provided, values are null");
		Assert.isTrue(!values.isEmpty(), "at least one value must be provided, values are empty");
		this.values = values;
		fromListGenerator = new FromListGenerator(values);
	}

	@Override
	public T getValueInner(GenerationConfiguration currentConfiguration, GenerationContext context) {
		return (T) fromListGenerator.generateValue();
	}

	private static class FromListGenerator {
		private List values;
		final SessionState<Integer> valueIndexState;

		private FromListGenerator(List values) {
			Assert.isTrue(!values.isEmpty());
			this.values = values;
			valueIndexState = GenerationSession
					.createManagedState(this, Integer.class, new SessionStateDescription<Integer>() {
						@Override
						public Integer getInitValue() {
							return 0;
						}
					});
		}

		public <T> T generateValue() {
			int currentIndex = valueIndexState.get();
			T result = (T) values.get(currentIndex);
			currentIndex++;
			if (currentIndex > values.size() - 1) {
				currentIndex = 0;
			}
			valueIndexState.set(currentIndex);
			return result;
		}

	}
}
