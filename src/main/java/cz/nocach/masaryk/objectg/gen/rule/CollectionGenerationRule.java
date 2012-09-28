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
public class CollectionGenerationRule extends GenerationRule{
    private final Class<? extends Collection> collectionClass;
    private final Class classOfObjects;

    public CollectionGenerationRule(Class<? extends Collection> collectionClass, Class classOfObjects){
        this.collectionClass = collectionClass;
        this.classOfObjects = classOfObjects;
    }

    @Override
    protected <T> T getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        GenerationConfiguration configurationOfCollection = currentConfiguration.newWithOverride(currentConfiguration);
        configurationOfCollection.setObjectsInCollections(0);
        Collection collection = ObjectG.generate(collectionClass, configurationOfCollection);
        collection.add(ObjectG.unique(classOfObjects));
        return (T)collection;
    }
}
