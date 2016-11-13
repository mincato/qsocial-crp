package com.qsocialnow.common.model.cases;

import java.io.Serializable;

import com.qsocialnow.common.model.event.EventLocation;
import com.qsocialnow.common.model.event.GeoSocialEventLocationMethod;

public class CaseLocationView implements Serializable {

    private static final long serialVersionUID = 1292031942073345438L;

    private String id;

    private String originalLocation;

    private EventLocation location;

    private GeoSocialEventLocationMethod locationMethod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalLocation() {
        return originalLocation;
    }

    public void setOriginalLocation(String originalLocation) {
        this.originalLocation = originalLocation;
    }

    public EventLocation getLocation() {
        return location;
    }

    public void setLocation(EventLocation location) {
        this.location = location;
    }

    public GeoSocialEventLocationMethod getLocationMethod() {
        return locationMethod;
    }

    public void setLocationMethod(GeoSocialEventLocationMethod locationMethod) {
        this.locationMethod = locationMethod;
    }

}
