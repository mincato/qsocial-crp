package com.qsocialnow.services.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.services.ServiceUrlResolver;
import com.qsocialnow.services.ThematicService;

@Service("thematicService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ThematicServiceImpl implements ThematicService {

    private static final Logger log = LoggerFactory.getLogger(ThematicServiceImpl.class);

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

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Thematic[]> thematics = restTemplate.getForEntity(builder.toUriString(), Thematic[].class);

            return Arrays.asList(thematics.getBody());
        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }
    }

}
