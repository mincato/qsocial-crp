package com.qsocialnow.services.impl;

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

import com.qsocialnow.common.model.cases.RegistryListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.ActionRegistryService;
import com.qsocialnow.services.ServiceUrlResolver;

@Service("actionRegistryService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ActionRegistryServiceImpl implements ActionRegistryService {

    private static final Logger log = LoggerFactory.getLogger(ActionRegistryServiceImpl.class);

    @Value("${cases.serviceurl}")
    private String caseServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public PageResponse<RegistryListView> findRegistries(int pageNumber, int pageSize, String caseId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(caseServiceUrl)).path("/" + caseId).path("/registries")
                    .queryParam("pageNumber", pageNumber).queryParam("pageSize", pageSize);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<PageResponse> response = restTemplate
                    .getForEntity(builder.toUriString(), PageResponse.class);

            PageResponse<RegistryListView> registries = response.getBody();
            return registries;
        } catch (Exception e) {
            log.error("There was an error while trying to call registry service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<RegistryListView> findRegistriesBy(int pageNumber, int pageSize, String caseId, String keyword,
            String action, String user, Long fromDate, Long toDate) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            // DateFormat df = new SimpleDateFormat("MMMM dd yyyy HH:mm:ss",
            // Locale.ENGLISH);
            // String fromDateFormat = (fromDate!=null)?
            // df.format(fromDate):null;
            // String toDateFormat = (toDate!=null)?df.format(toDate):null;
            String fromDateFormat = null;
            String toDateFormat = null;

            if (fromDate != null) {
                fromDateFormat = String.valueOf(fromDate);
            }

            if (toDate != null) {
                toDateFormat = String.valueOf(toDate);
            }

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl(caseServiceUrl)).path("/" + caseId).path("/registries")
                    .queryParam("text", keyword).queryParam("action", action).queryParam("user", user)
                    .queryParam("fromDate", fromDateFormat).queryParam("toDate", toDateFormat)
                    .queryParam("pageNumber", pageNumber).queryParam("pageSize", pageSize);

            RestTemplate restTemplate = new RestTemplate();
            ParameterizedTypeReference<PageResponse<RegistryListView>> typeRef = new ParameterizedTypeReference<PageResponse<RegistryListView>>() {
            };
            ResponseEntity<PageResponse<RegistryListView>> response = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET, null, typeRef);

            /**
             * ResponseEntity<PageResponse> response = restTemplate
             * .getForEntity(builder.toUriString(), PageResponse.class);
             */

            PageResponse<RegistryListView> registries = response.getBody();
            return registries;
        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }
    }

}
