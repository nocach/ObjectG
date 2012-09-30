package cz.nocach.masaryk.objectg.conf;

import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.rule.Rules;
import cz.nocach.masaryk.objectg.matcher.ClassGenerationContextFeature;
import cz.nocach.masaryk.objectg.matcher.JavaNativeTypeMatcher;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class ConfigurationBuilder {
    private GenerationConfiguration resultConfiguration;

    public ConfigurationBuilder(){
        resultConfiguration = new GenerationConfiguration();
    }

    public void nullObjects() {
        GenerationRule rule = Rules.onlyNull();
        rule.when(new ClassGenerationContextFeature(JavaNativeTypeMatcher.INSTANCE));
//        resultConfiguration.addRule();
    }
}
