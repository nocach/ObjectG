package cz.nocach.masaryk.objectg.gen;

/**
 * <p>
 *      Knows about all generators in the framework. Allows to find suitable Generators by
 *      using {@link #find(GenerationContext, Class)}.
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
        throw new UnsupportedOperationException("can't find generator for context=" +context
            +" for generating type="+forType.getName());
    }

    public static final GeneratorRegistry getInstance(){
        return instance;
    }
}
