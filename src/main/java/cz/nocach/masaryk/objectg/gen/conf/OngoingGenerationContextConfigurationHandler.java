package cz.nocach.masaryk.objectg.gen.conf;

import cz.nocach.masaryk.objectg.gen.GenerationRule;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: __nocach
 * Date: 16.9.12
 */
public class OngoingGenerationContextConfigurationHandler implements ConfigurationHandler {
    private Map<Integer, GenerationConfiguration> contextForObjects = new ConcurrentHashMap<Integer, GenerationConfiguration>();
    @Override
    public void onSetter(Object objectUnderConfiguration, String propertyName) {
        int identityOfObject = System.identityHashCode(objectUnderConfiguration);
        if (contextForObjects.get(identityOfObject) == null){
            //we are extracting superclass, because SetterConfigurator is creating new subclass in runtime
            Class<?> realObjectClass = objectUnderConfiguration.getClass().getSuperclass();
            contextForObjects.put(identityOfObject, new GenerationConfiguration(realObjectClass));
        }
        GenerationConfiguration generationContext = contextForObjects.get(identityOfObject);
        if (OngoingConfiguration.plannedRule == null){
            //TODO: put NullRule that will ALWAYS generate null for the property
        }
        else {
            GenerationRule plannedRule = OngoingConfiguration.plannedRule;
            plannedRule.setForProperty(propertyName);
            generationContext.addRule(plannedRule);
            OngoingConfiguration.plannedRule = null;
        }
    }

    public GenerationConfiguration getGenerationContext(Object forObject){
        Assert.notNull(forObject);
        return contextForObjects.get(System.identityHashCode(forObject));
    }
}
