package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.RegistryListView;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.elasticsearch.services.cases.CaseTicketService;

@Service
public class RegistryRepository {

    private Logger log = LoggerFactory.getLogger(RegistryRepository.class);

    @Autowired
    private CaseTicketService caseElasticService;

    public List<RegistryListView> findAll(String caseId, PageRequest pageRequest) {
        List<RegistryListView> registriesView = new ArrayList<>();

        try {

            List<ActionRegistry> regitries = caseElasticService.findCaseWithRegistries(pageRequest.getOffset(),
                    pageRequest.getLimit(), caseId);
            for (ActionRegistry registry : regitries) {
                RegistryListView registryListView = new RegistryListView();
                registryListView.setId(registry.getId());
                registryListView.setUser(registry.getUserName());
                registryListView.setAction(registry.getAction());
                if (registry.getEvent() != null)
                    registryListView.setDescription(registry.getEvent().getDescription());
                registryListView.setDate(registry.getDate());
                registriesView.add(registryListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return registriesView;
    }

    public Long count() {
        return 50L;
    }

}
