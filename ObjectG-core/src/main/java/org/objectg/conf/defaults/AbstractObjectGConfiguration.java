package org.objectg.conf.defaults;

import java.util.Collection;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.conf.prototype.PrototypeCreator;
import org.objectg.gen.GenerationRule;
import org.objectg.gen.PostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *     To define global configuration create class with exact name ObjectGConfiguration in package
 *     "org.objectg.conf". ObjectGConfiguration should extend AbstractObjectGConfiguration and define
 *     any configuration that should be globally applied.
 * </p>
 * <p>
 *     Configuration set by this way can be overriden by either local configuration
 *     using {@link org.objectg.conf.local.ConfigurationProvider} or any test-specific configuration.
 * </p>
 * <p>
 * User: __nocach
 * Date: 8.11.12
 * </p>
 */
public abstract class AbstractObjectGConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(AbstractObjectGConfiguration.class);

	protected GenerationConfiguration getConfiguration(){
		return null;
	}

	protected Collection<Object> getPrototypes(){
		return null;
	}

	protected Collection<? extends GenerationRule> getRules(){
		return null;
	}

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
		final Collection<Object> prototypes = getPrototypes();
		if (prototypes != null){
			configuration.addAllRules(prototypeCreator.getRulesFromPrototypes(prototypes.toArray()));
		}
		final GenerationConfiguration defaultConfiguration = getConfiguration();
		if (defaultConfiguration != null){
			defaultConfiguration.setLevel(GenerationConfiguration.LEVEL.GLOBAL);
			configuration.merge(defaultConfiguration);
		}
		final Collection<? extends GenerationRule> rules = getRules();
		if (rules != null){
			configuration.addAllRules(rules);
		}
		final Collection<? extends PostProcessor> postProcessors = getPostProcessors();
		if (postProcessors != null){
			configuration.addAllPostProcessors(postProcessors);
		}
		logger.info("configuration after merging default settings is " +configuration);
		return configuration;
	}
}
