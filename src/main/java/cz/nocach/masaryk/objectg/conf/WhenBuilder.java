package cz.nocach.masaryk.objectg.conf;

import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.RuleScope;
import cz.nocach.masaryk.objectg.gen.rule.Rules;
import org.hamcrest.Matcher;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 14.10.12
 */
public class WhenBuilder<T> {
    private final Matcher<GenerationContext> contextMatcher;
    private final ConfigurationBuilder configurationBuilder;

    public WhenBuilder(Matcher<GenerationContext> contextMatcher, ConfigurationBuilder configurationBuilder) {
        Assert.notNull(contextMatcher, "contextMatcher");
        Assert.notNull(configurationBuilder, "configurationBuilder");

        this.contextMatcher = contextMatcher;
        this.configurationBuilder = configurationBuilder;
    }

    public ConfigurationBuilder rule(GenerationRule rule){
        addRuleForMatcher(rule);
        return configurationBuilder;
    }

    private void addRuleForMatcher(GenerationRule rule) {
        rule.when(contextMatcher);
        configurationBuilder.addRule(rule);
    }

    private void addRuleForMatcher(GenerationRule rule, RuleScope scope) {
        rule.when(contextMatcher);
        rule.setScope(scope);
        configurationBuilder.addRule(rule);
    }

    public ConfigurationBuilder rule(GenerationRule rule, RuleScope scope){
        addRuleForMatcher(rule, scope);
        return configurationBuilder;
    }

    public ConfigurationBuilder value(T value){
        GenerationRule setValueRule = Rules.value(value);
        addRuleForMatcher(setValueRule);
        return configurationBuilder;
    }

    public ConfigurationBuilder setNull(){
        GenerationRule setValueRule = Rules.value(null);
        addRuleForMatcher(setValueRule);
        return configurationBuilder;
    }
}
