package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import cz.nocach.masaryk.objectg.gen.GenerationRule;

/**
 * User: __nocach
 * Date: 23.9.12
 */

public class SingleValueGenerationRule<T> extends GenerationRule{
    private T value;

    public SingleValueGenerationRule(T value){
        this.value = value;
    }
    @Override
    protected <T> T getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        return (T)value;
    }
}
