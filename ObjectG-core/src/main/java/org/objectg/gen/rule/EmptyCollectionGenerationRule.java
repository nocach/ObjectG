package org.objectg.gen.rule;

import java.util.Collection;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.session.GenerationSession;

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
        Collection collection = (Collection) GenerationSession.get().generate(configurationOfCollection, context);
        return collection;
    }
}
