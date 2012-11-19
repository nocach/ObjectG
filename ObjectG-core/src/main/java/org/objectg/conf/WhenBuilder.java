package org.objectg.conf;

import org.hamcrest.Matcher;
import org.objectg.conf.prototype.PrototypeCreator;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.PostProcessor;
import org.objectg.gen.RuleScope;
import org.objectg.gen.postproc.ApplyRuleHandler;
import org.objectg.gen.postproc.ExpressionPostProcessor;
import org.objectg.gen.rule.GenerationContextTransformer;
import org.objectg.gen.rule.Rules;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 14.10.12
 */
public class WhenBuilder<T> {
    private Matcher<GenerationContext> contextMatcher;
    private final ConfigurationBuilder configurationBuilder;
    private String expression;

    public WhenBuilder(Matcher<GenerationContext> contextMatcher, ConfigurationBuilder configurationBuilder) {
        Assert.notNull(contextMatcher, "contextMatcher");
        Assert.notNull(configurationBuilder, "configurationBuilder");

        this.contextMatcher = contextMatcher;
        this.configurationBuilder = configurationBuilder;
    }

    public WhenBuilder(String expression, ConfigurationBuilder configurationBuilder) {
        Assert.notNull(expression, "expression");
        Assert.notNull(configurationBuilder, "configurationBuilder");
        this.expression = expression;

        this.configurationBuilder = configurationBuilder;
    }

    public ConfigurationBuilder useRule(GenerationRule rule){
        addRule(rule);
        return configurationBuilder;
    }

    private void addRule(GenerationRule rule) {
        if (contextMatcher != null){
            rule.when(contextMatcher);
            configurationBuilder.addRule(rule);
        }
        else {
            PostProcessor expressionPostProc = new ExpressionPostProcessor(expression, new ApplyRuleHandler(rule));
            configurationBuilder.addPostProcessor(expressionPostProc);
        }
    }

    private void addRuleForMatcher(GenerationRule rule, RuleScope scope) {
        rule.when(contextMatcher);
        rule.setScope(scope);
        configurationBuilder.addRule(rule);
    }

    public ConfigurationBuilder useRule(GenerationRule rule, RuleScope scope){
        addRuleForMatcher(rule, scope);
        return configurationBuilder;
    }

    public ConfigurationBuilder setValue(T value){
        GenerationRule setValueRule = Rules.value(value);
        addRule(setValueRule);
        return configurationBuilder;
    }

    public ConfigurationBuilder setNull(){
        GenerationRule setValueRule = Rules.value(null);
        addRule(setValueRule);
        return configurationBuilder;
    }

    public ConfigurationBuilder usePrototype(T prototype) {
        PrototypeCreator prototypeCreator = configurationBuilder.getPrototypeCreator();
        GenerationRule specificConfigurationRule = Rules.specificRules(prototypeCreator.getRules(prototype));
        specificConfigurationRule.setScope(RuleScope.PROPERTY);
        addRule(specificConfigurationRule);
        return configurationBuilder;
    }

	public ConfigurationBuilder useClass(final Class<? extends T> classHint) {
		Assert.notNull(classHint, "classHint");
		final GenerationRule overrideClassRule = Rules.contextOverride(new GenerationContextTransformer() {
			@Override
			public void transform(final GenerationContext<?> context) {
				context.setClassThatIsGenerated(classHint);
			}
		});
		return useRule(overrideClassRule, RuleScope.GLOBAL);
	}

	public ConfigurationBuilder setValues(final T... values) {
		return useRule(Rules.fromList(values));
	}
}
