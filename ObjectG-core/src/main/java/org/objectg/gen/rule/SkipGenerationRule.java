package org.objectg.gen.rule;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.impl.SkipValueGenerationException;

/**
 * <p>
 *     GenerationRule saying that some property should be skipped, meaning that no attempt
 *     to setting this property is done.
 * </p>
 * <p>
 * User: __nocach
 * Date: 3.2.13
 * </p>
 */
class SkipGenerationRule extends GenerationRule{
	@Override
	public Object getValue(final GenerationConfiguration currentConfiguration, final GenerationContext context) {
		throw new SkipValueGenerationException();
	}

}
