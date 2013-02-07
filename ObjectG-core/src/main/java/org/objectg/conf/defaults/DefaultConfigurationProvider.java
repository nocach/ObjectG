package org.objectg.conf.defaults;

/**
 * <p>
 *     Implementations provide ways on how to find instance of {@link AbstractObjectGConfiguration}
 * </p>
 * <p>
 * User: __nocach
 * Date: 8.11.12
 * </p>
 */
public interface DefaultConfigurationProvider {
	public AbstractObjectGConfiguration getDefaultConfiguration();
}
