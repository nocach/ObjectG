package cz.nocach.masaryk.objectg.fixtures;

import javax.persistence.*;
import java.util.List;

/**
 * User: __nocach
 * Date: 29.9.12
 */
@Entity
public class Tour {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Money standardPrice;
    private Integer standardCapacity;
    @OneToMany(mappedBy = "tour")
    private List<TourStop> stops;
    @ManyToOne
    private TourSeason season;
    private TourType tourType;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Money getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(Money standardPrice) {
        this.standardPrice = standardPrice;
    }

    public Integer getStandardCapacity() {
        return standardCapacity;
    }

    public void setStandardCapacity(Integer standardCapacity) {
        this.standardCapacity = standardCapacity;
    }

    public List<TourStop> getStops() {
        return stops;
    }

    public void setStops(List<TourStop> stops) {
        this.stops = stops;
    }

    public TourType getTourType() {
        return tourType;
    }

    public void setTourType(TourType tourType) {
        this.tourType = tourType;
    }

    public TourSeason getSeason() {
        return season;
    }

    public void setSeason(TourSeason season) {
        this.season = season;
    }
}
