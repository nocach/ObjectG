package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.Generator;
import cz.nocach.masaryk.objectg.gen.NotNativeClassGenerator;
import cz.nocach.masaryk.objectg.gen.conf.GenerationConfiguration;

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
public class SpecificConfigurationGenerationRule extends GenerationRule {
    private GenerationConfiguration configuration;

    public SpecificConfigurationGenerationRule(GenerationConfiguration configuration){
        this.configuration = configuration;
    }
    @Override
    protected Generator getGenerator(GenerationConfiguration currentConfiguration) {
        return new NotNativeClassGenerator(currentConfiguration.newWithOverride(configuration));
    }
}
