package com.qsocialnow.services.impl;

import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Executions;

import com.qsocialnow.common.model.config.ClientOrganization;
import com.qsocialnow.security.AuthorizationFilter;
import com.qsocialnow.security.UserData;
import com.qsocialnow.services.OrganizationService;

@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationService {

    @Override
    public ClientOrganization getCurrentOrganization() {
        UserData userData = (UserData) Executions.getCurrent().getSession()
                .getAttribute(AuthorizationFilter.USER_SESSION_PARAMETER);
        ClientOrganization currentOrganization = new ClientOrganization();
        currentOrganization.setId(userData.getOrganization());
        return currentOrganization;
    }

}
