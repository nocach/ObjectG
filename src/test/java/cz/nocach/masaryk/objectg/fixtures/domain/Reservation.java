package cz.nocach.masaryk.objectg.fixtures.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * User: __nocach
 * Date: 29.9.12
 */
@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Agent agent;
    @ManyToOne
    private Departure departure;
    @ManyToOne
    private Customer customer;
    private Date dateReserved;
    private int numberOfPersons;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Departure getDeparture() {
        return departure;
    }

    public void setDeparture(Departure departure) {
        this.departure = departure;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDateReserved() {
        return dateReserved;
    }

    public void setDateReserved(Date dateReserved) {
        this.dateReserved = dateReserved;
    }

    public int getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(int numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }
}
