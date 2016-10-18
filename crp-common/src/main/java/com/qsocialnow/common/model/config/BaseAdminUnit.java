package com.qsocialnow.common.model.config;

public class BaseAdminUnit {

    private Long geoNameId;

    private String translation;

    private AdminUnitType type;

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public AdminUnitType getType() {
        return type;
    }

    public void setType(AdminUnitType type) {
        this.type = type;
    }

    public Long getGeoNameId() {
        return geoNameId;
    }

    public void setGeoNameId(Long geoNameId) {
        this.geoNameId = geoNameId;
    }

}
