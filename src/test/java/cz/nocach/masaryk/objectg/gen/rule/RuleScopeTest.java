package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.fixtures.domain.Person;
import cz.nocach.masaryk.objectg.gen.RuleScope;
import cz.nocach.masaryk.objectg.matcher.ContextMatchers;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * User: __nocach
 * Date: 28.9.12
 */
public class RuleScopeTest {

    @Test
    public void propertyScopeVersusGlobal(){
        Person generated = ObjectG.unique(Person.class, ObjectG.config()
                .when(ContextMatchers.instancesOf(String.class))
                    .rule(Rules.value("propertyScope"), RuleScope.PROPERTY)
                .when(ContextMatchers.instancesOf(String.class))
                    .rule(Rules.value("globalScope"), RuleScope.GLOBAL));

        assertEquals("PROPERTY scoped is respected more than GLOBAL", "propertyScope", generated.getFirstName());
    }

    @Test
    public void defaultScopeVersusGlobal(){
        Person generated = ObjectG.unique(Person.class, ObjectG.config()
                .when(ContextMatchers.instancesOf(String.class))
                    .rule(Rules.value("globalScope"), RuleScope.GLOBAL)
                .when(ContextMatchers.instancesOf(String.class))
                    .rule(Rules.value("innerDefaultScope"), RuleScope.INTERNAL_DEFAULT));

        assertEquals("GLOBAL scope is respected more than INTERNAL_DEFAULT", "globalScope", generated.getFirstName());
    }
}
