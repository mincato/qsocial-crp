package com.qsocialnow.services.impl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.Trigger;
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

    @Override
    public Domain findOne(String domainId) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl("centaurico", domainServiceUrl)).path("/" + domainId);
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.setDateFormat(DateFormat.getDateInstance(DateFormat.SHORT));
            jsonMessageConverter.setObjectMapper(mapper);
            messageConverters.add(jsonMessageConverter);
            restTemplate.setMessageConverters(messageConverters);
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

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setDateFormat(DateFormat.getDateInstance(DateFormat.SHORT));
            jsonMessageConverter.setObjectMapper(mapper);
            messageConverters.add(jsonMessageConverter);
            restTemplate.setMessageConverters(messageConverters);

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

    @Override
    public void createTrigger(String domainId, Trigger trigger) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl("centaurico", domainServiceUrl)).path("/" + domainId)
                    .path("/trigger");

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setDateFormat(DateFormat.getDateInstance(DateFormat.SHORT));
            jsonMessageConverter.setObjectMapper(mapper);
            messageConverters.add(jsonMessageConverter);
            restTemplate.setMessageConverters(messageConverters);

            restTemplate.put(builder.toUriString(), trigger);
        } catch (Exception e) {
            log.error("There was an error while trying to call domain service", e);
            throw new RuntimeException(e);
        }
    }
}
