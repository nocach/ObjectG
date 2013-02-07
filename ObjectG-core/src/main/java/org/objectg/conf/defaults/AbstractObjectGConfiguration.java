package org.objectg.conf.defaults;

import java.util.Collection;
import java.util.List;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.conf.prototype.PrototypeCreator;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.PostProcessor;
import org.objectg.gen.RuleScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *     To define global configuration create class with exact name ObjectGConfiguration in package
 *     "org.objectg.conf". ObjectGConfiguration should extend AbstractObjectGConfiguration and define
 *     any configuration that should be globally applied.
 * </p>
 * <p>
 *     This is useful to redefine default behaviour (e.g. you can change default number of objects in collection,
 *     {@link org.objectg.gen.cycle.CycleStrategy} or {@link org.objectg.gen.access.AccessStrategy})
 * </p>
 * <p>
 *     You can also define rules for objects that are widely used in your test cases. E.g. you can define
 *     default way of generating some class (e.g. you can define prototype for Person.class that will be used
 *     as a default way how to generate objects of that type)
 * </p>
 * <p>
 *     Configuration set by this way can be overriden by either local configuration
 *     using {@link org.objectg.ObjectG#configLocal(org.objectg.conf.GenerationConfiguration)}
 *     or any test-specific configuration.
 * </p>
 * <p>
 * User: __nocach
 * Date: 8.11.12
 * </p>
 */
public abstract class AbstractObjectGConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(AbstractObjectGConfiguration.class);

	/**
	 * <p>
	 * override this to return {@link GenerationConfiguration} that will be used as a default one.
	 * </p>
	 * <p>Returned  configuration will be applied for all generations, even for those that don't require you to pass
	 * configuration object (e.g. {@link org.objectg.ObjectG#unique(Class)} will use this configuration).
	 * </p>
	 * <p>
	 *     Any attributes of configuration that are redefined in test cases (either through supplying
	 *     {@link GenerationConfiguration} to generating methods or by using
	 *     {@link org.objectg.ObjectG#configLocal(org.objectg.conf.GenerationConfiguration)}) will override
	 *     values that are present in such global configuration.
	 * </p>
	 * @return {@link GenerationConfiguration} to be used as a global default.
	 */
	protected GenerationConfiguration getConfiguration(){
		return null;
	}

	/**
	 *
	 * @return all prototypes that should be used when generating objects. Prototype can still be overriden
	 * 			by supplying additional prototypes to generate methods in {@link org.objectg.ObjectG} or
	 * 			by setting prototype as a property on another prototype (e.g. personPrototype.setAddress(addressPrototype))
	 */
	protected Collection<? extends Object> getPrototypes(){
		return null;
	}

	/**
	 *
	 * @return rules that should be used globally. Rules returned can be overriden by adding rules through
	 * prototype (see {@link org.objectg.ObjectG#prototype(Class)} or by direct adding to configuration
	 * (see {@link GenerationConfiguration#addRule(org.objectg.gen.GenerationRule)}
	 * and {@link org.objectg.gen.rule.Rules}).
	 */
	protected Collection<? extends GenerationRule> getRules(){
		return null;
	}

	/**
	 *
	 * @return post processors that will be used after all generations, see {@link PostProcessor} for more info.
	 */
	protected Collection<? extends PostProcessor> getPostProcessors(){
		return null;
	}

	/**
	 *
	 * @param configuration not null
	 * @return
	 */
	public final GenerationConfiguration merge(PrototypeCreator prototypeCreator, final GenerationConfiguration configuration) {
		logger.debug("merging default configuration into supplied configuration="+configuration);
		final Collection<? extends Object> prototypes = getPrototypes();
		if (prototypes != null){
			final List<GenerationRule> defaultRulesFromPrototypes = prototypeCreator
					.getRulesFromPrototypes(prototypes.toArray());
			addGlobalRules(configuration, defaultRulesFromPrototypes);
		}
		final GenerationConfiguration defaultConfiguration = getConfiguration();
		if (defaultConfiguration != null){
			defaultConfiguration.setLevel(GenerationConfiguration.LEVEL.GLOBAL);
			configuration.merge(defaultConfiguration);
		}
		final Collection<? extends GenerationRule> rules = getRules();
		if (rules != null){
			addGlobalRules(configuration, rules);
		}
		final Collection<? extends PostProcessor> postProcessors = getPostProcessors();
		if (postProcessors != null){
			configuration.addAllPostProcessors(postProcessors);
		}
		logger.info("configuration after merging default settings is " +configuration);
		return configuration;
	}

	private void addGlobalRules(final GenerationConfiguration configuration,
			final Collection<? extends GenerationRule> rules) {
		for (GenerationRule each : rules){
			each.setScope(RuleScope.GLOBAL);
		}
		configuration.addAllRules(rules);
	}
}
