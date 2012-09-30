package cz.nocach.masaryk.objectg.fixtures;

import java.sql.Date;
import java.util.List;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class TourSeason {
    private Long id;
    private Tour tour;
    private String name;
    private Date startDate;
    private Date endDate;
    private double relativeRate;
    private List<Tour> toures;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getRelativeRate() {
        return relativeRate;
    }

    public void setRelativeRate(double relativeRate) {
        this.relativeRate = relativeRate;
    }


}
