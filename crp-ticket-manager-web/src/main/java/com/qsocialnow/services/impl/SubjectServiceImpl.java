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

import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.cases.SubjectListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.config.Organization;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.ServiceUrlResolver;
import com.qsocialnow.services.SubjectService;

@Service("subjectService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SubjectServiceImpl implements SubjectService {

    private static final Logger log = LoggerFactory.getLogger(SubjectServiceImpl.class);

    @Value("${subject.serviceurl}")
    private String subjectServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @Override
    public Subject create(Subject currentSubject) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            Subject createdSubjectCategorySet = restTemplate.postForObject(
                    serviceUrlResolver.resolveUrl(Organization.ODATECH, subjectServiceUrl), currentSubject, Subject.class);
            return createdSubjectCategorySet;
        } catch (Exception e) {
            log.error("There was an error while trying to call SubjectCategorySet service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Subject findOne(String subjectId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(Organization.ODATECH, subjectServiceUrl)).path("/" + subjectId);

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            Subject subjectSelected = restTemplate.getForObject(builder.toUriString(), Subject.class);

            return subjectSelected;
        } catch (Exception e) {
            log.error("There was an error while trying to call Subject service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Subject update(Subject currentSubject) {

        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(Organization.ODATECH, subjectServiceUrl)).path("/" + currentSubject.getId());

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            HttpEntity<Subject> requestEntity = new HttpEntity<Subject>(currentSubject, headers);
            ResponseEntity<Subject> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT,
                    requestEntity, Subject.class);

            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to request Subject service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<SubjectListView> findAll(int pageNumber, int pageSize, Map<String, String> filters) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(Organization.ODATECH, subjectServiceUrl))
                    .queryParam("pageNumber", pageNumber);

            if (filters != null) {
                for (Map.Entry<String, String> filter : filters.entrySet()) {
                    builder.queryParam(filter.getKey(), filter.getValue());
                }
            }

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            ResponseEntity<PageResponse<SubjectListView>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<SubjectListView>>() {
                    });

            PageResponse<SubjectListView> subjects = response.getBody();
            return subjects;
        } catch (Exception e) {
            log.error("There was an error while trying to call Subject service", e);
            throw new RuntimeException(e);
        }
    }

}
