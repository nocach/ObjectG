package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GenerationRule;

import java.util.Collection;

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
        Collection collection = ObjectG.generate(collectionClass, configurationOfCollection);
        collection.add(ObjectG.unique(classOfObjects));
        return (T)collection;
    }
}
