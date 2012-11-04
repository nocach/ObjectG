package org.objectg.conf.localconf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.conf.exception.ConfigurationException;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 3.11.12
 */
public class ConfigurationManager {

	private static final int INNER_STACK_TRACE_OFFSET = 3;

	private int stackTraceOffset;
	private Class<?> mainApiClass;
	private ConfigurationDiscover configurationDiscover;
	private Map<Class, GenerationConfiguration> classToConfiguration = new ConcurrentHashMap<Class, GenerationConfiguration>();
	/**
	 * @param stackTraceOffset offset defining how much stack trace elements should be
	 *                         skipped to get to the Class for which local configuration
	 *                         should be applied
	 * @param mainApiClass class from which this ConfigurationManager will be used
	 */
	public ConfigurationManager(final int stackTraceOffset, final Class<?> mainApiClass) {
		Assert.notNull(mainApiClass, "mainApiClass");
		this.stackTraceOffset = stackTraceOffset;
		this.mainApiClass = mainApiClass;
		configurationDiscover = new ConfigurationDiscover();
	}

	/**
	 *
	 * @param objectWithConfiguration object that have local configuration
	 */
	public void register(Object objectWithConfiguration){
		final GenerationConfiguration generationConfiguration = configurationDiscover.get(objectWithConfiguration);
		if (generationConfiguration == null) return;
		Class<?> classWithLocalConfiguration = getCallingClass();
		classToConfiguration.put(classWithLocalConfiguration, generationConfiguration);
	}

	/**
	 * walks through stack trace looking for mainApiClass. Once found it will wait until next class that
	 * differs from mainApiClass. This next class is considered to be caller on the mainApiClass.
	 * This guards from false-positive found result, because of inner calls within mainApiClass such as
	 * ObjectG.unique(class) calls return ObjectG.unique(class, null);
	 * Nevertheless this is still fragile.
	 * @return found calling class
	 */
	private Class<?> getCallingClass() {
		try {
			boolean useNextClass = false;
			for (StackTraceElement each : Thread.currentThread().getStackTrace()){
				if (useNextClass && !each.getClassName().equals(mainApiClass.getName())){
					return Class.forName(each.getClassName());
				}
				if (each.getClassName().equals(mainApiClass.getName())) useNextClass = true;
			}
			throw new ConfigurationException("could not determine called class from stack trace ");
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
		//local changes need to be cloned, so further invocation of getFinalConfiguration
		// returns originally set localConfiguration
		GenerationConfiguration finalConfiguration = localConfiguration.clone();
		//override all changes that were set by user
		finalConfiguration.merge(userConfiguration);
		//init the defaults
		finalConfiguration.init();
		return finalConfiguration;
	}
}
