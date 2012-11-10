package org.objectg.conf;

import org.objectg.ObjectG;
import org.objectg.conf.defaults.AbstractObjectGConfiguration;

/**
 * User: __nocach
 * Date: 10.11.12
 */
public class ObjectGConfiguration extends AbstractObjectGConfiguration {
	@Override
	protected GenerationConfiguration getConfiguration() {
		return ObjectG.config().setObjectsInCollection(2).done();
	}
}
