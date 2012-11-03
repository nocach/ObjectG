package org.objectg.gen.rule;

import java.util.Collection;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.GeneratorRegistry;

/**
 * User: __nocach
 * Date: 28.9.12
 */
class CollectionGenerationRule<T> extends GenerationRule<T>{
    private final Class<? extends Collection> collectionClass;
    private final Class classOfObjects;

    public CollectionGenerationRule(Class<? extends Collection> collectionClass, Class classOfObjects){
        this.collectionClass = collectionClass;
        this.classOfObjects = classOfObjects;
    }

    @Override
    public T getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        GenerationConfiguration configurationOfCollection = currentConfiguration.clone();
        configurationOfCollection.setObjectsInCollections(0);
		configurationOfCollection.removeRule(this);
        Collection collection = GeneratorRegistry.getInstance().generate(configurationOfCollection, context);
        collection.add(GeneratorRegistry.getInstance().generate(currentConfiguration, context.push(classOfObjects)));
        return (T)collection;
    }
}
