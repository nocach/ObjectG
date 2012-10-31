package org.objectg.gen.rule;

import org.objectg.ObjectG;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;

import java.util.Map;

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
        Map collection = (Map) ObjectG.generate(context.getClassThatIsGenerated(), configurationOfCollection);
        return collection;
    }
}