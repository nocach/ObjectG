package cz.nocach.masaryk.objectg.gen.impl;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GenerationException;
import cz.nocach.masaryk.objectg.gen.Generator;
import cz.nocach.masaryk.objectg.util.Generics;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * User: __nocach
 * Date: 28.9.12
 */
abstract class CollectionGenerator<CollectionT> extends Generator {
    @Override
    public final <T> T generateValue(GenerationConfiguration configuration, GenerationContext<T> context) {
        if (context.getField() == null
                && configuration.getObjectsInCollections() > 0){
            throw new IllegalArgumentException("can't generate collection with objects for context without field"
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
            Field contextField = fromContext.getField();
            genericType = Generics.extractTypeFromGenerics(contextField, typeVarIndex);
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
                    +context.getClassThatIsGenerated(), e);
        } catch (IllegalAccessException e) {
            throw new GenerationException("could not generated instance of collection.type="
                    +context.getClassThatIsGenerated(), e);
        }
    }

    protected abstract CollectionT createInstanceForAbstractType();
}
