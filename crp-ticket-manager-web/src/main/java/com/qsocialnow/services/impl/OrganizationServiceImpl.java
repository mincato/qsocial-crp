package com.qsocialnow.services.impl;

import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Executions;

import com.qsocialnow.common.model.config.ClientOrganization;
import com.qsocialnow.security.AuthorizationHelper;
import com.qsocialnow.security.UserData;
import com.qsocialnow.security.exception.AuthorizationException;
import com.qsocialnow.services.OrganizationService;

@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationService {

    @Override
    public ClientOrganization getCurrentOrganization() {
        UserData userData = (UserData) Executions.getCurrent().getSession()
                .getAttribute(AuthorizationHelper.USER_SESSION_PARAMETER);
        if (userData == null) {
            throw new AuthorizationException();
        }
        ClientOrganization currentOrganization = new ClientOrganization();
        currentOrganization.setId(userData.getOrganization());
        return currentOrganization;
    }

}
