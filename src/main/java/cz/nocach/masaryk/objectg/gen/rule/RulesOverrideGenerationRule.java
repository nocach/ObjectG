package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.GeneratorRegistry;
import cz.nocach.masaryk.objectg.gen.GenerationContext;

import java.util.List;

/**
 * <p>
 *     Rule allowing to define new or override existing GenerationConfiguration. This Rule will not completely override
 *     configuration, but allows to extend configuration for some properties (e.g. when you want to generate two
 *     fields of the same types but with different rules).
 * </p>
 * <p>
 * User: __nocach
 * Date: 16.9.12
 * </p>
 */
class RulesOverrideGenerationRule extends GenerationRule<Object> {
    private List<GenerationRule> rulesToOverride;

    public RulesOverrideGenerationRule(List<GenerationRule> rulesToOverride){
        this.rulesToOverride = rulesToOverride;
    }

    @Override
    protected Object getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        for (GenerationRule each : rulesToOverride) each.setScope(getScope());
        GenerationConfiguration overridenConfiguration = currentConfiguration.newWithMoreRules(rulesToOverride);
        //prevent cycling applying of this rule
        overridenConfiguration.removeRule(this);
        return GeneratorRegistry.getInstance().generate(overridenConfiguration, context);
    }

}
