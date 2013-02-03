package org.objectg.gen.rule;

import java.util.Collection;
import java.util.List;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.RuleScope;
import org.objectg.gen.jpa.Relation;
import org.objectg.gen.jpa.RelationValueProvider;

/**
 * User: __nocach
 * Date: 12.10.12
 */
class JpaRule extends GenerationRule<Object>{
    private RelationValueProvider relationValueProvider;
    JpaRule(RuleScope scope) {
        super(scope);
        relationValueProvider = new RelationValueProvider();
    }

    @Override
    public Object getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        List<Relation> relations = relationValueProvider.getRelationsFor(context.getClassThatIsGenerated());
        return findGeneratedObjectFromRelations(context, relations);
    }

    @Override
    public boolean matches(GenerationContext context) {
        List<Relation> relations = relationValueProvider.getRelationsFor(context.getClassThatIsGenerated());
        if (relations.isEmpty()) return false;
        if (context.getPropertyAccessor() == null
                && context.getParentPropertyAccessor() == null) return false;
        Object objectForRelation = findGeneratedObjectFromRelations(context, relations);
        return objectForRelation != null;
    }

    private Object findGeneratedObjectFromRelations(GenerationContext context, List<Relation> relations) {
        Object objectForRelation = null;
        for (Relation each : relations){
            if (isTargetGenerating(context, each)){
                Object lastGeneratedTarget = context.getLastGeneratedObject(context.getClassThatIsGenerated());
                if (lastGeneratedTarget == null) continue;
                objectForRelation = lastGeneratedTarget;
            }
            if (isOwnerGenerating(context, each)){
                Object lastGeneratedTarget = context.getLastGeneratedObject(context.getClassThatIsGenerated());
                if (lastGeneratedTarget == null) continue;
                objectForRelation = lastGeneratedTarget;
            }
        }
        return objectForRelation;
    }

    private boolean isOwnerGenerating(GenerationContext context, Relation each) {
        Class expectedClass = each.getTarget();
        String expectedProperty = each.getOwnerProperty();
        return isContextForExpectedProperty(context, expectedClass, expectedProperty);
    }

    private boolean isContextForExpectedProperty(GenerationContext context, Class expectedClass, String expectedProperty) {
        boolean matchedGeneratingField = context.getPropertyAccessor() != null && context.getPropertyAccessor().getName().equals(expectedProperty);
        boolean matchedParentCollectionField = context.getParentPropertyAccessor() != null
                && Collection.class.isAssignableFrom(context.getParentPropertyAccessor().getType())
                && context.getParentPropertyAccessor().getName().equals(expectedProperty);
        return expectedClass.equals(context.getClassThatIsGenerated())
                && (matchedGeneratingField || matchedParentCollectionField);
    }

    private boolean isTargetGenerating(GenerationContext context, Relation each) {
        return isContextForExpectedProperty(context, each.getOwner(), each.getTargetProperty());
    }
}
