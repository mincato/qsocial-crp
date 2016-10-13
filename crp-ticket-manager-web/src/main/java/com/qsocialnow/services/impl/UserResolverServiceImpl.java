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

import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.model.config.UserResolverListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.ServiceUrlResolver;
import com.qsocialnow.services.UserResolverService;

@Service("userResolverService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserResolverServiceImpl implements UserResolverService {

    private static final Logger log = LoggerFactory.getLogger(UserResolverServiceImpl.class);

    @Value("${userResolver.serviceurl}")
    private String userResolverServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    public UserResolver create(UserResolver userResolver) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            UserResolver createdUserResolver = restTemplate.postForObject(
                    serviceUrlResolver.resolveUrl(userResolverServiceUrl), userResolver, UserResolver.class);
            return createdUserResolver;
        } catch (Exception e) {
            log.error("There was an error while trying to call user resolver service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserResolver findOne(String userResolverId) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(userResolverServiceUrl)).path("/" + userResolverId);
            UserResolver userResolver = restTemplate.getForObject(builder.toUriString(), UserResolver.class);
            return userResolver;
        } catch (Exception e) {
            log.error("There was an error while trying to call user resolver service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserResolver update(UserResolver currentUserResolver) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(userResolverServiceUrl)).path("/" + currentUserResolver.getId());

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            HttpEntity<UserResolver> requestEntity = new HttpEntity<UserResolver>(currentUserResolver, headers);

            ResponseEntity<UserResolver> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT,
                    requestEntity, UserResolver.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call user resolver service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<UserResolverListView> findAll(int pageNumber, int pageSize, Map<String, String> filters) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(userResolverServiceUrl)).path("/list")
                    .queryParam("pageNumber", pageNumber).queryParam("pageSize", pageSize);

            if (filters != null) {
                for (Map.Entry<String, String> filter : filters.entrySet()) {
                    builder.queryParam(filter.getKey(), filter.getValue());
                }
            }

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<PageResponse<UserResolverListView>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<UserResolverListView>>() {
                    });

            PageResponse<UserResolverListView> userResolvers = response.getBody();
            return userResolvers;
        } catch (Exception e) {
            log.error("There was an error while trying to call user resolver service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String userResolverId) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(userResolverServiceUrl)).path("/" + userResolverId);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            restTemplate.delete(builder.toUriString());
        } catch (Exception e) {
            log.error("There was an error while trying to call user resolver service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserResolverListView> findAll(Map<String, String> filters) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serviceUrlResolver
                    .resolveUrl(userResolverServiceUrl));

            if (filters != null) {
                for (Map.Entry<String, String> filter : filters.entrySet()) {
                    builder.queryParam(filter.getKey(), filter.getValue());
                }
            }

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<List<UserResolverListView>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<UserResolverListView>>() {
                    });

            List<UserResolverListView> userResolvers = response.getBody();
            return userResolvers;
        } catch (Exception e) {
            log.error("There was an error while trying to call user resolver service", e);
            throw new RuntimeException(e);
        }

    }

}
