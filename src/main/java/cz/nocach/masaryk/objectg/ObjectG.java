package cz.nocach.masaryk.objectg;

import cz.nocach.masaryk.objectg.gen.GeneratorRegistry;
import cz.nocach.masaryk.objectg.conf.GenerationConfiguration;
import cz.nocach.masaryk.objectg.conf.OngoingConfiguration;
import cz.nocach.masaryk.objectg.conf.OngoingGenerationContextConfigurationHandler;
import cz.nocach.masaryk.objectg.conf.SetterConfigurator;
import cz.nocach.masaryk.objectg.gen.GenerationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *     User-facing api of the framework.
 * </p>
 * <p>
 * User: __nocach
 * Date: 26.8.12
 * </p>
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
        return generate(clazz, configuration);
    }

    public static <T> T generate(Class<T> clazz, GenerationConfiguration configuration) {
        return (T) GeneratorRegistry.getInstance().generate(configuration, new GenerationContext(clazz));
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
            GenerationConfiguration contextForObject = configurationHandler.getGenerationConfiguration(each);
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

    /**
     *
     * @param clazz type of the objects for list
     * @param <T>
     * @return
     */
    public static <T> List<T> generateList(Class<T> clazz) {
        return generateList(clazz, 1);
    }

    /**
     *
     * @param clazz type of the objects for list
     * @param size how many objects to generate
     * @param <T>
     * @return
     */
    public static <T> List<T> generateList(Class<T> clazz, int size) {
        Assert.isTrue(size >= 0, "size must be >= 0");
        List<T> result = new ArrayList<T>(size);
        for (int i = 0; i < size; i++){
            result.add(i, unique(clazz));
        }
        return result;
    }

    /**
     *
     * @param clazz type of the objects for set
     * @param <T>
     * @return
     */
    public static <T> Set<T> generateSet(Class<T> clazz) {
        return generateSet(clazz, 1);
    }

    public static <T> Set<T> generateSet(Class<T> clazz, int size){
        Assert.isTrue(size >= 0, "size must be >= 0");
        Set<T> result = new HashSet<T>(size);
        for (int i = 0; i < size; i++){
            result.add(unique(clazz));
        }
        return result;
    }
}
