package cz.nocach.masaryk.objectg.conf;

import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GenerationRule;
import org.hamcrest.Matcher;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 14.10.12
 */
public class WhenBuilder {
    private final Matcher<GenerationContext> contextMatcher;
    private final ConfigurationBuilder configurationBuilder;

    public WhenBuilder(Matcher<GenerationContext> contextMatcher, ConfigurationBuilder configurationBuilder) {
        Assert.notNull(contextMatcher, "contextMatcher");
        Assert.notNull(configurationBuilder, "configurationBuilder");

        this.contextMatcher = contextMatcher;
        this.configurationBuilder = configurationBuilder;
    }

    public ConfigurationBuilder rule(GenerationRule rule){
        rule.when(contextMatcher);
        configurationBuilder.addRule(rule);
        return configurationBuilder;
    }
}
