package com.qsocialnow.services;

import java.util.List;

import com.qsocialnow.common.model.config.ClientOrganization;

public interface OrganizationService {

    ClientOrganization getCurrentOrganization();

    List<ClientOrganization> getOrganizations();

    List<ClientOrganization> getActiveOrganizations();

    void setCurrentOrganization(ClientOrganization organization);

}
