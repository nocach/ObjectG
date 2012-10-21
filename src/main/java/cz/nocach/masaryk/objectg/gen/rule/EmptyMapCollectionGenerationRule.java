package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GenerationRule;

import java.util.Map;

/**
 * User: __nocach
 * Date: 14.10.12
 */
class EmptyMapCollectionGenerationRule extends GenerationRule<Map> {

    @Override
    protected Map getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        GenerationConfiguration configurationOfCollection = currentConfiguration.clone();
        configurationOfCollection.setObjectsInCollections(0);
        configurationOfCollection.removeRule(this);
        Map collection = (Map) ObjectG.generate(context.getClassThatIsGenerated(), configurationOfCollection);
        return collection;
    }
}