package org.objectg.gen.cycle;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;

/**
 * <p>
 * will generate additional objects on cycle. calling goDeeper(1) will result in: if properties Person.address
 * and Address.person exist then 3 objects will be generated Person, Person.address and Person.address.person.
 * </p>
 * User: __nocach
 * Date: 11.11.12
 */
public class GoDeeperCycleStrategy implements CycleStrategy {
	private int depth;

	/**
	 *
	 * @param depth how many NEW objects (not including original object) will be generated until null will be return
	 */
	public GoDeeperCycleStrategy(int depth){
		this.depth = depth;
	}
	@Override
	public Object generateForCycle(final GenerationConfiguration configuration, final GenerationContext context) {
		if (context.getCycleDepth() <= depth){
			throw new MustIgnoreCycleException();
		}
		return null;
	}

	@Override
	public boolean shouldGenerateValueInCollection() {
		return true;
	}
}
