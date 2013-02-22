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
public class ValueRuleTest  extends BaseObjectGTest{

	@Test
	public void canValuePrimitives(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setByteField((byte) PrototypeRules.value(1));
		prototype.setCharField(PrototypeRules.value('a'));
		prototype.setShortField((short) PrototypeRules.value(1));
		prototype.setIntField(PrototypeRules.value(1));
		prototype.setLongField(PrototypeRules.value(1));
		prototype.setFloatField(PrototypeRules.value(1));
		prototype.setBooleanField(PrototypeRules.value(false));
	}
}
