package com.qsocialnow.persistence;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.elasticsearch.services.cases.CaseTicketService;

@Service
public class CaseRepository {

    private Logger log = LoggerFactory.getLogger(CaseRepository.class);

    @Autowired
    private CaseTicketService caseElasticService;

    public List<CaseListView> findAll(PageRequest pageRequest, String subject, String title, String description,
            String pendingResponse, String fromOpenDate, String toOpenDate) {
        List<CaseListView> cases = new ArrayList<>();

        try {
            List<Case> casesRepo = caseElasticService.getCases(pageRequest.getOffset(), pageRequest.getLimit(),
                    pageRequest.getSortField(), pageRequest.getSortOrder(), subject, title, description,
                    pendingResponse, fromOpenDate, toOpenDate);

            for (Case caseRepo : casesRepo) {
                CaseListView caseListView = new CaseListView();
                caseListView.setId(caseRepo.getId());
                caseListView.setTitle(caseRepo.getTitle());
                caseListView.setDescription(caseRepo.getDescription());
                if (caseRepo.getSubject() != null) {
                    caseListView.setSubject(caseRepo.getSubject().getIdentifier());
                }
                caseListView.setOpenDate(caseRepo.getOpenDate());
                caseListView.setPendingResponse(caseRepo.getPendingResponse());
                caseListView.setOpen(caseRepo.getOpen());
                cases.add(caseListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return cases;
    }

    public JsonArray findAllAsJsonObject(PageRequest pageRequest, String title, String description,
            String pendingResponse, String fromOpenDate, String toOpenDate) {
        JsonObject jsonObject = caseElasticService.getCasesAsJsonObject(pageRequest.getOffset(),
                pageRequest.getLimit(), pageRequest.getSortField(), pageRequest.getSortOrder(), title, description,
                pendingResponse, fromOpenDate, toOpenDate);
        return jsonObject.getAsJsonObject("hits").getAsJsonArray("hits");
    }

    public Long count() {
        return 50L;
    }

    public Case findOne(String caseId) {
        return caseElasticService.findCaseById(caseId);
    }

    public Case save(Case caseObject) {

        try {
            String id = caseElasticService.indexCase(caseObject);
            caseObject.setId(id);
            return caseObject;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public boolean update(Case caseObject) {
        String id = caseElasticService.update(caseObject);
        return id != null;
    }

}
