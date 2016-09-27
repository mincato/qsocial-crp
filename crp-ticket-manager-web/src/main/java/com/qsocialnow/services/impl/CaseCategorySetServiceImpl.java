package com.qsocialnow.services.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.common.model.config.CaseCategorySetListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.CaseCategorySetService;
import com.qsocialnow.services.ServiceUrlResolver;

@Service("caseCategorySetService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CaseCategorySetServiceImpl implements CaseCategorySetService {

    private static final Logger log = LoggerFactory.getLogger(CaseCategorySetServiceImpl.class);

    @Value("${caseCategorySet.serviceurl}")
    private String caseCategorySetServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @Override
    public CaseCategorySet create(CaseCategorySet currentCaseCategorySet) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            CaseCategorySet createdCaseCategorySet = restTemplate.postForObject(
                    serviceUrlResolver.resolveUrl("centaurico", caseCategorySetServiceUrl), currentCaseCategorySet,
                    CaseCategorySet.class);
            return createdCaseCategorySet;
        } catch (Exception e) {
            log.error("There was an error while trying to call CaseCategorySet service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CaseCategorySet findOne(String caseCategorySetId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl("centaurico", caseCategorySetServiceUrl)).path(
                    "/" + caseCategorySetId);

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            CaseCategorySet caseCategorySetSelected = restTemplate.getForObject(builder.toUriString(),
                    CaseCategorySet.class);

            return caseCategorySetSelected;
        } catch (Exception e) {
            log.error("There was an error while trying to call CaseCategorySet service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CaseCategorySet update(CaseCategorySet currentCaseCategorySet) {

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl("centaurico", caseCategorySetServiceUrl)).path(
                    "/" + currentCaseCategorySet.getId());

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            HttpEntity<CaseCategorySet> requestEntity = new HttpEntity<CaseCategorySet>(currentCaseCategorySet, headers);

            ResponseEntity<CaseCategorySet> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT,
                    requestEntity, CaseCategorySet.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to CaseCategorySet team service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<CaseCategorySetListView> findAll(int pageNumber, int pageSize, Map<String, String> filters) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            String description = null;
            if (filters != null) {
                description = filters.get("description");
            }
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl("centaurico", caseCategorySetServiceUrl))
                    .queryParam("pageNumber", pageNumber).queryParam("pageSize", pageSize)
                    .queryParam("name", description);

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            ResponseEntity<PageResponse<CaseCategorySetListView>> response = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, null,
                    new ParameterizedTypeReference<PageResponse<CaseCategorySetListView>>() {
                    });

            PageResponse<CaseCategorySetListView> caseCategorySets = response.getBody();
            return caseCategorySets;
        } catch (Exception e) {
            log.error("There was an error while trying to call CaseCategorySet service", e);
            throw new RuntimeException(e);
        }
    }

}