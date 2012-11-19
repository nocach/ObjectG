package org.objectg.gen.rule;

import org.junit.Test;
import org.objectg.ObjectG;
import org.objectg.fixtures.domain.Person;
import org.objectg.gen.RuleScope;
import org.objectg.matcher.ContextMatchers;

import static org.junit.Assert.assertEquals;

/**
 * User: __nocach
 * Date: 28.9.12
 */
public class RuleScopeTest {

    @Test
    public void propertyScopeVersusGlobal(){
        Person generated = ObjectG.unique(Person.class, ObjectG.config()
                .when(ContextMatchers.typeOf(String.class))
                    .useRule(Rules.value("propertyScope"), RuleScope.PROPERTY)
                .when(ContextMatchers.typeOf(String.class))
                    .useRule(Rules.value("globalScope"), RuleScope.GLOBAL));

        assertEquals("PROPERTY scoped is respected more than GLOBAL", "propertyScope", generated.getFirstName());
    }

    @Test
    public void defaultScopeVersusGlobal(){
        Person generated = ObjectG.unique(Person.class, ObjectG.config()
                .when(ContextMatchers.typeOf(String.class))
                    .useRule(Rules.value("globalScope"), RuleScope.GLOBAL)
                .when(ContextMatchers.typeOf(String.class))
                    .useRule(Rules.value("innerDefaultScope"), RuleScope.INTERNAL_DEFAULT));

        assertEquals("GLOBAL scope is respected more than INTERNAL_DEFAULT", "globalScope", generated.getFirstName());
    }
}
