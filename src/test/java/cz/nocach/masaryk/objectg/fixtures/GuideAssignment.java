package cz.nocach.masaryk.objectg.fixtures;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class GuideAssignment {
    private Long id;
    private Departure departure;
    private Person person;
    private String duties;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Departure getDeparture() {
        return departure;
    }

    public void setDeparture(Departure departure) {
        this.departure = departure;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }
}
