package org.objectg.fixtures;

import org.objectg.fixtures.domain.IPerson;
import org.objectg.fixtures.domain.Tour;

/**
 * User: __nocach
 * Date: 3.11.12
 */
public class ClassWithIPerson {
	private IPerson person;
	private Tour tour;

	public IPerson getPerson() {
		return person;
	}

	public void setPerson(final IPerson person) {
		this.person = person;
	}

	public Tour getTour() {
		return tour;
	}

	public void setTour(final Tour tour) {
		this.tour = tour;
	}
}
