package org.objectg.fixtures.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * User: __nocach
 * Date: 17.11.12
 */
@Entity
public class TourSeasonBonus {
	@GeneratedValue
	@Id
	private Long id;
	private String description;
	private Money value;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Money getValue() {
		return value;
	}

	public void setValue(final Money value) {
		this.value = value;
	}
}
