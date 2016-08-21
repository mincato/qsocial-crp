package com.qsocialnow.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.pagination.PageResponse;
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public PageResponse<DomainListView> findAll(int pageNumber, int pageSize) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl("centaurico", domainServiceUrl))
                    .queryParam("pageNumber", pageNumber).queryParam("pageSize", pageSize);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<PageResponse> response = restTemplate
                    .getForEntity(builder.toUriString(), PageResponse.class);

            PageResponse<DomainListView> domains = response.getBody();
            return domains;
        } catch (Exception e) {
            log.error("There was an error while trying to call domain service", e);
            throw new RuntimeException(e);
        }
    }

}
