package cz.nocach.masaryk.objectg.gen.impl;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GeneratorRegistry;

import java.util.HashSet;
import java.util.Set;

/**
 * User: __nocach
 * Date: 28.9.12
 */
class SetGenerator extends CollectionGenerator<Set> {
    @Override
    protected void addNewObject(Set collection, GenerationConfiguration configuration, GenerationContext contextOfCollection) {
        GenerationContext contextForGenertingSetObj = getContextForGenericType(contextOfCollection, 0);
        Object objectForCollection = GeneratorRegistry.getInstance().generate(configuration, contextForGenertingSetObj);
        collection.add(objectForCollection);
    }

    @Override
    protected Set createInstanceForAbstractType() {
        return new HashSet();
    }

    @Override
    public boolean supportsType(Class type) {
        return Set.class.isAssignableFrom(type);
    }
}
