package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.matcher.ClassGenerationContextFeature;
import cz.nocach.masaryk.objectg.matcher.FieldNameMatcher;
import cz.nocach.masaryk.objectg.matcher.JavaNativeTypeMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.springframework.util.Assert;

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
public abstract class GenerationRule implements Comparable<GenerationRule>{
    private Matcher<GenerationContext> matcher;
    private RuleScope scope = RuleScope.GLOBAL;

    /**
     *
     * @param matcher matcher must have defined equals, so only one rule for same condition exists
     */
    public void when(Matcher<GenerationContext> matcher) {
        this.matcher = matcher;
    }

    protected abstract <T> T getValue(GenerationConfiguration currentConfiguration, GenerationContext context);

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
                "when(matcher) must be called");
    }

    @Override
    public String toString() {
        StringDescription describeMatcher = new StringDescription();
        matcher.describeTo(describeMatcher);
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
        if (o.scope.compareTo(o.scope) == 0) {
            //prevent vanishing of rules with same scope
            return -1;
        }
        return o.scope.compareTo(o.scope);
    }
}
