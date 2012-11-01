package org.objectg.fixtures.domain;

import java.util.List;

/**
 * User: __nocach
 * Date: 1.11.12
 */
public interface ITour {
    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    Money getStandardPrice();

    void setStandardPrice(Money standardPrice);

    Integer getStandardCapacity();

    void setStandardCapacity(Integer standardCapacity);

    List<TourStop> getStops();

    void setStops(List<TourStop> stops);

    TourType getTourType();

    void setTourType(TourType tourType);

    TourSeason getSeason();

    void setSeason(TourSeason season);
}
