package cz.nocach.masaryk.objectg.gen;

import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * <p>
 *      Knows about all generators in the framework. Allows to find suitable Generators for the context by
 *      using {@link #find(GenerationContext)}.
 * </p>
 * <p>
 * User: __nocach
 * Date: 28.8.12
 * </p>
 */
public class GeneratorRegistry {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorRegistry.class);

    private final CompositeGenerator generatorChain = new CompositeGenerator(
            new NativeClassGenerator(),
            new CollectionGenerator(),
            new NotNativeClassGenerator());
    private static final GeneratorRegistry instance = new GeneratorRegistry();

    /**
     *
     *
     * @param context not null context for which generator must be found
     * @return most matched Generator for the passed context
     * @throws UnsupportedOperationException if generator for the passed context was not found
     */
    public <T> T generate(GenerationConfiguration configuration,  GenerationContext context) {
        Assert.notNull(context, "context");

        return (T)generatorChain.generate(configuration, context);
    }

    public static final GeneratorRegistry getInstance(){
        return instance;
    }
}
