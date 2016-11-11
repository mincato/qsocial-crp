package com.qsocialnow.repositories;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qsocialnow.common.model.config.ClientOrganization;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientOrganizationsOutput {

    private List<ClientOrganization> clientOrganizations;

    public List<ClientOrganization> getClientOrganizations() {
        return clientOrganizations;
    }

    public void setClientOrganizations(List<ClientOrganization> clientOrganizations) {
        this.clientOrganizations = clientOrganizations;
    }

}
