package com.qsocialnow.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.config.Organization;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.ServiceUrlResolver;

@Service("caseService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CaseServiceImpl implements CaseService {

    private static final Logger log = LoggerFactory.getLogger(CaseServiceImpl.class);

    @Value("${cases.serviceurl}")
    private String caseServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public PageResponse<CaseListView> findAll(int pageNumber, int pageSize) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(Organization.ODATECH, caseServiceUrl))
                    .queryParam("pageNumber", pageNumber).queryParam("pageSize", pageSize);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<PageResponse> response = restTemplate
                    .getForEntity(builder.toUriString(), PageResponse.class);

            PageResponse<CaseListView> cases = response.getBody();
            return cases;
        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Case findById(String caseId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(Organization.ODATECH, caseServiceUrl)).path("/" + caseId);

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            Case caseSelected = restTemplate.getForObject(builder.toUriString(), Case.class);

            return caseSelected;
        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Case executeAction(String caseId, ActionRequest actionRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(Organization.ODATECH, caseServiceUrl))
                    .path("/" + caseId).path("/action");

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            Case caseUpdated = restTemplate.postForObject(builder.toUriString(), actionRequest, Case.class);
            return caseUpdated;
        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Resolution> getAvailableResolutions(String caseId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(Organization.ODATECH, caseServiceUrl))
                    .path("/" + caseId).path("/availableResolutions");

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<List<Resolution>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    null, new ParameterizedTypeReference<List<Resolution>>() {
                    });

            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public Case create(Case newCase) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            Case createdCase = restTemplate.postForObject(
                    serviceUrlResolver.resolveUrl(Organization.ODATECH, caseServiceUrl), newCase, Case.class);
            return createdCase;
        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }
    }

}
