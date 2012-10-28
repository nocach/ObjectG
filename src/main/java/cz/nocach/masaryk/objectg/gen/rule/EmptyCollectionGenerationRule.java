package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GenerationRule;

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
