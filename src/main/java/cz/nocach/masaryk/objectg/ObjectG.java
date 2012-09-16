package cz.nocach.masaryk.objectg;

import cz.nocach.masaryk.objectg.gen.GeneratorRegistry;
import cz.nocach.masaryk.objectg.gen.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.gen.conf.OngoingConfiguration;
import cz.nocach.masaryk.objectg.gen.conf.OngoingGenerationContextConfigurationHandler;
import cz.nocach.masaryk.objectg.gen.conf.SetterConfigurator;
import cz.nocach.masaryk.objectg.gen.context.GenerationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: __nocach
 * Date: 26.8.12
 */
public class ObjectG {
    private static final Logger logger = LoggerFactory.getLogger(ObjectG.class);
    private static final OngoingGenerationContextConfigurationHandler configurationHandler = new OngoingGenerationContextConfigurationHandler();
    private static final SetterConfigurator setterConfigurator = new SetterConfigurator(configurationHandler);

    public static <T> T unique(Class<T> clazz){
        return unique(clazz, new GenerationConfiguration());
    }

    public static <T> T unique(Class<T> clazz, GenerationConfiguration configuration){
        configuration.setUnique(true);
        return GeneratorRegistry.getInstance().find(configuration, new GenerationContext(clazz)).generate(clazz);
    }

    /**
     * will create GenerationCOnfiguration from {@code configuredObjects} for this generation
     */
    public static <T> T unique(Class<T> clazz, Object... configuredObjects){
        return unique(clazz, contextFromObjects(configuredObjects));
    }

    public static <T> T config(Class<T> clazz){
        T result = setterConfigurator.newConfigurator(clazz);
        OngoingConfiguration.plannedConfigurationObject = result;
        return result;
    }

    public static GenerationConfiguration contextFromObjects(Object... objects){
        GenerationConfiguration result = new GenerationConfiguration();
        for (Object each : objects){
            GenerationConfiguration contextForObject = configurationHandler.getGenerationContext(each);
            if (contextForObject == null){
                logger.debug("no GenerationContext contained in configurationHanlder for object " + each
                    +" was this object created using ObjectG.config(Class)?");
            }
            else {
                result.putChild(contextForObject);
            }
        }
        return result;
    }
}
