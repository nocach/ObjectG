package org.objectg.gen.rule;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;
import org.objectg.conf.OngoingRules;
import org.objectg.fixtures.PrimitivesClass;
import org.objectg.integration.ObjectGTestRule;

/**
 * User: __nocach
 * Date: 22.1.13
 */
public class RangeRuleTest extends BaseObjectGTest {
	@Test
	public void intRange(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setIntField(OngoingRules.range(0, 3));

		final PrimitivesClass unique1 = ObjectG.unique(prototype);
		final PrimitivesClass unique2 = ObjectG.unique(prototype);
		final PrimitivesClass unique3 = ObjectG.unique(prototype);
		final PrimitivesClass unique4 = ObjectG.unique(prototype);
		final PrimitivesClass unique5 = ObjectG.unique(prototype);


		assertEquals("range should start from 'from' value", 0, unique1.getIntField());
		assertEquals("range should increment by 1", 1, unique2.getIntField());
		assertEquals("range should increment by 1", 2, unique3.getIntField());
		assertEquals("range should generate till value equal to 'to'", 3, unique4.getIntField());
		assertEquals("range values should cycle", 0, unique5.getIntField());
	}

}
