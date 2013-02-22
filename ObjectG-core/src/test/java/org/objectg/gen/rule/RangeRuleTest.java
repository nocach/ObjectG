package org.objectg.gen.rule;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;
import org.objectg.conf.PrototypeRules;
import org.objectg.fixtures.PrimitivesClass;
import org.objectg.gen.rule.range.BigDecimalRange;
import org.objectg.gen.rule.range.DateRange;
import org.objectg.gen.rule.range.DoubleRange;
import org.objectg.gen.rule.range.FloatRange;
import org.objectg.gen.rule.range.IntRange;
import org.objectg.gen.rule.range.LongRange;

import static org.objectg.gen.rule.range.IntRange.create;

/**
 * User: __nocach
 * Date: 22.1.13
 */
public class RangeRuleTest extends BaseObjectGTest {

	@Test
	public void intRangeBase(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setIntField(PrototypeRules.rangeInt(create(0, 3)));

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

	@Test
	public void intRangeWithStep(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setIntField(PrototypeRules.rangeInt(create(0, 3).step(2)));

		final PrimitivesClass unique1 = ObjectG.unique(prototype);
		final PrimitivesClass unique2 = ObjectG.unique(prototype);
		final PrimitivesClass unique3 = ObjectG.unique(prototype);

		assertEquals(0, unique1.getIntField());
		assertEquals(2, unique2.getIntField());
		assertEquals(0, unique3.getIntField());
	}

	@Test
	public void doubleRange(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setDoubleField(PrototypeRules.rangeDouble(DoubleRange.create(0., 2.)));

		final PrimitivesClass unique1 = ObjectG.unique(prototype);
		final PrimitivesClass unique2 = ObjectG.unique(prototype);
		final PrimitivesClass unique3 = ObjectG.unique(prototype);
		final PrimitivesClass unique4 = ObjectG.unique(prototype);

		assertEquals("range should start from 'from' value", 0, unique1.getDoubleField(), 0.01);
		assertEquals("range should increment by 1", 1, unique2.getDoubleField(), 0.01);
		assertEquals("range should generate till value equal to 'to'", 2, unique3.getDoubleField(), 0.01);
		assertEquals("range values should cycle", 0, unique4.getDoubleField(), 0.01);
	}

	@Test
	public void doubleRangeWithStep(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setDoubleField(PrototypeRules.rangeDouble(DoubleRange.create(0., 0.3).step(0.2)));

		final PrimitivesClass unique1 = ObjectG.unique(prototype);
		final PrimitivesClass unique2 = ObjectG.unique(prototype);
		final PrimitivesClass unique3 = ObjectG.unique(prototype);

		assertEquals(0, unique1.getDoubleField(), 0.001);
		assertEquals(0.2, unique2.getDoubleField(), 0.001);
		assertEquals(0, unique3.getDoubleField(), 0.001);
	}

	@Test
	public void longRange(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setLongField(PrototypeRules.rangeLong(LongRange.create(2, 3)));

		final PrimitivesClass unique1 = ObjectG.unique(prototype);
		final PrimitivesClass unique2 = ObjectG.unique(prototype);
		final PrimitivesClass unique3 = ObjectG.unique(prototype);

		assertEquals(2, unique1.getLongField());
		assertEquals(3, unique2.getLongField());
		assertEquals(2, unique3.getLongField());
	}

	@Test
	public void floatRange(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setFloatField(PrototypeRules.rangeFloat(FloatRange.create(1.f, 1.1f).step(0.1f)));

		final PrimitivesClass unique1 = ObjectG.unique(prototype);
		final PrimitivesClass unique2 = ObjectG.unique(prototype);
		final PrimitivesClass unique3 = ObjectG.unique(prototype);

		assertEquals(1.f, unique1.getFloatField(), 0.0001f);
		assertEquals(1.1f, unique2.getFloatField(), 0.0001f);
		assertEquals(1f, unique3.getFloatField(), 0.0001f);
	}

	@Test
	public void bigDecimalRange(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setBigDecimal(PrototypeRules
				.range(BigDecimalRange.create(new BigDecimal(1), new BigDecimal(2)).step(BigDecimal.ONE)));

		final PrimitivesClass unique1 = ObjectG.unique(prototype);
		final PrimitivesClass unique2 = ObjectG.unique(prototype);
		final PrimitivesClass unique3 = ObjectG.unique(prototype);

		assertTrue(unique1.getBigDecimal().compareTo(new BigDecimal(1)) == 0);
		assertTrue(unique2.getBigDecimal().compareTo(new BigDecimal(2)) == 0);
		assertTrue(unique3.getBigDecimal().compareTo(new BigDecimal(1)) == 0);
	}

	@Test
	public void dateRange(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		final Date from = new Date(1990, 0, 1);
		final Date to = new Date(1990, 0, 3);
		prototype.setDate(PrototypeRules.range(DateRange.create(from, to)));

		final PrimitivesClass unique1 = ObjectG.unique(prototype);
		final PrimitivesClass unique2 = ObjectG.unique(prototype);
		final PrimitivesClass unique3 = ObjectG.unique(prototype);
		final PrimitivesClass unique4 = ObjectG.unique(prototype);

		assertEquals(from, unique1.getDate());
		assertEquals(new Date(1990, 0, 2), unique2.getDate());
		assertEquals(to, unique3.getDate());
		assertEquals(from, unique4.getDate());
	}

	@Test(expected = IllegalArgumentException.class)
	public void noValidValueRange(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setIntField(PrototypeRules.rangeInt(IntRange.create(5, 0)));
	}

	@Test
	public void canDefineReverseIntRange(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setIntField(PrototypeRules.rangeInt(IntRange.createReverse(1, 0)));

		final PrimitivesClass unique1 = ObjectG.unique(prototype);
		final PrimitivesClass unique2 = ObjectG.unique(prototype);
		final PrimitivesClass unique3 = ObjectG.unique(prototype);

		assertEquals(1, unique1.getIntField());
		assertEquals(0, unique2.getIntField());
		assertEquals(1, unique3.getIntField());

	}

	@Test
	public void canDefineReverseBigDecimalRange(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setBigDecimal(
				PrototypeRules.range(BigDecimalRange.createReverse(BigDecimal.valueOf(2L), BigDecimal.valueOf(1L))));

		final PrimitivesClass unique1 = ObjectG.unique(prototype);
		final PrimitivesClass unique2 = ObjectG.unique(prototype);
		final PrimitivesClass unique3 = ObjectG.unique(prototype);

		assertTrue(BigDecimal.valueOf(2L).compareTo(unique1.getBigDecimal()) == 0);
		assertTrue(BigDecimal.valueOf(1L).compareTo(unique2.getBigDecimal()) == 0);
		assertTrue(BigDecimal.valueOf(2L).compareTo(unique3.getBigDecimal()) == 0);
	}

	@Test
	public void canDefineReverseDateRange(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		final Date from = new Date(1990, 0, 2);
		final Date to = new Date(1990, 0, 1);
		prototype.setDate(PrototypeRules.range(DateRange.createReverse(from, to)));

		final PrimitivesClass unique1 = ObjectG.unique(prototype);
		final PrimitivesClass unique2 = ObjectG.unique(prototype);
		final PrimitivesClass unique3 = ObjectG.unique(prototype);

		assertEquals(from, unique1.getDate());
		assertEquals(to, unique2.getDate());
		assertEquals(from, unique3.getDate());
	}
}
