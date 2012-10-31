package org.objectg.gen;

import org.objectg.conf.GenerationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class CompositeGenerator extends Generator{
    private static final Logger logger = LoggerFactory.getLogger(CompositeGenerator.class);
    private Generator[] generators;
    public CompositeGenerator(Generator... generators){
        this.generators = generators;
    }

    @Override
    public Object generateValue(GenerationConfiguration configuration, GenerationContext context) {
        for (Generator each : generators){
            if (each.supportsType(context.getClassThatIsGenerated())){
                logger.debug("found generator=" +each.getClass().getSimpleName()
                        +" for conf="+configuration + " context="+context);
                return each.generateValue(configuration, context);
            }
        }
        throw new IllegalArgumentException("can't generate value for context=" + context);
    }

    @Override
    public boolean supportsType(Class type) {
        for (Generator each : generators){
            if (each.supportsType(type)) return true;
        }
        return false;
    }

}
