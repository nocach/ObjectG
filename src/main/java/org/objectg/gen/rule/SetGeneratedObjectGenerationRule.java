package org.objectg.gen.rule;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;

/**
 * User: __nocach
 * Date: 8.11.12
 */
class SetGeneratedObjectGenerationRule extends GenerationRule {
	@Override
	public Object getValue(final GenerationConfiguration currentConfiguration, final GenerationContext context) {
		return context.getRootObject();
	}

}
