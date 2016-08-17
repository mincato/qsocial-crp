package com.qsocialnow.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.ServiceUrlResolver;

@Service("domainService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DomainServiceImpl implements DomainService {

    private static final Logger log = LoggerFactory.getLogger(DomainServiceImpl.class);

    @Value("${domains.serviceurl}")
    private String domainServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    public Domain create(Domain domain) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Domain createdDomain = restTemplate.postForObject(
                    serviceUrlResolver.resolveUrl("centaurico", domainServiceUrl), domain, Domain.class);
            return createdDomain;
        } catch (Exception e) {
            log.error("There was an error while trying to call domain service", e);
            throw new RuntimeException(e);
        }
    }

}
