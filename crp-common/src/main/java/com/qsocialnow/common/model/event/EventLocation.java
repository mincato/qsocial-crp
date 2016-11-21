package com.qsocialnow.common.model.event;

import java.io.Serializable;

public class EventLocation implements Serializable {

    private static final long serialVersionUID = -8875193134922331125L;

    private Double lat;
    private Double lon;

    public EventLocation() {
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

}
