package org.objectg.gen.cycle;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;

/**
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
