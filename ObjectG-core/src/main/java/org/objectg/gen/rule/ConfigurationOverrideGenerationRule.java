package org.objectg.gen.rule;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.GeneratorRegistry;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 17.11.12
 */
public class ConfigurationOverrideGenerationRule extends GenerationRule {

	private GenerationConfiguration overrideConfiguration;

	public ConfigurationOverrideGenerationRule(GenerationConfiguration overrideConfiguration){
		Assert.notNull(overrideConfiguration, "overrideConfiguration");
		this.overrideConfiguration = overrideConfiguration;
	}
	@Override
	public Object getValue(final GenerationConfiguration currentConfiguration, final GenerationContext context) {
		makeDepthRelativeToContext(context);
		final GenerationConfiguration mergedConfiguration = overrideConfiguration.merge(currentConfiguration);
		mergedConfiguration.removeRule(this);
		return GeneratorRegistry.getInstance().generate(mergedConfiguration, context);
	}

	private void makeDepthRelativeToContext(final GenerationContext context) {
		if (overrideConfiguration.getDepth() != null && overrideConfiguration.getDepth() != -1){
			//depth should be relative to current context
			//this is more natural way how the user sees the configuration by prototype
			overrideConfiguration.setDepth(overrideConfiguration.getDepth() + context.getGenerationDepth());
		}
	}
}