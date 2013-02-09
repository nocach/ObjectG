package org.objectg.gen;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.matcher.impl.PropertyNameMatcher;
import org.springframework.util.Assert;

import static org.hamcrest.Matchers.any;

/**
 * <p>
 *     GenerationRule defines what specific rules and when are used for generating of property value.
 * </p>
 * <p>
 *     Generation rules is a main form of customising and altering process of generation. Two important methods
 *     of this class are {@link #when(org.hamcrest.Matcher)} and
 *     {@link #getValue(org.objectg.conf.GenerationConfiguration, GenerationContext)}.
 * </p>
 * <p>
 *     {@link #when(org.hamcrest.Matcher)} defines conditions under which given GenerationRule should be applied.
 *     Conditions way be as flexible as you would like. During single generation {@link GenerationContext} will be
 *     passed FOR ALL properties of the generated object. Even root object is will be checked against matcher.
 *     This means that you can define GenerationRule that will match several properties or several types. Or you
 *     can define property for single property. For common matchers refer to {@link org.objectg.matcher.ContextMatchers}
 * </p>
 * <p>
 *     {@link #getValue(org.objectg.conf.GenerationConfiguration, GenerationContext)} is called when matcher set to
 *     this GenerationRule successfully matched some {@link GenerationContext}. Implementors should provide logic
 *     for returning value. Returned value will be used as a value generated for the passed {@link GenerationContext}.
 *     <b>Important Note:</b>
 *     <P>
 *         Rules that store state (e.g. rule that generates value with respect to the number of invocations) must
 *         store that state using {@link org.objectg.gen.session.GenerationSession#createManagedState(Object, org.objectg.gen.session.SessionStateDescription)}
 *         This is required to guarantee isolation of tests. GenerationRule can be used globally (e.g. using
 *         {@link org.objectg.conf.defaults.AbstractObjectGConfiguration}}) and if rule generates values according
 *         to inner state, then different test suites will bring different generation results for the same code base
 *     </P>
 * </p>
 * <p>
 *     Preferred way of setting rules is by means of prototypes, see {@link org.objectg.ObjectG#prototype(Class)}
 * </p>
 * <p>
 * User: __nocach
 * Date: 1.9.12
 * </p>
 */
public abstract class GenerationRule<T> implements Comparable<GenerationRule>{
    private Matcher<GenerationContext> matcher;
    private RuleScope scope = RuleScope.GLOBAL;

	/**
	 * create rule with scope {@link RuleScope#GLOBAL}
	 */
    public GenerationRule(){}

    protected GenerationRule(RuleScope scope) {
		Assert.notNull(scope, "scope should be not null");
        this.scope = scope;
    }

    /**
     *
     * @param matcher matcher to use
     */
    public void when(Matcher<GenerationContext> matcher) {
        this.matcher = matcher;
    }

	/**
	 *
	 * @param currentConfiguration configuration to be used when generating value
	 * @param context context for which value must be generated
	 * @return value for the passed parameters
	 */
    public abstract T getValue(GenerationConfiguration currentConfiguration, GenerationContext context);

    /**
     * @param parentClass class where property is
     * @param forProperty for which property this rule must be applied
     */
    public void setForProperty(Class<?> parentClass, String forProperty) {
        matcher = new PropertyNameMatcher(parentClass, forProperty);
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
