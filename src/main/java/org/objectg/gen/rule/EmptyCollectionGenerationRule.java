package org.objectg.gen.rule;

import org.objectg.ObjectG;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;

import java.util.Collection;

/**
 * User: __nocach
 * Date: 14.10.12
 */
class EmptyCollectionGenerationRule extends GenerationRule<Collection> {

    @Override
    public Collection getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        GenerationConfiguration configurationOfCollection = currentConfiguration.clone();
        configurationOfCollection.setObjectsInCollections(0);
        configurationOfCollection.removeRule(this);
        Collection collection = (Collection) ObjectG.generate(context.getClassThatIsGenerated(), configurationOfCollection);
        return collection;
    }
}
