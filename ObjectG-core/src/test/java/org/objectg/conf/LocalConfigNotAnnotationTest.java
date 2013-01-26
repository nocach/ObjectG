package org.objectg.conf;

import org.junit.Before;
import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;
import org.objectg.fixtures.domain.Tour;

import static junit.framework.Assert.assertEquals;

/**
 * User: __nocach
 * Date: 17.11.12
 */
public class LocalConfigNotAnnotationTest extends BaseObjectGTest {
	@Before
	public void setup(){
		ObjectG.setupConfig(ObjectG.config().setObjectsInCollection(3));
	}

	@Test
	public void localConfigurationIsApplied(){
		final Tour unique = ObjectG.unique(Tour.class);

		assertEquals("local configuration set objectsCount to 3", 3, unique.getStops().size());
	}
}
