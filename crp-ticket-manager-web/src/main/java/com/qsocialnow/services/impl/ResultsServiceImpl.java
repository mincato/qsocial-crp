package com.qsocialnow.services.impl;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.ResultsService;
import com.qsocialnow.services.ServiceUrlResolver;

@Service("resultsService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ResultsServiceImpl implements ResultsService {

    private static final Logger log = LoggerFactory.getLogger(CaseServiceImpl.class);

    @Value("${cases.serviceurl}")
    private String caseServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public PageResponse<ResultsListView> sumarizeAll(PageRequest pageRequest, Map<String, String> filters) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(caseServiceUrl)).path("/results")
                    .queryParam("pageNumber", pageRequest.getPageNumber())
                    .queryParam("pageSize", pageRequest.getPageSize())
                    .queryParam("sortField", pageRequest.getSortField())
                    .queryParam("sortOrder", pageRequest.getSortOrder());

            if (filters != null) {
                for (Map.Entry<String, String> filter : filters.entrySet()) {
                    builder.queryParam(filter.getKey(), filter.getValue());
                }
            }

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<PageResponse> response = restTemplate
                    .getForEntity(builder.toUriString(), PageResponse.class);
            PageResponse<ResultsListView> results = response.getBody();
            return results;

        } catch (Exception e) {
            log.error("There was an error while trying to call sumarize all service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getReport(Map<String, String> filters) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(caseServiceUrl)).path("/results/report");

            if (filters != null) {
                for (Map.Entry<String, String> filter : filters.entrySet()) {
                    builder.queryParam(filter.getKey(), filter.getValue());
                }
            }

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
            byte[] data = restTemplate.getForObject(builder.toUriString(), byte[].class);

            return data;
        } catch (Exception e) {
            log.error("There was an error while trying to call results report service", e);
            throw new RuntimeException(e);
        }
    }

}
