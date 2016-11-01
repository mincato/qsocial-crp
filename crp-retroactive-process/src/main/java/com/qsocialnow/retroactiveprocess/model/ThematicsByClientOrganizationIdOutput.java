package com.qsocialnow.retroactiveprocess.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ThematicsByClientOrganizationIdOutput {

    private Integer clientOrganizationId;

    private List<RealTimeStaticServerByThematic> thematics = new ArrayList<RealTimeStaticServerByThematic>();

    public Integer getClientOrganizationId() {
        return clientOrganizationId;
    }

    public void setClientOrganizationId(Integer clientOrganizationId) {
        this.clientOrganizationId = clientOrganizationId;
    }

    public List<RealTimeStaticServerByThematic> getThematics() {
        return thematics;
    }

    public void setThematics(List<RealTimeStaticServerByThematic> thematics) {
        this.thematics = thematics;
    }

}
