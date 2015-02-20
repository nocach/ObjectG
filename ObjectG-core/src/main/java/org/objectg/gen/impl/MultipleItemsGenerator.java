package org.objectg.gen.impl;

import org.objectg.conf.GenerationConfiguration;
import org.objectg.gen.GenerationContext;
import org.objectg.gen.Generator;

/**
 * User: kaloshin
 * Date: 11.3.13
 */
abstract class MultipleItemsGenerator extends Generator {
	protected boolean shouldAddObjectForContext(GenerationConfiguration configuration
			, GenerationContext contextForCollectionsObject) {
		if (!configuration.shouldGenerate(contextForCollectionsObject)) return false;
		if (contextForCollectionsObject.isCycle()
				&& configuration.getCycleStrategy().shouldGenerateValueInCollection()) return true;
		if (!contextForCollectionsObject.isCycle()) return true;
		return false;
	}
}
