package org.objectg.conf;

import org.objectg.gen.access.AccessStrategy;

/**
 * User: __nocach
 * Date: 3.2.13
 */
public class AccessBuilder {
	private ConfigurationBuilder configurationBuilder;

	public AccessBuilder(final ConfigurationBuilder configurationBuilder) {

		this.configurationBuilder = configurationBuilder;
	}

	/**
	 * generate values only for properties that have BOTH setters and getters
	 */
	public ConfigurationBuilder onlyMethods() {
		configurationBuilder.setAccessStrategy(AccessStrategy.ONLY_METHODS);
		return configurationBuilder;
	}

	/**
	 * generate values for ALL fields, regardless if those fields have access methods.
	 */
	public ConfigurationBuilder onlyFields() {
		configurationBuilder.setAccessStrategy(AccessStrategy.ONLY_FIELDS);
		return configurationBuilder;
	}

	/**
	 * when setting and getting property values methods will be used first. If some property is missing
	 * one of access methods, then direct field access will be performed. Properties having no access methods
	 * will be manipulated through fields.
	 */
	public ConfigurationBuilder preferMethods() {
		configurationBuilder.setAccessStrategy(AccessStrategy.PREFER_METHODS);
		return configurationBuilder;
	}
}
