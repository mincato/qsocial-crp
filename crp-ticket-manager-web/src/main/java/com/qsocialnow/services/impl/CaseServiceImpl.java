package com.qsocialnow.services.impl;

import java.util.Arrays;
import java.util.HashMap;
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
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.JsonObject;
import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.cases.CasesFilterRequestReport;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.ServiceUrlResolver;
import com.qsocialnow.services.UserSessionService;

@Service("caseService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CaseServiceImpl implements CaseService {

    private static final Logger log = LoggerFactory.getLogger(CaseServiceImpl.class);

    @Value("${cases.serviceurl}")
    private String caseServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @Autowired
    private UserSessionService userSessionService;

    @SuppressWarnings({ "unchecked" })
    @Override
    public PageResponse<CaseListView> findAll(CasesFilterRequest filterRequest) {
        try {
            String userName = userSessionService.getUsername();
            boolean isAdmin = userSessionService.isAdmin();
            // user
            if (!isAdmin) {
                filterRequest.setUserName(userName);
            }
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            PageResponse<CaseListView> cases = restTemplate.postForObject(serviceUrlResolver.resolveUrl(caseServiceUrl)
                    + "/list", filterRequest, PageResponse.class);
            return cases;

        } catch (Exception e) {
            log.error("There was an error while trying to call retroactive service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Case findById(String caseId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(caseServiceUrl)).path("/" + caseId);

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            Case caseSelected = restTemplate.getForObject(builder.toUriString(), Case.class);

            return caseSelected;
        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Case executeAction(String caseId, ActionRequest actionRequest) {
        addExecutor(actionRequest);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(caseServiceUrl)).path("/" + caseId).path("/action");

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            Case caseUpdated = restTemplate.postForObject(builder.toUriString(), actionRequest, Case.class);
            return caseUpdated;
        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }

    }

    private void addExecutor(ActionRequest actionRequest) {
        if (actionRequest.getParameters() == null) {
            actionRequest.setParameters(new HashMap<ActionParameter, Object>());
        }
        actionRequest.getParameters().put(ActionParameter.EXECUTOR, userSessionService.getUsername());
    }

    @Override
    public List<Resolution> getAvailableResolutions(String caseId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(caseServiceUrl)).path("/" + caseId)
                    .path("/availableResolutions");

            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            ResponseEntity<List<Resolution>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    null, new ParameterizedTypeReference<List<Resolution>>() {
                    });

            return response.getBody();
        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public Case create(Case newCase) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            Case createdCase = restTemplate.postForObject(serviceUrlResolver.resolveUrl(caseServiceUrl), newCase,
                    Case.class);
            return createdCase;
        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getReport(CasesFilterRequest filterRequest, String language, String timeZone) {
        try {
            String userName = userSessionService.getUsername();
            boolean isAdmin = userSessionService.isAdmin();
            // user
            if (!isAdmin) {
                filterRequest.setUserName(userName);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    serviceUrlResolver.resolveUrl(caseServiceUrl)).path("/report");

            CasesFilterRequestReport filterRequestReport = new CasesFilterRequestReport();
            filterRequestReport.setFilterRequest(filterRequest);
            filterRequestReport.setLanguage(language);
            filterRequestReport.setTimeZone(timeZone);
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
            byte[] data = restTemplate.postForObject(builder.toUriString(), filterRequestReport, byte[].class);
            return data;

        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public String calculateGeoJson(int pageNumber, int pageSize) {
       try {
    		 UriComponentsBuilder builder = UriComponentsBuilder
    	                .fromHttpUrl(serviceUrlResolver.resolveUrl(caseServiceUrl)).path("/map").queryParam("pageNumber", pageNumber)
                        .queryParam("pageSize", pageSize);
    	     RestTemplate restTemplate = RestTemplateFactory.createRestTemplate(); 
    	     String geoJson = restTemplate.getForObject(builder.toUriString(), String.class);
             return geoJson.toString();
         } catch (Exception e) {
             log.error("There was an error while trying to call retroactive service", e);
             throw new RuntimeException(e);
         }
    }

}
