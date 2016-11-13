package com.qsocialnow.services.impl;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.cases.CasesFilterRequestReport;
import com.qsocialnow.common.model.cases.ResultsListView;
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

    @SuppressWarnings({ "unchecked" })
    public PageResponse<ResultsListView> sumarizeAll(CasesFilterRequest filterRequest) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            PageResponse<ResultsListView> results = restTemplate.postForObject(
                    serviceUrlResolver.resolveUrl(caseServiceUrl) + "/resolutions", filterRequest, PageResponse.class);

            return results;

        } catch (Exception e) {
            log.error("There was an error while trying to call sumarize all service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getReport(CasesFilterRequestReport filterRequestReport) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(caseServiceUrl)).path("/resolutions/report");

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
            byte[] data = restTemplate.postForObject(builder.toUriString(), filterRequestReport, byte[].class);

            return data;
        } catch (Exception e) {
            log.error("There was an error while trying to call results report service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public PageResponse<ResultsListView> sumarizeResolutionByUser(CasesFilterRequest filterRequest) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            PageResponse<ResultsListView> results = restTemplate.postForObject(
                    serviceUrlResolver.resolveUrl(caseServiceUrl) + "/resolutions/" + filterRequest.getIdResolution(),
                    filterRequest, PageResponse.class);

            return results;

        } catch (Exception e) {
            log.error("There was an error while trying to call sumarize all service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public PageResponse<ResultsListView> sumarizeStatusByUser(CasesFilterRequest filterRequest) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            PageResponse<ResultsListView> results = restTemplate.postForObject(
                    serviceUrlResolver.resolveUrl(caseServiceUrl) + "/status", filterRequest, PageResponse.class);

            return results;

        } catch (Exception e) {
            log.error("There was an error while trying to call sumarize all service", e);
            throw new RuntimeException(e);
        }
    }

}
