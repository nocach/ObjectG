package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GenerationRule;

/**
 * User: __nocach
 * Date: 28.9.12
 */
class GenericSetGenerationRule extends GenerationRule{
    private Class classOfObjects;
    private int size;

    public GenericSetGenerationRule(Class classOfObjects){
        this(classOfObjects, 1);
    }

    public GenericSetGenerationRule(Class<String> clazzOfObjects, int size) {
        this.classOfObjects = clazzOfObjects;
        this.size = size;
    }

    @Override
    protected <T> T getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        return (T) ObjectG.generateSet(classOfObjects, size);
    }
}
