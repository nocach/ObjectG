package org.objectg.gen.rule;

import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;
import org.objectg.conf.OngoingRules;
import org.objectg.fixtures.PrimitivesClass;

/**
 * User: __nocach
 * Date: 29.1.13
 */
public class ValueRuleTest  extends BaseObjectGTest{

	@Test
	public void canValuePrimitives(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setByteField((byte) OngoingRules.value(1));
		prototype.setCharField(OngoingRules.value('a'));
		prototype.setShortField((short) OngoingRules.value(1));
		prototype.setIntField(OngoingRules.value(1));
		prototype.setLongField(OngoingRules.value(1));
		prototype.setFloatField(OngoingRules.value(1));
		prototype.setBooleanField(OngoingRules.value(false));
	}
}
