package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.gen.Generator;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 15.9.12
 */
public abstract class GeneratorForSingleClass implements Generator{
    private Class targetClass;

    public GeneratorForSingleClass(Class targetClass){
        Assert.notNull(targetClass);
        this.targetClass = targetClass;
    }
    @Override
    public boolean supportsType(Class type) {
        return targetClass.isAssignableFrom(type);
    }
}
