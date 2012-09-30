package cz.nocach.masaryk.objectg.fixtures;

import java.util.List;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class Destination {
    private Long id;
    private String city;
    private String region;
    private String country;
    private List<DestLink> references;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<DestLink> getReferences() {
        return references;
    }

    public void setReferences(List<DestLink> references) {
        this.references = references;
    }
}
