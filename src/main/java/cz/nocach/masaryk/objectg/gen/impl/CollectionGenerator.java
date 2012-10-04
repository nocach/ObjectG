package cz.nocach.masaryk.objectg.gen.impl;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GenerationException;
import cz.nocach.masaryk.objectg.gen.Generator;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
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

    protected <T> GenerationContext getContextForGenericType(GenerationContext<T> context, int typeVarIndex) {
        if (!(context.getField().getGenericType() instanceof ParameterizedType)){
            throw new GenerationException("no generic information for field, context="+context);
        }
        Type[] actualTypeArguments = ((ParameterizedType) context.getField().getGenericType()).getActualTypeArguments();
        //TODO: process all implementations of Type: GenericArrayTypeImpl, ParametrizedTypeImpl,
        // TypeVariableImpl, WildcardTypeImple
        Type firstType = actualTypeArguments[typeVarIndex];
        return context.push((Class) firstType);
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
