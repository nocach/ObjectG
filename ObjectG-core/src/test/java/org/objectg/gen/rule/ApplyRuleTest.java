package org.objectg.gen.rule;

import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;
import org.objectg.gen.GenerationRule;

/**
 * User: __nocach
 * Date: 22.2.13
 */
public class ApplyRuleTest extends BaseObjectGTest{

	@Test
	public void basic(){
		final GenerationRule sequence = Rules.sequence(10, 20);

		final Integer first = ObjectG.applyRule(Integer.class, sequence);
		final Integer second = ObjectG.applyRule(Integer.class, sequence);
		assertEquals((int)first, 10);
		assertEquals((int)second, 20);
	}
}
