package com.qsocialnow.services.impl;

import java.util.ArrayList;
import java.util.Date;
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

import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.cases.RegistryListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.ServiceUrlResolver;

@Service("caseService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CaseServiceImpl implements CaseService {

    private static final Logger log = LoggerFactory.getLogger(CaseServiceImpl.class);

    @Value("${cases.serviceurl}")
    private String caseServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public PageResponse<CaseListView> findAll(int pageNumber, int pageSize) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(serviceUrlResolver.resolveUrl("centaurico", caseServiceUrl))
                    .queryParam("pageNumber", pageNumber).queryParam("pageSize", pageSize);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<PageResponse> response = restTemplate
                    .getForEntity(builder.toUriString(), PageResponse.class);

            PageResponse<CaseListView> cases = response.getBody();
            return cases;
        } catch (Exception e) {
            log.error("There was an error while trying to call case service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public PageResponse<RegistryListView> findRegistriesByCase(int activePage, int pageSize) {
        List<RegistryListView> items = new ArrayList<RegistryListView>();
        RegistryListView registry = new RegistryListView();
        registry.setUser("Bruce");
        registry.setAction("Open Action");
        registry.setDate(new Date());
        registry.setDescription("Hey Gotham what's up?");
        items.add(registry);

        PageResponse<RegistryListView> registries = new PageResponse<RegistryListView>(items, 0, 1);
        return registries;
    }

}
