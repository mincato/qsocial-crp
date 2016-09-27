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

import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.common.model.config.SubjectCategorySetListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.ServiceUrlResolver;
import com.qsocialnow.services.SubjectCategorySetService;

@Service("subjectCategorySetService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SubjectCategorySetServiceImpl implements SubjectCategorySetService {

    private static final Logger log = LoggerFactory.getLogger(SubjectCategorySetServiceImpl.class);

    @Value("${subjectCategorySet.serviceurl}")
    private String subjectCategorySetServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @Override
    public SubjectCategorySet create(SubjectCategorySet currentSubjectCategorySet) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            SubjectCategorySet createdSubjectCategorySet = restTemplate.postForObject(
                    serviceUrlResolver.resolveUrl("centaurico", subjectCategorySetServiceUrl),
                    currentSubjectCategorySet, SubjectCategorySet.class);
            return createdSubjectCategorySet;
        } catch (Exception e) {
            log.error("There was an error while trying to call SubjectCategorySet service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public SubjectCategorySet findOne(String subjectCategorySetId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl("centaurico", subjectCategorySetServiceUrl)).path(
                    "/" + subjectCategorySetId);

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            SubjectCategorySet subjectCategorySetSelected = restTemplate.getForObject(builder.toUriString(),
                    SubjectCategorySet.class);

            return subjectCategorySetSelected;
        } catch (Exception e) {
            log.error("There was an error while trying to call SubjectCategorySet service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public SubjectCategorySet update(SubjectCategorySet currentSubjectCategorySet) {

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl("centaurico", subjectCategorySetServiceUrl)).path(
                    "/" + currentSubjectCategorySet.getId());

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            HttpEntity<SubjectCategorySet> requestEntity = new HttpEntity<SubjectCategorySet>(
                    currentSubjectCategorySet, headers);

            ResponseEntity<SubjectCategorySet> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT,
                    requestEntity, SubjectCategorySet.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to SubjectCategorySet team service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<SubjectCategorySetListView> findAll(int pageNumber, int pageSize, Map<String, String> filters) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            String description = null;
            if (filters != null) {
                description = filters.get("description");
            }
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl("centaurico", subjectCategorySetServiceUrl))
                    .queryParam("pageNumber", pageNumber).queryParam("pageSize", pageSize)
                    .queryParam("name", description);

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            ResponseEntity<PageResponse<SubjectCategorySetListView>> response = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, null,
                    new ParameterizedTypeReference<PageResponse<SubjectCategorySetListView>>() {
                    });

            PageResponse<SubjectCategorySetListView> subjectCategorySets = response.getBody();
            return subjectCategorySets;
        } catch (Exception e) {
            log.error("There was an error while trying to call SubjectCategorySet service", e);
            throw new RuntimeException(e);
        }
    }

}
