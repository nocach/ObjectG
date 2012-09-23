package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.gen.Generator;
import org.springframework.util.Assert;

/**
 * <p>
 *     Generator supporting single base class (and it's derivations)
 * </p>
 * <p>
 * User: __nocach
 * Date: 15.9.12
 * </p>
 */
public abstract class GeneratorForSingleClass extends Generator{
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
