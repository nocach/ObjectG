package org.objectg.conf.prototype;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objectg.conf.OngoingConfiguration;
import org.objectg.conf.exception.ConfigurationException;
import org.objectg.conf.exception.PrototypeMisuseException;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.RuleScope;
import org.objectg.gen.rule.Rules;
import org.springframework.util.Assert;

/**
 * <p>
 *     PrototypeSetterHandler that manages list of rules for each property and objects.
 * </p>
 * <p>
 * User: __nocach
 * Date: 16.9.12
 * </p>
 */
public class PrototypeSetterHandler {
    //TODO: need to make clean up of the used rules
	//TODO: use weak reference on to prototypes
    //e.g before each test or testcase
    /**
     * Integer = System.identityHashCode(prototyp)
     * LinkedList<GenerationRule> - rules for the prototype
     */
    private Map<Integer, LinkedList<GenerationRule>> rulesForPrototype =
            new ConcurrentHashMap<Integer, LinkedList<GenerationRule>>();
    /**
     * Integer = System.identityHashCode(prototyp)
     * String = name of the property for which to return nested prototype
     * Object = prototype for the property
     */
    private Map<Integer, Map<String, Object>> prototypesForPrototypeProperties
            = new ConcurrentHashMap<Integer, Map<String, Object>>();
    /**
     * Integer = System.identityHashCode(prototyp)
     * Map<String, List<GenerationRule>>
     *     String = name of the property
     *     List<GenerationRule> = rules for the property
     */
    private Map<Integer, Map<String, List<GenerationRule>>> rulesForPrototypeProperties =
            new ConcurrentHashMap<Integer, Map<String, List<GenerationRule>>>();
    private PrototypeCreator prototypeCreator;

    public PrototypeSetterHandler(PrototypeCreator prototypeCreator) {
        this.prototypeCreator = prototypeCreator;
    }

    /**
    * called when new protype is created
    * @param prototype not null prototype
    */
    public void onInit(Object prototype) {
        int identityOfObject = getObjectIdentity(prototype);
        if (rulesForPrototype.get(identityOfObject) == null){
            //we are extracting superclass, because PrototypeCreator is creating new subclass in runtime
            rulesForPrototype.put(identityOfObject, new LinkedList<GenerationRule>());
        }
    }

    /**
     * called when setter was called for configuration.
     * @param prototype on which setter is called
     * @param value value with which setter was called
     * @param propertyName property of the setter
     */
    public void onSetter(Object prototype, Object value, String propertyName) {
        List<GenerationRule> configuredRules = getOngoingRules(prototype);
        if (OngoingConfiguration.getPlannedRule() != null) {
            addPlannedRuleForProperty(prototype, propertyName, configuredRules);
        }
        else if (needToUseConfigurationObject(prototype)){
            setPrototypeForProperty(prototype, propertyName, value);
        }
        else {
            addValueRule(prototype, value, propertyName);
        }
        OngoingConfiguration.clear();
    }

    private void setPrototypeForProperty(Object prototypeParent, String propertyName, Object prototypeForProperty) {
        setRuleListForProperty(prototypeParent, propertyName, OngoingConfiguration.getPlannedPrototype());
        Map<String, Object> prototypeProperties = getPrototypesForProperties(prototypeParent);
        prototypeProperties.put(propertyName, prototypeForProperty);
    }

    private Map<String, Object> getPrototypesForProperties(Object prototype) {
        Integer prototypeHashCode = getObjectIdentity(prototype);
        if (prototypesForPrototypeProperties.get(prototypeHashCode) == null){
            prototypesForPrototypeProperties.put(prototypeHashCode, new HashMap<String, Object>());
        }
        return prototypesForPrototypeProperties.get(prototypeHashCode);
    }

    private int getObjectIdentity(Object prototype) {
        return System.identityHashCode(prototype);
    }

