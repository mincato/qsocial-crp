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

import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.config.Organization;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.ResolutionService;
import com.qsocialnow.services.ServiceUrlResolver;

@Service("resolutionService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ResolutionServiceImpl implements ResolutionService {

    private static final Logger log = LoggerFactory.getLogger(ResolutionServiceImpl.class);

    @Value("${domains.serviceurl}")
    private String domainServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @Override
    public Resolution create(String domainId, Resolution resolution) {
        try {

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(Organization.ODATECH, domainServiceUrl))
                    .path("/" + domainId).path("/resolutions");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            HttpEntity<Resolution> requestEntity = new HttpEntity<Resolution>(resolution, headers);

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<Resolution> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT,
                    requestEntity, Resolution.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call resolution service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resolution update(String domainId, Resolution resolution) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(Organization.ODATECH, domainServiceUrl))
                    .path("/" + domainId).path("/resolutions/").path(resolution.getId());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            HttpEntity<Resolution> requestEntity = new HttpEntity<Resolution>(resolution, headers);

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<Resolution> response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT,
                    requestEntity, Resolution.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call resolution service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String domainId, String resolutionId) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(Organization.ODATECH, domainServiceUrl))
                    .path("/" + domainId).path("/resolutions/").path(resolutionId);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            restTemplate.delete(builder.toUriString());
        } catch (Exception e) {
            log.error("There was an error while trying to call resolution service", e);
            throw new RuntimeException(e);
        }
    }

}
