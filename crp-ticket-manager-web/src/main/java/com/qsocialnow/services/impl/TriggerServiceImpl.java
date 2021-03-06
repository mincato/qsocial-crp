package com.qsocialnow.services.impl;

import java.util.List;
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
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.SegmentListView;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.ServiceUrlResolver;
import com.qsocialnow.services.TriggerService;

@Service("triggerService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TriggerServiceImpl implements TriggerService {

    private static final Logger log = LoggerFactory.getLogger(TriggerServiceImpl.class);

    @Value("${domains.serviceurl}")
    private String domainServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @Override
    public Trigger create(String domainId, Trigger trigger) {
        try {

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(domainServiceUrl)).path("/" + domainId).path("/trigger");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            HttpEntity<Trigger> requestEntity = new HttpEntity<Trigger>(trigger, headers);

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<Trigger> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT,
                    requestEntity, Trigger.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call trigger service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<TriggerListView> findAll(String domainId, int pageNumber, int pageSize,
            Map<String, String> filters) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(domainServiceUrl)).path("/" + domainId).path("/trigger")
                    .queryParam("pageNumber", pageNumber).queryParam("pageSize", pageSize);

            if (filters != null) {
                for (Map.Entry<String, String> filter : filters.entrySet()) {
                    builder.queryParam(filter.getKey(), filter.getValue());
                }
            }

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<PageResponse<TriggerListView>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<TriggerListView>>() {
                    });

            PageResponse<TriggerListView> triggers = response.getBody();
            return triggers;
        } catch (Exception e) {
            log.error("There was an error while trying to call domain service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TriggerListView> findAllActive(String domainId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(domainServiceUrl)).path("/" + domainId)
                    .path("/triggerActive");

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<List<TriggerListView>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<TriggerListView>>() {
                    });

            List<TriggerListView> triggers = response.getBody();
            return triggers;
        } catch (Exception e) {
            log.error("There was an error while trying to call domain service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Trigger findOne(String domainId, String triggerId) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(domainServiceUrl)).path("/" + domainId)
                    .path("/trigger/" + triggerId);

            Trigger trigger = restTemplate.getForObject(builder.toUriString(), Trigger.class);
            return trigger;
        } catch (Exception e) {
            log.error("There was an error while trying to call trigger service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Trigger findOneWithActiveSegments(String domainId, String triggerId) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(domainServiceUrl)).path("/" + domainId)
                    .path("/triggerWithActiveSegments/" + triggerId);

            Trigger trigger = restTemplate.getForObject(builder.toUriString(), Trigger.class);
            return trigger;
        } catch (Exception e) {
            log.error("There was an error while trying to call trigger service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Trigger update(String domainId, Trigger trigger) {
        try {

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(domainServiceUrl)).path("/" + domainId)
                    .path("/trigger/" + trigger.getId());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            HttpEntity<Trigger> requestEntity = new HttpEntity<Trigger>(trigger, headers);

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<Trigger> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT,
                    requestEntity, Trigger.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call trigger service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Segment findSegment(String domainId, String triggerId, String segmentId) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(domainServiceUrl)).path("/" + domainId)
                    .path("/trigger/" + triggerId).path("/segment/" + segmentId);

            Segment segment = restTemplate.getForObject(builder.toUriString(), Segment.class);
            return segment;
        } catch (Exception e) {
            log.error("There was an error while trying to call trigger service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SegmentListView> findSegments(String domainId, String triggerId) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(domainServiceUrl)).path("/" + domainId)
                    .path("/trigger/" + triggerId).path("/segments");

            ResponseEntity<List<SegmentListView>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<SegmentListView>>() {
                    });
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call trigger service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SegmentListView> findActiveSegments(String domainId, String triggerId) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(domainServiceUrl)).path("/" + domainId)
                    .path("/trigger/" + triggerId).path("/segmentsActive");

            ResponseEntity<List<SegmentListView>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<SegmentListView>>() {
                    });
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call trigger service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CaseCategorySet> findCategories(String domainId, String triggerId) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(domainServiceUrl)).path("/" + domainId)
                    .path("/trigger/" + triggerId).path("/caseCategories");

            ResponseEntity<List<CaseCategorySet>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<CaseCategorySet>>() {
                    });
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call trigger service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CaseCategorySet> findActiveCategories(String domainId, String triggerId) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(domainServiceUrl)).path("/" + domainId)
                    .path("/trigger/" + triggerId).path("/caseCategoriesActive");

            ResponseEntity<List<CaseCategorySet>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<CaseCategorySet>>() {
                    });
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call trigger service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SubjectCategorySet> findSubjectCategories(String domainId, String triggerId) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(domainServiceUrl)).path("/" + domainId)
                    .path("/trigger/" + triggerId).path("/subjectCategories");

            ResponseEntity<List<SubjectCategorySet>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<SubjectCategorySet>>() {
                    });
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call trigger service", e);
            throw new RuntimeException(e);
        }
    }

}
