package cz.nocach.masaryk.objectg.conf;

import com.sun.corba.se.impl.orbutil.CacheTable;
import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.cycle.BackReferenceCycleStrategy;
import cz.nocach.masaryk.objectg.gen.cycle.NullValueCycleStrategy;
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

    public ConfigurationBuilder backReferenceCycle() {
        resultConfiguration.setCycleStrategy(new BackReferenceCycleStrategy());
        return this;
    }

    public GenerationConfiguration done() {
        return resultConfiguration;
    }

    public ConfigurationBuilder nullCycle() {
        resultConfiguration.setCycleStrategy(new NullValueCycleStrategy());
        return this;
    }
}
