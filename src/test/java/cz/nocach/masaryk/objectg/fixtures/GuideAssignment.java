package cz.nocach.masaryk.objectg.fixtures;

import javax.persistence.*;

/**
 * User: __nocach
 * Date: 29.9.12
 */
@Entity
public class GuideAssignment {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Departure departure;
    @ManyToOne
    private Guide guide;
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

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }
}
