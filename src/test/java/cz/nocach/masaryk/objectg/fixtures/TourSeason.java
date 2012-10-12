package cz.nocach.masaryk.objectg.fixtures;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.List;

/**
 * User: __nocach
 * Date: 29.9.12
 */
@Entity
public class TourSeason {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private double relativeRate;
    @OneToMany(mappedBy = "season")
    private List<Tour> toures;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
