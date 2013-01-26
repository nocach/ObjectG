package org.objectg.gen.rule;

import java.util.List;

import org.objectg.ObjectG;
import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.GenerationRule;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 23.9.12
 */
class GenericListGenerationRule extends GenerationRule<List> {
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
    public List getValue(GenerationConfiguration currentConfiguration, GenerationContext context) {
        return ObjectG.uniqueList(genericDefinition, size);
    }
}
