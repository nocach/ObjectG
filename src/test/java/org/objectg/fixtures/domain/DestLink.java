package org.objectg.fixtures.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.net.URL;

/**
 * User: __nocach
 * Date: 29.9.12
 */
@Entity
public class DestLink {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Destination destination;
    private URL url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
