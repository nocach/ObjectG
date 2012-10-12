package cz.nocach.masaryk.objectg.fixtures;

import javax.persistence.*;

/**
 * User: __nocach
 * Date: 29.9.12
 */
@Entity
public class Preference {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Customer customer;
    @OneToOne
    private Destination destination;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
