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
class ListGenerator extends CollectionGenerator<List>{


    @Override
    public boolean supportsType(Class type) {
        return List.class.isAssignableFrom(type);
    }

    @Override
    protected void addNewObject(List collection,
                                GenerationConfiguration configuration, GenerationContext contextOfCollection) {
        GenerationContext contextForCollectionsObject = getContextForGeneratingObjectsOfCollection(contextOfCollection);
        Object generatedValue = GeneratorRegistry.getInstance().generate(configuration, contextForCollectionsObject);
        collection.add(generatedValue);
    }

    private <T> GenerationContext getContextForGeneratingObjectsOfCollection(GenerationContext<T> context) {
        return getContextForGenericType(context, 0);
    }

    @Override
    protected List createInstanceForAbstractType() {
        return new ArrayList();
    }
}
