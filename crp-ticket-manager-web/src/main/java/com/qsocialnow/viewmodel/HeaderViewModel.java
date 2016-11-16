package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.ClientOrganization;
import com.qsocialnow.security.SessionHandler;
import com.qsocialnow.services.OrganizationService;
import com.qsocialnow.services.UserSessionService;
import com.qsocialnow.services.impl.AnalyticsAuthService;

@VariableResolver(DelegatingVariableResolver.class)
public class HeaderViewModel implements Serializable {

    private static final long serialVersionUID = -5168365811321248975L;

    @WireVariable
    private AnalyticsAuthService analyticsAuthService;

    @WireVariable
    private SessionHandler sessionHandler;

    @WireVariable
    private OrganizationService organizationService;

    @WireVariable
    private UserSessionService userSessionService;

    private List<ClientOrganization> organizations;

    private ClientOrganization organization;

    public List<ClientOrganization> getOrganizations() {
        return organizations;
    }

    @Init
    public void init() {
        if (userSessionService.isOdatech()) {
            organizations = organizationService.getActiveOrganizations();
            ClientOrganization currentOrganization = organizationService.getCurrentOrganization();
            Optional<ClientOrganization> optionalOrganization = organizations.stream()
                    .filter(organization -> organization.getId().equals(currentOrganization.getId())).findFirst();
            if (optionalOrganization.isPresent()) {
                organization = optionalOrganization.get();
            }
        }
    }

    @Command
    public void redirectAnalytics() {
        analyticsAuthService.redirectAnalyticsHome();
    }

    @Command
    public void logout() {
        sessionHandler.logout();
    }

    @Command
    public void selectOrganization() {
        organizationService.setCurrentOrganization(organization);
        Executions.getCurrent().sendRedirect("");
    }

    public ClientOrganization getOrganization() {
        return organization;
    }

    public void setOrganization(ClientOrganization organization) {
        this.organization = organization;
    }

}
