package org.objectg.gen.rule;

import org.junit.Test;
import org.objectg.ObjectG;
import org.objectg.conf.OngoingRules;
import org.objectg.fixtures.domain.FixedPrice;

import static junit.framework.Assert.assertEquals;

/**
 * User: __nocach
 * Date: 5.11.12
 */
public class RulesTest {
	@Test
	public void setGeneratedObjectRule(){
		//TODO: add comment why or when to use this?
		ConcreteProduct prototype = ObjectG.prototype(ConcreteProduct.class);
		prototype.getPrice2Product().setProduct(OngoingRules.<Product>generatedObject());

		final ConcreteProduct unique = ObjectG.unique(prototype);
		assertEquals(unique, unique.getPrice2Product().getProduct());
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
