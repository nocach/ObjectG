package org.objectg.gen;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.matcher.impl.FieldNameMatcher;
import org.springframework.util.Assert;

import static org.hamcrest.Matchers.any;

/**
 * <p>
 *     GenerationRule defines what specific rules and when are used for generating of property value.
 *
 * </p>
 * <p>
 * User: __nocach
 * Date: 1.9.12
 * </p>
 */
public abstract class GenerationRule<T> implements Comparable<GenerationRule>{
    private Matcher<GenerationContext> matcher;
    private RuleScope scope = RuleScope.GLOBAL;

    public GenerationRule(){}

    protected GenerationRule(RuleScope scope) {
        this.scope = scope;
    }

    /**
     *
     * @param matcher matcher must have defined equals, so only one rule for same condition exists
     */
    public void when(Matcher<GenerationContext> matcher) {
        this.matcher = matcher;
    }

    public abstract T getValue(GenerationConfiguration currentConfiguration, GenerationContext context);

    /**
     * @param parentClass class where property is
     * @param forProperty for which property this rule must be applied
     */
    public void setForProperty(Class<?> parentClass, String forProperty) {
        matcher = new FieldNameMatcher(parentClass, forProperty);
    }

    /**
     *
     * @param context
     * @return true if this rule must be applied in the passed context
     */
    public boolean matches(GenerationContext context){
        if (matcher != null) return matcher.matches(context);
        throw new IllegalStateException("Rule without matching rule. " +
                "when(matcher) must be called to set when rule must be applied");
    }

    @Override
    public String toString() {
        StringDescription describeMatcher = new StringDescription();
        if (matcher != null) matcher.describeTo(describeMatcher);
        return getClass().getSimpleName()+"{" +
                "matcher=" + describeMatcher +
                ", scope=" + scope.name() +
                '}';
    }

    public void setScope(RuleScope scope) {
        Assert.notNull(scope);
        this.scope = scope;
    }

    public RuleScope getScope(){
        return scope;
    }

    @Override
    public int compareTo(GenerationRule o) {
        return o.scope.compareTo(o.scope);
    }

    public void matchAlways() {
        when(any(GenerationContext.class));
    }
}