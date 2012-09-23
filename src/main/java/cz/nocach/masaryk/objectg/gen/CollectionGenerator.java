package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: __nocach
 * Date: 22.9.12
 */
class CollectionGenerator extends Generator{
    @Override
    public <T> T generateValue(GenerationConfiguration configuration, GenerationContext<T> context) {
        if (context.getField() == null){
            throw new IllegalArgumentException("can't generate collection for context without field"
                    +", context="+context);
        }
        Collection collection = createCollectionInstance(context);
        // TypeVariableImpl, WildcardTypeImple
        if (!(context.getField().getGenericType() instanceof ParameterizedType)){
            throw new GenerationException("no generic information for field, context="+context);
        }
        GenerationContext contextForListObjects = getContextForGeneratingObjectsOfCollection(context);
        Object generatedValue = GeneratorRegistry.getInstance().generate(configuration, contextForListObjects);
        collection.add(generatedValue);
        return (T)collection;
    }

    private <T> GenerationContext getContextForGeneratingObjectsOfCollection(GenerationContext<T> context) {
        Type[] actualTypeArguments = ((ParameterizedType) context.getField().getGenericType()).getActualTypeArguments();
        //TODO: process all implementations of Type: GenericArrayTypeImpl, ParametrizedTypeImpl,
        Type firstType = actualTypeArguments[0];
        return new GenerationContext((Class) firstType);
    }

    private <T> Collection createCollectionInstance(GenerationContext<T> context) {
        if (Modifier.isAbstract(context.getClassThatIsGenerated().getModifiers())){
            return new ArrayList();
        }
        return new ArrayList<T>();
    }

    @Override
    public boolean supportsType(Class type) {
        return List.class.isAssignableFrom(type);
    }
}
