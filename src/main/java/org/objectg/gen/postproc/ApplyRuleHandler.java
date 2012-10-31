package org.objectg.gen.postproc;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 28.10.12
 */
public class ApplyRuleHandler implements ExpressionPostProcessor.Handler {
    private GenerationRule rule;

    public ApplyRuleHandler(GenerationRule rule){
        Assert.notNull(rule, "rule");
        this.rule = rule;
    }
    @Override
    public Object handle(GenerationConfiguration configuration, Object generatedObject, GenerationContext contextForExpression) {
        return rule.getValue(configuration, contextForExpression);
    }
}
