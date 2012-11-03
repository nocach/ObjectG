package org.objectg.conf.localconf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.conf.exception.ConfigurationException;

/**
 * User: __nocach
 * Date: 3.11.12
 */
public class ConfigurationManager {

	private int stackTraceOffset;
	private ConfigurationDiscover configurationDiscover;
	private Map<Class, GenerationConfiguration> classToConfiguration = new ConcurrentHashMap<Class, GenerationConfiguration>();
	/**
	 * @param stackTraceOffset offset defining how much stack trace elements should be
	 *                         skipped to get to the Class for which local configuration
	 *                         should be applied
	 */
	public ConfigurationManager(int stackTraceOffset){
		this.stackTraceOffset = stackTraceOffset;
		configurationDiscover = new ConfigurationDiscover();
	}
	/**
	 *
	 * @param objectWithConfiguration object that have local configuration
	 */
	public void register(Object objectWithConfiguration){
		final GenerationConfiguration generationConfiguration = configurationDiscover.get(objectWithConfiguration);
		Class<?> classWithLocalConfiguration = getCallingClass();
		classToConfiguration.put(classWithLocalConfiguration, generationConfiguration);
	}

	private Class<?> getCallingClass() {
		try {
			final StackTraceElement calledFromObjectStackTrace = Thread.currentThread().getStackTrace()[stackTraceOffset];
			return Class.forName(calledFromObjectStackTrace.getClassName());
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException("could not determine class from which setupConfig was called", e);
		}
	}

	private GenerationConfiguration get(){
		return classToConfiguration.get(getCallingClass());
	}

	public GenerationConfiguration getFinalConfiguration(final GenerationConfiguration userConfiguration) {
		GenerationConfiguration localConfiguration = get();
		if (localConfiguration == null){
			localConfiguration = new GenerationConfiguration();
		}
		//local chagnes need to be cloned, so further getFinalCOnfiguration returns originally set localConfiguration
		GenerationConfiguration finalConfiguration = localConfiguration.clone();
		//override all changes that were set by user
		finalConfiguration.merge(userConfiguration);
		//init the defaults
		finalConfiguration.init();
		return finalConfiguration;
	}
}
