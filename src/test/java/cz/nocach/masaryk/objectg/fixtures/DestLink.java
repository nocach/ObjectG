package cz.nocach.masaryk.objectg.fixtures;

import java.net.URL;

/**
 * User: __nocach
 * Date: 29.9.12
 */
public class DestLink {
    private Long id;
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
