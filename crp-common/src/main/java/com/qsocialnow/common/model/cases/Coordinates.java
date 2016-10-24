package com.qsocialnow.common.model.cases;

import java.io.Serializable;

public class Coordinates implements Serializable {

	private static final long serialVersionUID = 1571236095884526627L;

	private double longitude;

    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
