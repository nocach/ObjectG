package org.objectg.gen.rule;

import java.util.Map;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.GeneratorRegistry;

/**
 * User: __nocach
 * Date: 14.10.12
 */
class EmptyMapCollectionGenerationRule extends GenerationRule<Map> {

    @Override
    public Map getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        GenerationConfiguration configurationOfCollection = currentConfiguration.clone();
        configurationOfCollection.setObjectsInCollections(0);
        configurationOfCollection.removeRule(this);
        Map collection = (Map) GeneratorRegistry.getInstance().generate(configurationOfCollection, context);
        return collection;
    }
}