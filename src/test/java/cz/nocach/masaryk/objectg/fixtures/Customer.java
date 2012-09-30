package cz.nocach.masaryk.objectg.fixtures;

import java.util.LinkedList;
import java.util.Set;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class Customer {
    private Person person;
    private String notes;
    private Set<Reservation> reservations;
    private LinkedList<Preference> preferences;

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

    public LinkedList<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(LinkedList<Preference> preferences) {
        this.preferences = preferences;
    }
}
