package org.objectg.conf;

import org.objectg.gen.cycle.BackReferenceCycleStrategy;
import org.objectg.gen.cycle.GoDeeperCycleStrategy;
import org.objectg.gen.cycle.NullValueCycleStrategy;

/**
 * User: __nocach
 * Date: 11.11.12
 */
public class CycleBuilder {
	private ConfigurationBuilder configurationBuilder;

	public CycleBuilder(ConfigurationBuilder configurationBuilder){
		this.configurationBuilder = configurationBuilder;
	}

	public ConfigurationBuilder backReference() {
		return configurationBuilder.setCycleStrategy(new BackReferenceCycleStrategy());
	}

	public ConfigurationBuilder setNull() {
		return configurationBuilder.setCycleStrategy(new NullValueCycleStrategy());
	}

	/**
	 *
	 * @param depth how many NEW objects (not including original object) will be generated in cycle.
	 *                 After "depth" count of objects was generated null will be returned
	 * @return
	 */
	public ConfigurationBuilder goDeeper(final int depth) {
		return configurationBuilder.setCycleStrategy(new GoDeeperCycleStrategy(depth));
	}
}
