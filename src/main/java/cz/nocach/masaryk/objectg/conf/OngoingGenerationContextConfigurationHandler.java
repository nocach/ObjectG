package cz.nocach.masaryk.objectg.conf;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.rule.Rules;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *     ConfigurationHandler that populates map of contexts for each individual configuration
 *     object created by {@link SetterConfigurator}. After object is considered to be configured you can call
 *     {@link #getGenerationConfiguration} to get corresponding configuration.
 * </p>
 * <p>
 * User: __nocach
 * Date: 16.9.12
 * </p>
 */
public class OngoingGenerationContextConfigurationHandler implements ConfigurationHandler {
    private Map<Integer, GenerationConfiguration> contextForObjects = new ConcurrentHashMap<Integer, GenerationConfiguration>();

    @Override
    public void onInit(Object objectUnderConfiguration) {
        int identityOfObject = System.identityHashCode(objectUnderConfiguration);
        if (contextForObjects.get(identityOfObject) == null){
            //we are extracting superclass, because SetterConfigurator is creating new subclass in runtime
            Class<?> realObjectClass = objectUnderConfiguration.getClass().getSuperclass();
            contextForObjects.put(identityOfObject, new GenerationConfiguration(realObjectClass));
        }
    }

    @Override
    public void onSetter(Object objectUnderConfiguration, String propertyName) {
        GenerationConfiguration generationContext = getOngoingGenerationConfiguration(objectUnderConfiguration);
        if (OngoingConfiguration.plannedRule != null) {
            addPlannedRuleForProperty(propertyName, generationContext);
        }
        if (needToUseConfigurationObject(objectUnderConfiguration)){
            addRuleFromConfigurationObject(propertyName, generationContext);
        }
        OngoingConfiguration.clear();
    }

    private void addRuleFromConfigurationObject(String propertyName, GenerationConfiguration generationContext) {
        GenerationConfiguration specificConfiguration =
                ObjectG.contextFromObjects(OngoingConfiguration.plannedConfigurationObject);
        GenerationRule specificConfigurationRule = Rules.specificConfiguration(specificConfiguration);
        specificConfigurationRule.setForProperty(propertyName);
        generationContext.addRule(specificConfigurationRule);
    }

    private boolean needToUseConfigurationObject(Object objectUnderConfiguration) {
        return objectUnderConfiguration instanceof InterceptedBySetterConfigurator
                && OngoingConfiguration.plannedConfigurationObject != null
                && OngoingConfiguration.plannedConfigurationObject != objectUnderConfiguration;
    }

    private GenerationConfiguration getOngoingGenerationConfiguration(Object objectUnderConfiguration) {
        int identityOfObject = System.identityHashCode(objectUnderConfiguration);
        return contextForObjects.get(identityOfObject);
    }

    private void addPlannedRuleForProperty(String propertyName, GenerationConfiguration generationContext) {
        GenerationRule plannedRule = OngoingConfiguration.plannedRule;
        plannedRule.setForProperty(propertyName);
        generationContext.addRule(plannedRule);
    }

    /**
     *
     * @param forObject not null object for which to return GenerationConfiguration
     * @return GenerationConfiguration for the object or null if no such object was configured by this handler.
     */
    public GenerationConfiguration getGenerationConfiguration(Object forObject){
        Assert.notNull(forObject);
        return contextForObjects.get(System.identityHashCode(forObject));
    }
}
