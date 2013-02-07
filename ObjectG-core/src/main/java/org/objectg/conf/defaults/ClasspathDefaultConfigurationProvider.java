package org.objectg.conf.defaults;

import org.objectg.conf.exception.ConfigurationException;

/**
 * <p>
 *     Gets {@link AbstractObjectGConfiguration} by trying to instantiate class with name
 *     {@code org.objectg.conf.ObjectGConfiguration}
 * </p>
 * <p>
 * User: __nocach
 * Date: 8.11.12
 * </p>
 */
public class ClasspathDefaultConfigurationProvider implements DefaultConfigurationProvider {
	@Override
	public AbstractObjectGConfiguration getDefaultConfiguration() {
		try{
			final Class<?> userConfClass = Class.forName("org.objectg.conf.ObjectGConfiguration");
			if (!AbstractObjectGConfiguration.class.isAssignableFrom(userConfClass)){
				throw new ConfigurationException("ObjectGConfiguration must extend AbstractObjectGConfiguration");
			}
			return (AbstractObjectGConfiguration)userConfClass.newInstance();
		}
		catch (ClassNotFoundException e) {
			return null;
		} catch (InstantiationException e) {
			throw new ConfigurationException("ObjectGConfiguration should have no-arg constructor", e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("ObjectGConfiguration should be visible", e);
		}

	}
}
