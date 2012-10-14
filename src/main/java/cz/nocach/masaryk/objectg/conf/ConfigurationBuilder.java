package cz.nocach.masaryk.objectg.conf;

import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.cycle.BackReferenceCycleStrategy;
import cz.nocach.masaryk.objectg.gen.cycle.NullValueCycleStrategy;
import cz.nocach.masaryk.objectg.gen.rule.Rules;
import cz.nocach.masaryk.objectg.matcher.impl.JavaNativeTypeMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;

import static cz.nocach.masaryk.objectg.matcher.impl.GenerationContextFeatures.forClass;
import static cz.nocach.masaryk.objectg.matcher.impl.GenerationContextFeatures.forIsRoot;
import static org.hamcrest.Matchers.*;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class ConfigurationBuilder {
    private GenerationConfiguration resultConfiguration;

    public ConfigurationBuilder(){
        resultConfiguration = new GenerationConfiguration();
    }

    /**
     * sets rule that any not primitive object will be set to null during generation,
     * except for collections and maps, which will be only empty.
     * @return
     */
    public ConfigurationBuilder noObjects() {
        GenerationRule nullForNotRootObjects = Rules.onlyNull();
        nullForNotRootObjects.when(
                Matchers.<GenerationContext>allOf(
                        Matchers.not(forClass(JavaNativeTypeMatcher.INSTANCE))
                        , forIsRoot(Matchers.is(false))
                )
        );
        resultConfiguration.addRule(nullForNotRootObjects);

        GenerationRule emptyCollectionRule = Rules.emptyCollection();
        emptyCollectionRule.when(forClass(typeCompatibleWith(Collection.class)));
        resultConfiguration.addRule(emptyCollectionRule);

        GenerationRule emptyMapRule = Rules.emptyMap();
        emptyMapRule.when(forClass(typeCompatibleWith(Map.class)));
        resultConfiguration.addRule(emptyMapRule);

        return this;
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

    public WhenBuilder when(Matcher<GenerationContext> contextMatcher){
        return new WhenBuilder(contextMatcher, this);
    }

    void addRule(GenerationRule rule) {
        Assert.notNull(rule, "rule");
        resultConfiguration.addRule(rule);
    }
}
