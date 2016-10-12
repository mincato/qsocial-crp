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

import com.qsocialnow.common.model.config.CategoryGroup;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.ServiceUrlResolver;
import com.qsocialnow.services.ThematicService;

@Service("thematicService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ThematicServiceImpl implements ThematicService {

    private static Logger log = LoggerFactory.getLogger(ThematicServiceImpl.class);

    @Value("${thematic.serviceurl}")
    private String thematicServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @Override
    public List<Thematic> findAll() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(serviceUrlResolver.resolveUrl("centaurico",
                    thematicServiceUrl));

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<List<Thematic>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    null, new ParameterizedTypeReference<List<Thematic>>() {
                    });

            List<Thematic> thematics = response.getBody();
            return thematics;
        } catch (Exception e) {
            log.error("There was an error while trying to call thematic service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CategoryGroup> findCategoriesBySerieId(Long thematicId, Long serieId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl("centaurico", thematicServiceUrl))
                    .path("/" + thematicId).path("/series/" + serieId).path("/categories");

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<List<CategoryGroup>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    null, new ParameterizedTypeReference<List<CategoryGroup>>() {
                    });

            List<CategoryGroup> categories = response.getBody();
            return categories;
        } catch (Exception e) {
            log.error("There was an error while trying to call thematic service", e);
            throw new RuntimeException(e);
        }
    }

}
