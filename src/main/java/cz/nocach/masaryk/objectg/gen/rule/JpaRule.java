package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.RuleScope;
import cz.nocach.masaryk.objectg.gen.jpa.Relation;
import cz.nocach.masaryk.objectg.gen.jpa.RelationValueProvider;

import java.util.Collection;
import java.util.List;

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
        if (context.getField() == null
                && context.getParentField() == null) return false;
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
        boolean matchedGeneratingField = context.getField() != null && context.getField().getName().equals(expectedProperty);
        boolean matchedParentCollectionField = context.getParentField() != null
                && Collection.class.isAssignableFrom(context.getParentField().getType())
                && context.getParentField().getName().equals(expectedProperty);
        return expectedClass.equals(context.getClassThatIsGenerated())
                && (matchedGeneratingField || matchedParentCollectionField);
    }

    private boolean isTargetGenerating(GenerationContext context, Relation each) {
        return isContextForExpectedProperty(context, each.getOwner(), each.getTargetProperty());
    }
}
