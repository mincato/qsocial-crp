package com.qsocialnow.services.impl;

import java.util.Set;

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

import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.ServiceUrlResolver;
import com.qsocialnow.services.SourceService;

@Service("sourceService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SourceServiceImpl implements SourceService {

    private static final Logger log = LoggerFactory.getLogger(SourceServiceImpl.class);

    @Value("${sourcesBlocked.serviceurl}")
    private String blockedSourceServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @Override
    public Set<Long> getBlockedSources() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serviceUrlResolver
                    .resolveUrl(blockedSourceServiceUrl));

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<Set<Long>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null,
                    new ParameterizedTypeReference<Set<Long>>() {
                    });

            Set<Long> blockedSources = response.getBody();
            return blockedSources;
        } catch (Exception e) {
            log.error("There was an error while trying to call source service", e);
        }
        return null;
    }
}
