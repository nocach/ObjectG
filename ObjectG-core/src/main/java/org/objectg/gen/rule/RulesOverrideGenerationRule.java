package org.objectg.gen.rule;

import java.util.List;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.session.GenerationSession;

/**
 * <p>
 * Rule allowing to define new or override existing GenerationConfiguration. This Rule will not completely override
 * configuration, but allows to extend configuration for some properties (e.g. when you want to generate two
 * fields of the same types but with different rules).
 * </p>
 * <p>
 * User: __nocach
 * Date: 16.9.12
 * </p>
 */
class RulesOverrideGenerationRule extends GenerationRule<Object> {
	private List<GenerationRule> rulesToOverride;

	public RulesOverrideGenerationRule(List<GenerationRule> rulesToOverride) {
		this.rulesToOverride = rulesToOverride;
	}

	@Override
	public Object getValueInner(GenerationConfiguration currentConfiguration, GenerationContext context) {
		for (GenerationRule each : rulesToOverride) each.setScope(getScope());
		GenerationConfiguration overridenConfiguration = currentConfiguration.newWithMoreRules(rulesToOverride);
		//prevent cycling applying of this rule
		overridenConfiguration.removeRule(this);
		return GenerationSession.get().generate(overridenConfiguration, context);
	}

}
