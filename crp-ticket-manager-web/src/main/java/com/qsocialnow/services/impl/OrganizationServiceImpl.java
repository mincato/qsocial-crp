package com.qsocialnow.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Executions;

import com.qsocialnow.common.model.config.ClientOrganization;
import com.qsocialnow.repositories.OrganizationRepository;
import com.qsocialnow.security.AuthorizationHelper;
import com.qsocialnow.security.UserData;
import com.qsocialnow.security.exception.AuthorizationException;
import com.qsocialnow.services.OrganizationService;

@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationService {

    private static final String CURRENT_ORGANIZATION = "currentOrganization";
    
	@Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public ClientOrganization getCurrentOrganization() {
        ClientOrganization currentOrganization = (ClientOrganization) Executions.getCurrent().getSession()
                .getAttribute(CURRENT_ORGANIZATION);
        if (currentOrganization == null) {
            UserData userData = (UserData) Executions.getCurrent().getSession()
                    .getAttribute(AuthorizationHelper.USER_SESSION_PARAMETER);
            if (userData == null) {
                throw new AuthorizationException();
            }
            currentOrganization = new ClientOrganization();
            currentOrganization.setId(userData.getOrganization());
        }
        return currentOrganization;
    }

    @Override
    public List<ClientOrganization> getOrganizations() {
        return organizationRepository.findAll();
    }

    @Override
    public void setCurrentOrganization(ClientOrganization organization) {
        Executions.getCurrent().getSession().setAttribute(CURRENT_ORGANIZATION, organization);
    }

}
