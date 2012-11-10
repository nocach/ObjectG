package org.objectg.conf.defaults;

/**
 * User: __nocach
 * Date: 8.11.12
 */
public abstract class DefaultConfigurationProviderHolder {
	private static DefaultConfigurationProvider provider = new ClasspathDefaultConfigurationProvider();

	public static DefaultConfigurationProvider get(){
		return provider;
	}
	public static void set(DefaultConfigurationProvider provider){
		DefaultConfigurationProviderHolder.provider = provider;
	}
}
