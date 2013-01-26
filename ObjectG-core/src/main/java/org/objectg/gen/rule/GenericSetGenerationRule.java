package org.objectg.gen.rule;

import java.util.Set;

import org.objectg.ObjectG;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;

/**
 * User: __nocach
 * Date: 28.9.12
 */
class GenericSetGenerationRule extends GenerationRule<Set>{
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
    public Set getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        return ObjectG.uniqueSet(classOfObjects, size);
    }
}
