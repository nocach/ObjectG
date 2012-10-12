package cz.nocach.masaryk.objectg.fixtures;

import javax.persistence.*;
import java.util.List;

/**
 * User: __nocach
 * Date: 29.9.12
 */
@Entity
public class Guide {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private Person person;
    private String tourSpecialites;
    @OneToMany(mappedBy = "guide")
    private List<GuideAssignment> assignments;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getTourSpecialites() {
        return tourSpecialites;
    }

    public void setTourSpecialites(String tourSpecialites) {
        this.tourSpecialites = tourSpecialites;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<GuideAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<GuideAssignment> assignments) {
        this.assignments = assignments;
    }
}
