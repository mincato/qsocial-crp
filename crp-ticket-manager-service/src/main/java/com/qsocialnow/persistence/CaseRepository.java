package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.elasticsearch.services.cases.CaseTicketService;

@Service
public class CaseRepository {

    private Logger log = LoggerFactory.getLogger(CaseRepository.class);

    @Autowired
    private CaseTicketService caseElasticService;

    public List<CaseListView> findAll(PageRequest pageRequest, String domainId, String triggerId, String segmentId,
            String subject, String title, String pendingResponse, String status, String fromOpenDate,
            String toOpenDate, List<String> teamsToFilter, String userName, String userSelected) {
        List<CaseListView> cases = new ArrayList<>();

        try {
            List<Case> casesRepo = caseElasticService.getCases(pageRequest.getOffset(), pageRequest.getLimit(),
                    pageRequest.getSortField(), pageRequest.getSortOrder(), domainId, triggerId, segmentId, subject,
                    title, pendingResponse, status, fromOpenDate, toOpenDate, teamsToFilter, userName, userSelected);

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
                if (caseRepo.getAssignee() != null) {
                    caseListView.setAssignee(caseRepo.getAssignee().getUsername());
                }
                if (caseRepo.getPriority() != null) {
                    caseListView.setPriority(caseRepo.getPriority().name());
                }
                cases.add(caseListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return cases;
    }

    public List<ResultsListView> sumarizeResolvedByResolution(PageRequest pageRequest, String domainId) {
        List<ResultsListView> results = new ArrayList<>();
        try {
            Map<String, Long> resultsRepo = caseElasticService.getCasesCountByResolution(domainId);
            Set<String> resultKeys = resultsRepo.keySet();
            for (String key : resultKeys) {
                ResultsListView resultView = new ResultsListView();
                resultView.setResolution(key);
                resultView.setTotal(resultsRepo.get(key));
                results.add(resultView);
            }

        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return results;
    }

    public JsonArray findAllAsJsonObject(PageRequest pageRequest, String domainId, String triggerId, String segmentId,
            String subject, String title, String description, String pendingResponse, String status,
            String fromOpenDate, String toOpenDate, List<String> teamsToFilter, String userName, String userSelected) {
        JsonObject jsonObject = caseElasticService.getCasesAsJsonObject(pageRequest.getOffset(),
                pageRequest.getLimit(), pageRequest.getSortField(), pageRequest.getSortOrder(), domainId, triggerId,
                segmentId, subject, title, description, pendingResponse, status, fromOpenDate, toOpenDate,
                teamsToFilter, userName, userSelected);
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
