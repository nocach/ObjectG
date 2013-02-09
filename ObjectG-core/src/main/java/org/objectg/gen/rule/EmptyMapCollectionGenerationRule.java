package org.objectg.gen.rule;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.session.GenerationSession;

/**
 * User: __nocach
 * Date: 14.10.12
 */
class EmptyMapCollectionGenerationRule extends GenerationRule {

    @Override
    public Object getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        GenerationConfiguration configurationOfCollection = currentConfiguration.clone();
        configurationOfCollection.setObjectsInCollections(0);
        configurationOfCollection.removeRule(this);
        return GenerationSession.get().generate(configurationOfCollection, context);
    }
}