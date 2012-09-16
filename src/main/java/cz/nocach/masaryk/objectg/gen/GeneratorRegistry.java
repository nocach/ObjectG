package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.gen.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.context.GenerationContext;
import org.springframework.util.Assert;

/**
 * <p>
 *      Knows about all generators in the framework. Allows to find suitable Generators by
 *      using {@link #find(GenerationConfiguration, GenerationContext)}.
 * </p>
 * <p>
 * User: __nocach
 * Date: 28.8.12
 * </p>
 */
public class GeneratorRegistry {
    private UniqueGenerator nativeClassUniqueGenerator;
    private static final GeneratorRegistry instance = new GeneratorRegistry();

    private GeneratorRegistry(){
        this.nativeClassUniqueGenerator = new NativeClassUniqueGenerator();
    }

    public Generator find(GenerationConfiguration generationConfiguration, GenerationContext context) {
        Assert.notNull(generationConfiguration, "generationConfiguration");
        Assert.notNull(context, "context");

        GenerationRule rule = generationConfiguration.getRule(context);
        if (rule != null){
            return rule.getGenerator();
        }

        if (generationConfiguration.isUnique()){
            if (nativeClassUniqueGenerator.supportsType(context.getClassThatIsGenerated())) return nativeClassUniqueGenerator;
            return new NotNativeClassGenerator(generationConfiguration);
        }
        throw new UnsupportedOperationException("can't find generator for context=" +context
            +" for generating type="+context.getClassThatIsGenerated().getName());
    }

    public static final GeneratorRegistry getInstance(){
        return instance;
    }
}
