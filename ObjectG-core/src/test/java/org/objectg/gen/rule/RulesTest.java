package org.objectg.gen.rule;

import org.junit.Test;
import org.objectg.BaseObjectGTest;
import org.objectg.ObjectG;
import org.objectg.conf.PrototypeRules;
import org.objectg.fixtures.PrimitivesClass;
import org.objectg.fixtures.domain.FixedPrice;
import org.objectg.fixtures.domain.Tour;
import org.objectg.fixtures.domain.TourSeason;

/**
 * User: __nocach
 * Date: 5.11.12
 */
public class RulesTest extends BaseObjectGTest {
	@Test
	public void setGeneratedObjectRule(){
		//TODO: add comment why or when to use this?
		ConcreteProduct prototype = ObjectG.prototype(ConcreteProduct.class);
		prototype.getPrice2Product().setProduct(PrototypeRules.<Product>generatedObject());

		final ConcreteProduct unique = ObjectG.unique(prototype);
		assertEquals(unique, unique.getPrice2Product().getProduct());
	}

	@Test
	public void setRuleOnPrimitives(){
		final PrimitivesClass prototype = ObjectG.prototype(PrimitivesClass.class);
		prototype.setByteField(PrototypeRules.ruleByte(Rules.value((byte) 1)));
		prototype.setCharField(PrototypeRules.ruleChar(Rules.value('1')));
		prototype.setShortField(PrototypeRules.ruleShort(Rules.value((short) 1)));
		prototype.setIntField(PrototypeRules.ruleInt(Rules.value(1)));
		prototype.setLongField(PrototypeRules.ruleLong(Rules.value(1L)));
		prototype.setFloatField(PrototypeRules.ruleFloat(Rules.value(1f)));
		prototype.setDoubleField(PrototypeRules.ruleDouble(Rules.value(1d)));
		prototype.setBooleanField(PrototypeRules.ruleBoolean(Rules.value(true)));

		final PrimitivesClass unique = ObjectG.unique(prototype);

		assertEquals(1, unique.getByteField());
		assertEquals('1', unique.getCharField());
		assertEquals(1, unique.getShortField());
		assertEquals(1, unique.getIntField());
		assertEquals(1, unique.getLongField());
		assertEquals(1f, unique.getFloatField());
		assertEquals(1d, unique.getDoubleField());
		assertEquals(true, unique.isBooleanField());
	}

	@Test
	public void setRuleOnObject(){
		final Tour prototype = ObjectG.prototype(Tour.class);
		final TourSeason expectedSeason = new TourSeason();
		prototype.setSeason(PrototypeRules.rule(Rules.value(expectedSeason)));

		final Tour unique = ObjectG.unique(prototype);

		assertEquals(expectedSeason, unique.getSeason());
	}

	public static abstract class Product{
		private Price2Product price2Product;

		public Price2Product getPrice2Product() {
			return price2Product;
		}

		public void setPrice2Product(final Price2Product price2Product) {
			this.price2Product = price2Product;
		}
	}

	public static class ConcreteProduct extends Product{
	}

	public static class Price2Product{
		private Product product;
		private FixedPrice price;

		public Product getProduct() {
			return product;
		}

		public void setProduct(final Product product) {
			this.product = product;
		}

		public FixedPrice getPrice() {
			return price;
		}

		public void setPrice(final FixedPrice price) {
			this.price = price;
		}
	}
}
