package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.GeneratorRegistry;
import cz.nocach.masaryk.objectg.gen.GenerationContext;

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
class SpecificConfigurationGenerationRule extends GenerationRule {
    private GenerationConfiguration configuration;

    public SpecificConfigurationGenerationRule(GenerationConfiguration configuration){
        this.configuration = configuration;
    }

    @Override
    protected <T> T getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        GenerationConfiguration overridenConfiguration = currentConfiguration.newWithOverride(configuration);
        return (T) GeneratorRegistry.getInstance().generate(overridenConfiguration
                    , new GenerationContext(context.getClassThatIsGenerated()));
    }
}
