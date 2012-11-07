package org.objectg.fixtures.domain;

import java.math.BigDecimal;

/**
 * User: __nocach
 * Date: 5.11.12
 */
public class FixedPrice extends Price {
	private BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public BigDecimal getValue() {
		return amount;
	}
}
