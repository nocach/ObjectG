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

	CycleBuilder(ConfigurationBuilder configurationBuilder){
		this.configurationBuilder = configurationBuilder;
	}

	/**
	 * use object that caused cycle as a value. E.g. exists Person.address and Address.person,
	 * when generating object of type Person 2 objects will be generated Person and Person.address,
	 * Person.address.person will be firstly generated person.
	 */
	public ConfigurationBuilder backReference() {
		return configurationBuilder.setCycleStrategy(new BackReferenceCycleStrategy());
	}

	/**
	 * set null on cycle. E.g. exists Person.address and Address.person,
	 * when generating object of type Person 2 objects will be generated Person and Person.address,
	 * Person.address.person will be null.
	 */
	public ConfigurationBuilder setNull() {
		return configurationBuilder.setCycleStrategy(new NullValueCycleStrategy());
	}

	/**
	 * will generate additional objects on cycle. calling goDeeper(1) will result in: if properties Person.address
	 * and Address.person exist then 3 objects will be generated Person, Person.address and Person.address.person.
	 *
	 * @param depth how many NEW objects (not including original object) will be generated in cycle.
	 *                 After "depth" count of objects was generated null will be returned
	 * @return
	 */
	public ConfigurationBuilder goDeeper(final int depth) {
		return configurationBuilder.setCycleStrategy(new GoDeeperCycleStrategy(depth));
	}
}
