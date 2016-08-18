package com.qsocialnow.service;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.persistence.DomainRepository;

@Service
public class DomainService {

    private static final Logger log = LoggerFactory.getLogger(DomainService.class);

    @Autowired
    private DomainRepository repository;

    @Autowired
    private CuratorFramework zookeeperClient;

    @Value("${app.domains.path}")
    private String domainsPath;

    public Domain save(Domain newDomain) {
        Domain domainSaved = null;
        try {
            domainSaved = repository.save(newDomain);
            zookeeperClient.create().forPath(domainsPath.concat(newDomain.getName()));
        } catch (Exception e) {
            log.error("There was an error saving domain: " + newDomain.getName(), e);
            // TODO rollback;
            throw new RuntimeException(e.getMessage());
        }
        return domainSaved;
    }

    public void setRepository(DomainRepository repository) {
        this.repository = repository;
    }

}
