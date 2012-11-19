package org.objectg.fixtures.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * User: __nocach
 * Date: 29.9.12
 */
@Entity
public class Tour implements ITour {
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
	private byte[] barcode;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Money getStandardPrice() {
        return standardPrice;
    }

    @Override
    public void setStandardPrice(Money standardPrice) {
        this.standardPrice = standardPrice;
    }

    @Override
    public Integer getStandardCapacity() {
        return standardCapacity;
    }

    @Override
    public void setStandardCapacity(Integer standardCapacity) {
        this.standardCapacity = standardCapacity;
    }

    @Override
    public List<TourStop> getStops() {
        return stops;
    }

    @Override
    public void setStops(List<TourStop> stops) {
        this.stops = stops;
    }

    @Override
    public TourType getTourType() {
        return tourType;
    }

    @Override
    public void setTourType(TourType tourType) {
        this.tourType = tourType;
    }

    @Override
    public TourSeason getSeason() {
        return season;
    }

    @Override
    public void setSeason(TourSeason season) {
        this.season = season;
    }

	public byte[] getBarcode() {
		return barcode;
	}

	public void setBarcode(final byte[] barcode) {
		this.barcode = barcode;
	}
}
