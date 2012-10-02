package cz.nocach.masaryk.objectg.gen.impl;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GeneratorRegistry;

import java.util.ArrayList;
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
