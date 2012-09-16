package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.Generator;
import cz.nocach.masaryk.objectg.gen.NotNativeClassGenerator;
import cz.nocach.masaryk.objectg.gen.conf.GenerationConfiguration;

/**
 * <p>
 *     Rule allowing override GenerationConfiguration (general configuration or class-specific configuration)
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
        try {
            GenerationConfiguration extendedConfiguration = currentConfiguration.clone();
            extendedConfiguration.putChild(configuration);
            return new NotNativeClassGenerator(extendedConfiguration);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
