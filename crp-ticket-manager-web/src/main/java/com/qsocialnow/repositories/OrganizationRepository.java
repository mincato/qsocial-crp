package com.qsocialnow.repositories;

import java.util.List;

import com.qsocialnow.common.model.config.ClientOrganization;

public interface OrganizationRepository {

    List<ClientOrganization> findAll();

}
