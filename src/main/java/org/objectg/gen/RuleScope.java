package org.objectg.gen;

/**
 * <p>
 *     Defines scope of the rule. Rules with BIGGER ordinal are applied FIRST.
 *     (ordinal is order in which enum constant is defined)
 * </p>
 * <p>
 *     During single generation more than one rule can matched for certain types and ONLY FIRST matched rule is applied
 *     during generation. RuleScope lets you define what rules must be checked for matching first.
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
     * rule is considered to be turned on as a default during generation, so it will be checked as last
     */
    INTERNAL_DEFAULT,
    /**
     * rule is considered to be globally applied (e.g. rule must be used for every generation of some type)
     */
    GLOBAL,
    /**
     * rule is considered to be applied only on the certain property, so it will be checked as first
     */
    PROPERTY,
}