    private void addValueRule(Object prototype, Object value, String propertyName) {
        List<GenerationRule> configuredRules = getOngoingRules(prototype);
        GenerationRule generationRule = Rules.fromList(value);
        generationRule.setForProperty(prototypeCreator.getRealObjectClass(prototype), propertyName);
        configuredRules.add(generationRule);
    }

    public void setRuleListForProperty(Object prototypeParent, String propertyName , Object prototypeForProperty) {
        if (rulesForPrototypeProperties.get(getObjectIdentity(prototypeParent))== null){
            rulesForPrototypeProperties.put(getObjectIdentity(prototypeParent)
                    , new HashMap<String, List<GenerationRule>>());
        }
        Map<String, List<GenerationRule>> propertyRules = rulesForPrototypeProperties.get(getObjectIdentity(prototypeParent));
        propertyRules.put(propertyName, getRules(prototypeForProperty));
    }

    private boolean needToUseConfigurationObject(Object prototype) {
        return prototype instanceof InterceptedByPrototypeCreator
                && OngoingConfiguration.getPlannedPrototype() != null
                && OngoingConfiguration.getPlannedPrototype() != prototype;
    }

    private List<GenerationRule> getOngoingRules(Object prototype) {
        int identityOfObject = getObjectIdentity(prototype);
        return rulesForPrototype.get(identityOfObject);
    }

    private void addPlannedRuleForProperty(Object prototype, String propertyName, List<GenerationRule> rulesForPrototype) {
        GenerationRule plannedRule = OngoingConfiguration.getPlannedRule();
        plannedRule.setForProperty(prototypeCreator.getRealObjectClass(prototype), propertyName);
        rulesForPrototype.add(plannedRule);
    }

    public Object getPrototypeFromSetter(Object prototype, String propertyName){
        if (prototypesForPrototypeProperties.get(getObjectIdentity(prototype)) == null) return null;
        return prototypesForPrototypeProperties.get(getObjectIdentity(prototype)).get(propertyName);
    }

    /**
     *
     * @param forPrototype not null object for which to return rules
     * @return List<GenerationRule> for the prototype or null if no such prototype was managed by this handler
     */
    public List<GenerationRule> getRules(Object forPrototype){
        Assert.notNull(forPrototype);
        LinkedList<GenerationRule> resultRules = rulesForPrototype.get(getObjectIdentity(forPrototype));
        if (rulesForPrototypeProperties.get(getObjectIdentity(forPrototype)) != null){
            addRulesForEachProperty(forPrototype, resultRules);
        }
        return resultRules;
    }

    private void addRulesForEachProperty(Object prototype, LinkedList<GenerationRule> resultRules) {
        Map<String, List<GenerationRule>> rulesForProperties = rulesForPrototypeProperties.get(getObjectIdentity(prototype));
        for (Map.Entry<String, List<GenerationRule>> each : rulesForProperties.entrySet()){
            GenerationRule specificConfigurationRule = Rules.specificRules(each.getValue());
            specificConfigurationRule.setScope(RuleScope.PROPERTY);
            specificConfigurationRule.setForProperty(prototypeCreator.getRealObjectClass(prototype), each.getKey());
            resultRules.add(specificConfigurationRule);
        }
    }

    public Object newPrototypeFromGetter(Object prototypeParent, Class clazz, String propertyName){
        try{
            if (getPrototypeFromSetter(prototypeParent, propertyName) != null){
                return getPrototypeFromSetter(prototypeParent, propertyName);
            }

            Map<String, Object> prototypeProperties = getPrototypesForProperties(prototypeParent);
            if (prototypeProperties.containsKey(propertyName)) return prototypeProperties.get(propertyName);

            Object prototypeForProperty = prototypeCreator.newPrototype(clazz);

            setRuleListForProperty(prototypeParent, propertyName, prototypeForProperty);
            prototypeProperties.put(propertyName, prototypeForProperty);

            OngoingConfiguration.clear();

            return prototypeForProperty;
        }
        catch (ConfigurationException e){
            throw new PrototypeMisuseException("getter on prototype can be used only " +
                    "on properties that can be configurated with prototyping", e);
        }
    }
}
