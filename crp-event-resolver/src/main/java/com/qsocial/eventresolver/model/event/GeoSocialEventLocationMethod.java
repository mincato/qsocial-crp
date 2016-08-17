package com.qsocial.eventresolver.model.event;

public enum GeoSocialEventLocationMethod {
    GEO_LOCATION("geo_location"), USER_LOCATION("user_location"), POLYGON_LOCATION("polygon_location");

    private String locationMethod;

    GeoSocialEventLocationMethod(String locationMethod) {
        this.locationMethod = locationMethod;
    }

    public String getLocationMethod() {
        return locationMethod;
    }
}
