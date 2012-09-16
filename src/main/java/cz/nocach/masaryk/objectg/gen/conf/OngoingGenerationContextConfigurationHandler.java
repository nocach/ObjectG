package cz.nocach.masaryk.objectg.gen.conf;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.rule.SpecificConfigurationGenerationRule;
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
        GenerationRule specificConfigurationRule = new SpecificConfigurationGenerationRule(specificConfiguration);
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

    public GenerationConfiguration getGenerationContext(Object forObject){
        Assert.notNull(forObject);
        return contextForObjects.get(System.identityHashCode(forObject));
    }
}
