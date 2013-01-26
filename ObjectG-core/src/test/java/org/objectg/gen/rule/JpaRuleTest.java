package org.objectg.gen.rule;

import org.junit.Before;
import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.RuleScope;
import org.objectg.gen.jpa.JpaRelationTest;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * User: __nocach
 * Date: 13.10.12
 */
public class JpaRuleTest extends BaseObjectGTest {

    private JpaRule rule;

    @Before
    public void setup(){
        rule = new JpaRule(RuleScope.INTERNAL_DEFAULT);
    }

    @Test
    public void doesNotMatchForNotEntity(){
        GenerationContext context = GenerationContext.createRoot(String.class);
        assertFalse(rule.matches(context));
    }

    @Test
    public void doesNotMatchForEntityWithoutRelations(){
        GenerationContext context = GenerationContext.createRoot(EntityWithoutRelations.class);
        assertFalse(rule.matches(context));
    }

    @Test
    public void doesNotMatchForEntityWithRelationButWithoutHierarchy(){
        GenerationContext context = GenerationContext.createRoot(JpaRelationTest.Organization.class);
        assertFalse("root", rule.matches(context));
        GenerationContext listStep = context.push(List.class, new JpaRelationTest.Organization());
        assertFalse("listStep", rule.matches(listStep));
        GenerationContext unitStep = listStep.push(JpaRelationTest.OrganizationUnit.class, new ArrayList());
        assertFalse("unitStep", rule.matches(listStep));
    }

    @Test
    public void matchForEntityWithRelationAndHierarchyCreatingInTarget() throws NoSuchFieldException {
        GenerationContext context = GenerationContext.createRoot(JpaRelationTest.OrganizationUnit.class);
        GenerationContext organizationStep = context.push(JpaRelationTest.Organization.class, new JpaRelationTest.OrganizationUnit());
        GenerationContext listStep = organizationStep.push(List.class, new JpaRelationTest.Organization());
        GenerationContext organizationUnitStep = listStep.push(JpaRelationTest.OrganizationUnit.class, new ArrayList());
        organizationUnitStep.setParentField(JpaRelationTest.Organization.class.getDeclaredField("units"));

        assertTrue(rule.matches(organizationUnitStep));

    }

    @Test
    public void matchForEntityWithRelationAndHierarchyCreatingInOwner() throws NoSuchFieldException {
        GenerationContext context = GenerationContext.createRoot(JpaRelationTest.Organization.class);
        GenerationContext listStep = context.push(List.class, new JpaRelationTest.Organization());
        GenerationContext unitStep = listStep.push(JpaRelationTest.OrganizationUnit.class, new ArrayList());
        GenerationContext organizationInUnitStep =
                unitStep.push(JpaRelationTest.Organization.class, new JpaRelationTest.OrganizationUnit());
        organizationInUnitStep.setField(JpaRelationTest.OrganizationUnit.class.getDeclaredField("organization"));

        assertTrue(rule.matches(organizationInUnitStep));
    }

    @Test
    public void valueFromHierarchyIsGeneratedWhenCreatingInTarget() throws NoSuchFieldException {
        GenerationContext context = GenerationContext.createRoot(JpaRelationTest.OrganizationUnit.class);
        JpaRelationTest.OrganizationUnit firstlyGeneratedUnit = new JpaRelationTest.OrganizationUnit();
        GenerationContext organizationStep = context.push(JpaRelationTest.Organization.class, firstlyGeneratedUnit);
        GenerationContext listStep = organizationStep.push(List.class, new JpaRelationTest.Organization());
        GenerationContext organizationUnitStep = listStep.push(JpaRelationTest.OrganizationUnit.class, new ArrayList());
        organizationUnitStep.setParentField(JpaRelationTest.Organization.class.getDeclaredField("units"));

        Object valueFromRule = rule.getValue(new GenerationConfiguration(), organizationUnitStep);

        assertEquals(firstlyGeneratedUnit, valueFromRule);
    }

    @Entity
    public static class EntityWithoutRelations{
        @Id
        private Long id;
    }
}
