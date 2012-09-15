package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.gen.context.GenerationContext;

/**
 * User: __nocach
 * Date: 28.8.12
 */
public class NotNativeClassUniqueGenerator extends NotNativeClassGenerator implements UniqueGenerator {
    private GenerationContext uniqueGenerationContext;
    public NotNativeClassUniqueGenerator(){
        uniqueGenerationContext = new GenerationContext();
        uniqueGenerationContext.isUnique = true;
    }

    @Override
    protected GenerationContext getGenerationContext(Class generatingClass) {
        return uniqueGenerationContext;
    }
}
