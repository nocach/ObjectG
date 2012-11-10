package org.objectg.fixtures.domain;

import java.math.BigDecimal;

/**
 * User: __nocach
 * Date: 5.11.12
 */
public class PercentagePrice extends Price {
	private BigDecimal base;
	private BigDecimal rate;

	public BigDecimal getBase() {
		return base;
	}

	public void setBase(final BigDecimal base) {
		this.base = base;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(final BigDecimal rate) {
		this.rate = rate;
	}

	@Override
	public BigDecimal getValue() {
		return base.multiply(rate);
	}
}
