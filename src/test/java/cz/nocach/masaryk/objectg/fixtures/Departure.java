package cz.nocach.masaryk.objectg.fixtures;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
}
