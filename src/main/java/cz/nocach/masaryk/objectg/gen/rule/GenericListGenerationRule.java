package cz.nocach.masaryk.objectg.gen.rule;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 23.9.12
 */
class GenericListGenerationRule extends GenerationRule {
    private final Class genericDefinition;
    private final int size;

    public GenericListGenerationRule(Class genericDefinition){
        this(genericDefinition, 1);
    }
    public GenericListGenerationRule(Class genericDefinition, int size){
        Assert.isTrue(size >= 0, "size must be >= 0");
        this.genericDefinition = genericDefinition;
        this.size = size;
    }
    @Override
    protected <T> T getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        return (T)ObjectG.generateList(genericDefinition, size);
    }
}
