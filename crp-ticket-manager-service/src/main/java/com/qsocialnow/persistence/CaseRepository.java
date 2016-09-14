package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.cases.RegistryListView;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.elasticsearch.services.cases.CaseTicketService;

@Service
public class CaseRepository {

    private Logger log = LoggerFactory.getLogger(CaseRepository.class);

    @Autowired
    private CaseTicketService caseElasticService;

    public List<CaseListView> findAll(PageRequest pageRequest) {
        List<CaseListView> cases = new ArrayList<>();

        try {
            List<Case> casesRepo = caseElasticService.getCases(pageRequest.getOffset(), pageRequest.getLimit());

            for (Case caseRepo : casesRepo) {
                CaseListView caseListView = new CaseListView();
                caseListView.setId(caseRepo.getId());
                caseListView.setTitle(caseRepo.getTitle());
                caseListView.setOpenDate(caseRepo.getOpenDate());
                cases.add(caseListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return cases;
    }

    public List<RegistryListView> findCaseWithRegistries(PageRequest pageRequest, String caseId) {
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

    public Case findOne(String caseId) {
        return caseElasticService.findCaseById(caseId);
    }

    public void save(Case caseObject) {
        caseElasticService.indexCase(caseObject);

    }

    public boolean update(Case caseObject) {
        String id = caseElasticService.update(caseObject);
        return id != null;
    }

}
