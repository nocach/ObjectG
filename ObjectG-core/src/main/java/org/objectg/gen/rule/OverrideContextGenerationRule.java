package org.objectg.gen.rule;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.GeneratorRegistry;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 3.11.12
 */
class OverrideContextGenerationRule extends GenerationRule {
	private GenerationContextTransformer transformer;
	private boolean permanent;

	public OverrideContextGenerationRule(GenerationContextTransformer transformer, final boolean permanent){
		Assert.notNull(transformer, "transformer");
		this.permanent = permanent;
		this.transformer = transformer;
	}
	@Override
	public Object getValue(final GenerationConfiguration currentConfiguration, final GenerationContext context) {
		transformer.transform(context);
		if (!permanent) currentConfiguration.removeRule(this);
		return GeneratorRegistry.getInstance().generate(currentConfiguration, context);
	}

}
