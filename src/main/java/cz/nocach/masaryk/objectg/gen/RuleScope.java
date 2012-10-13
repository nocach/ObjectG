package cz.nocach.masaryk.objectg.gen;

/**
 * <p>
 *     Defines scope of the rule.
 * </p>
 * <p>
 *     E.g. some rule can be global {@code nullFor(anyClass())} but at the same time
 *     you want to be able to use more close rules for certain class or property
 *     (as {@code value("someValue") for property='name'}.) as an analogy can be seen weight in layout frameworks.
 * </p>
 * <p>
 * User: __nocach
 * Date: 30.9.12
 * </p>
 */
public enum RuleScope {
    /**
     * rule is applied as the default
     */
    INTERNAL_DEFAULT,
    /**
     * rule is globally applied
     */
    GLOBAL,
    /**
     * rule is applied to property
     */
    PROPERTY,
}
