package cz.nocach.masaryk.objectg.fixtures;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * User: __nocach
 * Date: 29.9.12
 */
@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Person person;
    private String notes;
    @OneToMany(mappedBy = "customer")
    private Set<Reservation> reservations;
    @OneToMany(mappedBy = "customer" )
    private List<Preference> preferences;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
