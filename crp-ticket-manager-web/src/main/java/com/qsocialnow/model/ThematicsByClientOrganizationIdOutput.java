package com.qsocialnow.model;

import java.util.ArrayList;
import java.util.List;

public class ThematicsByClientOrganizationIdOutput {

    private Integer clientOrganizationId;

    private List<Thematic> thematics = new ArrayList<Thematic>();

    public Integer getClientOrganizationId() {
        return clientOrganizationId;
    }

    public void setClientOrganizationId(Integer clientOrganizationId) {
        this.clientOrganizationId = clientOrganizationId;
    }

    public List<Thematic> getThematics() {
        return thematics;
    }

    public void setThematics(List<Thematic> thematics) {
        this.thematics = thematics;
    }

}
