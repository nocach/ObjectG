package org.objectg.fixtures.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * User: __nocach
 * Date: 29.9.12
 */
@Entity
public class Departure {
	@Id
	@GeneratedValue
	private Long id;
	@OneToOne
	private Tour tour;
	private Date departureDate;
	@OneToOne
	private GuideAssignment staff;
	@OneToMany
	private List<Reservation> reservations;
	@OneToMany
	private List<Customer> servedCustomers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tour getTour() {
		return tour;
	}

	public void setTour(Tour tour) {
		this.tour = tour;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public GuideAssignment getStaff() {
		return staff;
	}

	public void setStaff(GuideAssignment staff) {
		this.staff = staff;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(final List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public List<Customer> getServedCustomers() {
		return servedCustomers;
	}

	public void setServedCustomers(final List<Customer> servedCustomers) {
		this.servedCustomers = servedCustomers;
	}
}
