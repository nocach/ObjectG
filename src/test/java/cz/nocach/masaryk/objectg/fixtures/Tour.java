package cz.nocach.masaryk.objectg.fixtures;

import java.util.List;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class Tour {
    private Long id;
    private String name;
    private String description;
    private Money standardPrice;
    private Integer standardCapacity;
    private List<TourStop> stops;
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
}
