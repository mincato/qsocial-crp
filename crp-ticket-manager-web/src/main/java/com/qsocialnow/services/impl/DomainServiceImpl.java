package com.qsocialnow.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    @Override
    public Domain findOne(String domainId) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl("centaurico", domainServiceUrl)).path("/" + domainId);

            Domain domain = restTemplate.getForObject(builder.toUriString(), Domain.class);
            return domain;
        } catch (Exception e) {
            log.error("There was an error while trying to call domain service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Domain update(Domain domain) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl("centaurico", domainServiceUrl)).path("/" + domain.getId());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            HttpEntity<Domain> requestEntity = new HttpEntity<Domain>(domain, headers);
            ResponseEntity<Domain> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT,
                    requestEntity, Domain.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call domain service", e);
            throw new RuntimeException(e);
        }
    }

}
