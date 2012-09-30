package cz.nocach.masaryk.objectg.conf;

import cz.nocach.masaryk.objectg.ObjectG;
import cz.nocach.masaryk.objectg.gen.GenerationRule;
import cz.nocach.masaryk.objectg.gen.RuleScope;
import cz.nocach.masaryk.objectg.gen.rule.Rules;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *     ConfigurationHandler that populates map of contexts for each individual configuration
 *     object created by {@link SetterConfigurator}. After object is considered to be configured you can call
 *     {@link #getRules} to get corresponding configuration.
 * </p>
 * <p>
 * User: __nocach
 * Date: 16.9.12
 * </p>
 */
public class OngoingGenerationContextConfigurationHandler implements ConfigurationHandler {
    private Map<Integer, LinkedList<GenerationRule>> contextForObjects =
            new ConcurrentHashMap<Integer, LinkedList<GenerationRule>>();

    @Override
    public void onInit(Object objectUnderConfiguration) {
        int identityOfObject = System.identityHashCode(objectUnderConfiguration);
        if (contextForObjects.get(identityOfObject) == null){
            //we are extracting superclass, because SetterConfigurator is creating new subclass in runtime
            contextForObjects.put(identityOfObject, new LinkedList<GenerationRule>());
        }
    }

    private Class<?> getRealObjectClass(Object objectUnderConfiguration) {
        return objectUnderConfiguration.getClass().getSuperclass();
    }

    @Override
    public void onSetter(Object objectUnderConfiguration, Object value, String propertyName) {
        List<GenerationRule> configuredRules = getOngoingGenerationConfiguration(objectUnderConfiguration);
        if (OngoingConfiguration.plannedRule != null) {
            addPlannedRuleForProperty(objectUnderConfiguration, propertyName, configuredRules);
        }
        else if (needToUseConfigurationObject(objectUnderConfiguration)){
            addRuleFromConfigurationObject(objectUnderConfiguration, propertyName, configuredRules);
        }
        else {
            addValueRule(objectUnderConfiguration, value, propertyName);
        }
        OngoingConfiguration.clear();
    }

    private void addValueRule(Object objectUnderConfiguration, Object value, String propertyName) {
        List<GenerationRule> configuredRules = getOngoingGenerationConfiguration(objectUnderConfiguration);
        GenerationRule generationRule = Rules.fromList(value);
        generationRule.setForProperty(getRealObjectClass(objectUnderConfiguration), propertyName);
        configuredRules.add(generationRule);
    }

    private void addRuleFromConfigurationObject(Object objectUnderConfiguration, String propertyName, List<GenerationRule> rules) {
        GenerationRule specificConfigurationRule = Rules.specificRules(getRules(OngoingConfiguration.plannedConfigurationObject));
        specificConfigurationRule.setScope(RuleScope.PROPERTY);
        specificConfigurationRule.setForProperty(getRealObjectClass(objectUnderConfiguration), propertyName);
        rules.add(specificConfigurationRule);
    }

    private boolean needToUseConfigurationObject(Object objectUnderConfiguration) {
        return objectUnderConfiguration instanceof InterceptedBySetterConfigurator
                && OngoingConfiguration.plannedConfigurationObject != null
                && OngoingConfiguration.plannedConfigurationObject != objectUnderConfiguration;
    }

    private List<GenerationRule> getOngoingGenerationConfiguration(Object objectUnderConfiguration) {
        int identityOfObject = System.identityHashCode(objectUnderConfiguration);
        return contextForObjects.get(identityOfObject);
    }

    private void addPlannedRuleForProperty(Object objectUnderConfiguration, String propertyName, List<GenerationRule> rules) {
        GenerationRule plannedRule = OngoingConfiguration.plannedRule;
        plannedRule.setForProperty(getRealObjectClass(objectUnderConfiguration), propertyName);
        rules.add(plannedRule);
    }

    /**
     *
     * @param forObject not null object for which to return GenerationConfiguration
     * @return GenerationConfiguration for the object or null if no such object was configured by this handler.
     */
    public List<GenerationRule> getRules(Object forObject){
        Assert.notNull(forObject);
        return contextForObjects.get(System.identityHashCode(forObject));
    }
}
