package com.qsocialnow.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.qsocialnow.common.model.config.AdminUnit;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.model.AdminUnitResponse;
import com.qsocialnow.services.AutocompleteService;

@Service("adminUnitsAutocompleteService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AdminUnitsAutocompleteService implements AutocompleteService<AdminUnit> {

    private static final Logger log = LoggerFactory.getLogger(AdminUnitsAutocompleteService.class);

    @Autowired
    private String adminUnitServiceUrl;

    @Override
    public List<AdminUnit> findBy(String query, int maxRows, Object parameters) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(adminUnitServiceUrl).queryParam("query",
                    query);

            if (parameters != null) {
                String language = (String) parameters;
                builder.queryParam("language", language);
            }

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            AdminUnitResponse response = restTemplate.getForObject(builder.toUriString(), AdminUnitResponse.class);

            return response.getAdministrativeUnits();
        } catch (Exception e) {
            log.error("There was an error while trying to call admin unit service", e);
            throw new RuntimeException(e);
        }
    }

}
