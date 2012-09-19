package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.gen.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.context.GenerationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * <p>
 *      Knows about all generators in the framework. Allows to find suitable Generators for the context by
 *      using {@link #find(GenerationConfiguration, GenerationContext)}.
 * </p>
 * <p>
 * User: __nocach
 * Date: 28.8.12
 * </p>
 */
public class GeneratorRegistry {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorRegistry.class);

    private UniqueGenerator nativeClassUniqueGenerator;
    private static final GeneratorRegistry instance = new GeneratorRegistry();

    private GeneratorRegistry(){
        this.nativeClassUniqueGenerator = new NativeClassUniqueGenerator();
    }

    /**
     *
     * @param generationConfiguration not null configuration to be used during search
     * @param context not null context for which generator must be found
     * @return most matched Generator for the passed context
     * @throws UnsupportedOperationException if generator for the passed context was not found
     */
    public Generator find(GenerationConfiguration generationConfiguration, GenerationContext context) {
        Assert.notNull(generationConfiguration, "generationConfiguration");
        Assert.notNull(context, "context");


        GenerationRule rule = generationConfiguration.getRule(context);
        if (rule != null){
            return rule.getGenerator(generationConfiguration);
        }

        if (generationConfiguration.isUnique()){
            if (nativeClassUniqueGenerator.supportsType(context.getClassThatIsGenerated())) {
                logger.debug("return native class unique generator for context="+context);
                return nativeClassUniqueGenerator;
            }
            logger.debug("return not native class generator for context="+context);
            return new NotNativeClassGenerator(generationConfiguration);
        }
        throw new UnsupportedOperationException("can't find generator for context=" +context
            +" for generating type="+context.getClassThatIsGenerated().getName());
    }

    public static final GeneratorRegistry getInstance(){
        return instance;
    }
}
