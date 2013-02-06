package org.objectg.conf;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objectg.conf.defaults.AbstractObjectGConfiguration;
import org.objectg.conf.defaults.DefaultConfigurationProviderHolder;
import org.objectg.conf.exception.ConfigurationException;
import org.objectg.conf.prototype.PrototypeCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * User: __nocach
 * Date: 3.11.12
 */
public class ConfigurationManager {
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);

	private Class<?> mainApiClass;
	private Map<Class, GenerationConfiguration> classToConfiguration = new ConcurrentHashMap<Class, GenerationConfiguration>();
	/**
	 * @param mainApiClass class from which this ConfigurationManager will be used
	 */
	public ConfigurationManager(final Class<?> mainApiClass) {
		Assert.notNull(mainApiClass, "mainApiClass");
		this.mainApiClass = mainApiClass;
	}

	/**
	 *
	 * @param configuration configuration to be bind to the call class
	 */
	public void register(GenerationConfiguration configuration){
		bindConfigurationToCallClass(configuration);
	}

	private void bindConfigurationToCallClass(final GenerationConfiguration generationConfiguration) {
		generationConfiguration.setLevel(GenerationConfiguration.LEVEL.LOCAL);
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
			throw new ConfigurationException("could not determine class from which configLocal was called", e);
		}
	}

	private GenerationConfiguration getLocalConf(){
		List<Class> callingClassHierarchy = getCallingClassHierarchy();
		GenerationConfiguration resultConfiguration = getConfigurationForClassHierarchy(callingClassHierarchy);
		return resultConfiguration;
	}

	/**
	 * traverses callingClassHierarchy from the most generic class to most specific. Most specific configuration
	 * overrides more generic configuration
	 * @param callingClassHierarchy
	 * @return
	 */
	private GenerationConfiguration getConfigurationForClassHierarchy(final List<Class> callingClassHierarchy) {
		GenerationConfiguration resultConfiguration = null;
		for (Class each : callingClassHierarchy){
			final GenerationConfiguration classConfig = classToConfiguration.get(each);
			if (classConfig != null){
				if (resultConfiguration != null){
					logger.debug("merging local configuration="+classConfig+" found for class="+each.getName());
					//we WANT to override defined values, because local configurations have same level,
					//but localConfiguration in more specific class (deeper in class hierarchy) is
					//considered to have higher level.
					boolean canOverride = true;
					resultConfiguration.merge(classConfig, canOverride);
				}
				else {
					logger.debug("found local configuration="+classConfig+" for class="+each.getName());
					resultConfiguration = classConfig.clone();
				}
			}
		}
		logger.info("using local configuration="+resultConfiguration);
		return resultConfiguration;
	}

	/**
	 *
	 * @return list of Class in the order from most superclass to the calling class
	 */
	private List<Class> getCallingClassHierarchy() {
		final Class<?> callingClass = getCallingClass();
		List<Class> callingClassHierarchy = new LinkedList<Class>();
		Class current = callingClass;
		while (current != Object.class){
			callingClassHierarchy.add(current);
			current = current.getSuperclass();
		}
		Collections.reverse(callingClassHierarchy);
		return callingClassHierarchy;
	}

	public GenerationConfiguration getFinalConfiguration(PrototypeCreator prototypeCreator
			, GenerationConfiguration userConfiguration) {
		GenerationConfiguration localConfiguration = getLocalConf();
		if (localConfiguration == null){
			localConfiguration = new GenerationConfiguration(GenerationConfiguration.LEVEL.LOCAL);
		}
		GenerationConfiguration finalLocalConfiguration = localConfiguration.clone();
		finalLocalConfiguration = mergeDefaultConfiguration(prototypeCreator, finalLocalConfiguration);
		userConfiguration.merge(finalLocalConfiguration);
		userConfiguration.init();
		return userConfiguration;
	}

	private GenerationConfiguration mergeDefaultConfiguration(PrototypeCreator prototypeCreator, GenerationConfiguration configuration) {
		AbstractObjectGConfiguration defaultConfiguration = DefaultConfigurationProviderHolder.get().getDefaultConfiguration();
		if (defaultConfiguration != null) {
			return defaultConfiguration.merge(prototypeCreator, configuration);
		}
		else {
			logger.debug("no default configuration found");
			return configuration;
		}
	}
}
