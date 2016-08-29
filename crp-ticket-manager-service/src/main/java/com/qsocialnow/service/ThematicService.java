package com.qsocialnow.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.qsocialnow.common.model.config.Thematic;

@Service
public class ThematicService {

    private static final Logger log = LoggerFactory.getLogger(ThematicService.class);

    @SuppressWarnings({ "unchecked" })
    public List<Thematic> findAll() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://demo7078473.mockable.io/tematicas");

            RestTemplate restTemplate = new RestTemplate();
            List<Thematic> thematics = restTemplate.getForObject(builder.toUriString(), List.class);

            return thematics;
        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }
    }

}
