package com.qsocialnow.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    @Qualifier("zookeeperClient")
    private CuratorFramework zookeeperClient;

    @Value("${zookeeper.base.path}")
    private String zookeeperBasePath;

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
    public List<ClientOrganization> getActiveOrganizations() {

        List<ClientOrganization> allOrgs = organizationRepository.findAll();
        List<Long> zookOrgs = findOrganizationsFromZookeeper();

        return allOrgs.stream().filter(org -> zookOrgs.contains(org.getId())).collect(Collectors.toList());
    }

    private List<Long> findOrganizationsFromZookeeper() {
        try {
            List<String> childrens = zookeeperClient.getChildren().forPath(zookeeperBasePath);
            return childrens.stream().filter(children -> StringUtils.isNumeric(children))
                    .map(children -> Long.parseLong(children)).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void setCurrentOrganization(ClientOrganization organization) {
        Executions.getCurrent().getSession().setAttribute(CURRENT_ORGANIZATION, organization);
    }

}
