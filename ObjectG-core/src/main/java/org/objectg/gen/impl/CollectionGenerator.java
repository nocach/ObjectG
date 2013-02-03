package org.objectg.gen.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationException;
import org.objectg.gen.Generator;
import org.objectg.util.Generics;

/**
 * User: __nocach
 * Date: 28.9.12
 */
abstract class CollectionGenerator<CollectionT> extends Generator {
    @Override
    public final <T> T generateValue(GenerationConfiguration configuration, GenerationContext<T> context) {
        if (context.getPropertyAccessor() == null
                && configuration.getObjectsInCollections() > 0){
            throw new IllegalArgumentException("can't generate collection with objects for context without propertyAccessor"
                    +", context="+context);
        }
        CollectionT collection = createCollectionInstance(context);

        for (int i = 0; i < configuration.getObjectsInCollections(); i++){
            addNewObject(collection, configuration, context);
        }
        return (T)collection;
    }

    protected abstract void addNewObject(CollectionT collection,
                                         GenerationConfiguration configuration, GenerationContext contextOfCollection);

    protected boolean shouldAddObjectForContext(GenerationConfiguration configuration
            , GenerationContext contextForCollectionsObject) {
        if (!contextForCollectionsObject.isCycle()) return true;
        if (contextForCollectionsObject.isCycle()
                && configuration.getCycleStrategy().shouldGenerateValueInCollection()) return true;
        return false;
    }

    protected <T> GenerationContext getContextForGenericType(GenerationContext<T> context, int typeVarIndex) {

        Type genericType = extractGenericType(context, typeVarIndex);

        if (genericType == null){
            throw new GenerationException("no generic information for field, context="+context);
        }
        GenerationContext contextForGenType = context.push((Class) genericType);
        return contextForGenType;
    }

    private <T> Type extractGenericType(GenerationContext<T> fromContext, int typeVarIndex) {
        Type genericType = null;
        if (fromContext.getFieldPropertyDescriptor() != null){
            genericType = fromContext.getFieldPropertyDescriptor().getGenericTypeFromMethodsSignature(typeVarIndex);
            if (genericType != null) return genericType;
        }
        if (genericType == null){
            genericType = fromContext.getPropertyAccessor().getGenericType(typeVarIndex);
        }
        return genericType;
    }

    private <T> CollectionT createCollectionInstance(GenerationContext<T> context) {
        if (Modifier.isAbstract(context.getClassThatIsGenerated().getModifiers())){
            return createInstanceForAbstractType();
        }
        try {
            return (CollectionT)context.getClassThatIsGenerated().newInstance();
        } catch (InstantiationException e) {
            throw new GenerationException("could not generated instance of collection.type="
                    +context.getClassThatIsGenerated(), context, e);
        } catch (IllegalAccessException e) {
            throw new GenerationException("could not generated instance of collection.type="
                    +context.getClassThatIsGenerated(), context, e);
        }
    }

    protected abstract CollectionT createInstanceForAbstractType();
}
