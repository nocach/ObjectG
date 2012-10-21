package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GenerationRule;

/**
 * User: __nocach
 * Date: 23.9.12
 */

class SingleValueGenerationRule<T> extends GenerationRule<T>{
    private T value;

    public SingleValueGenerationRule(T value){
        this.value = value;
    }
    @Override
    protected T getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        return value;
    }
}
