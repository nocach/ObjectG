package org.objectg.fixtures.domain;

import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * User: __nocach
 * Date: 5.11.12
 */
@MappedSuperclass
public abstract class Price {
	@Id
	@GeneratedValue
	private Long id;
	public abstract BigDecimal getValue();
}
