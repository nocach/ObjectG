package org.objectg.gen.rule;

import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;
import org.objectg.conf.PrototypeRules;
import org.objectg.fixtures.PrimitivesClass;

/**
 * User: __nocach
 * Date: 29.1.13
 */
public class ValueRuleTest extends BaseObjectGTest {

	@Test
	public void canValuePrimitives() {
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setByteField(PrototypeRules.valueByte((byte) 1));
		prototype.setCharField(PrototypeRules.valueChar('a'));
		prototype.setShortField(PrototypeRules.valueShort((short) 1));
		prototype.setIntField(PrototypeRules.valueInt(1));
		prototype.setLongField(PrototypeRules.valueLong(1));
		prototype.setFloatField(PrototypeRules.valueFloat(1));
		prototype.setBooleanField(PrototypeRules.valueBoolean(false));
	}
}
