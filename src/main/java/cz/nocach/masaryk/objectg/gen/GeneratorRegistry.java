package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.gen.context.GenerationContext;

/**
 * <p>
 *      Knows about all generators in the framework. Allows to find suitable Generators by
 *      using {@link #find(cz.nocach.masaryk.objectg.gen.context.GenerationContext, Class)}.
 * </p>
 * <p>
 * User: __nocach
 * Date: 28.8.12
 * </p>
 */
public class GeneratorRegistry {
    private UniqueGenerator nativeClassUniqueGenerator;
    private UniqueGenerator notNativeClassUniqueGenerator;
    private static final GeneratorRegistry instance = new GeneratorRegistry();

    private GeneratorRegistry(){
        this.nativeClassUniqueGenerator = new NativeClassUniqueGenerator();
        this.notNativeClassUniqueGenerator = new NotNativeClassUniqueGenerator();
    }

    public Generator find(GenerationContext context, Class forType) {
        if (context.isUnique){
            if (nativeClassUniqueGenerator.supportsType(forType)) return nativeClassUniqueGenerator;
            return notNativeClassUniqueGenerator;
        }
        //TODO: to be written
//        for (Rule each :context.getRules()){
//            if (each.matches(forType)){
//                return ruleBasedGeneratorFactory.from(each);
//            }
//        }
        throw new UnsupportedOperationException("can't find generator for context=" +context
            +" for generating type="+forType.getName());
    }

    public static final GeneratorRegistry getInstance(){
        return instance;
    }
}
