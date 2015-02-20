package org.objectg.gen.rule;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;

/**
 * User: __nocach
 * Date: 23.9.12
 */

class SingleValueGenerationRule<T> extends GenerationRule<T> {
	private T value;

	public SingleValueGenerationRule(T value) {
		this.value = value;
	}

	@Override
	public T getValueInner(GenerationConfiguration currentConfiguration, GenerationContext context) {
		return value;
	}
}
