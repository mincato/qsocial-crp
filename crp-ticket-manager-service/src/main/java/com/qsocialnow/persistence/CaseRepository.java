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
import com.qsocialnow.common.model.cases.CaseLocationView;
import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.elasticsearch.services.cases.CaseTicketService;

@Service
public class CaseRepository {

    private Logger log = LoggerFactory.getLogger(CaseRepository.class);

    @Autowired
    private CaseTicketService caseElasticService;

    public List<CaseListView> findAll(CasesFilterRequest filterRequest) {
        List<CaseListView> cases = new ArrayList<>();

        try {
            List<Case> casesRepo = caseElasticService.getCasesByFilters(filterRequest);

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

    public List<ResultsListView> sumarizeResolvedByResolution(CasesFilterRequest filterRequest) {
        List<ResultsListView> results = new ArrayList<>();
        try {
            Map<String, Long> resultsRepo = caseElasticService.getCasesCountByResolution(filterRequest);
            Set<String> resultKeys = resultsRepo.keySet();
            for (String key : resultKeys) {
                ResultsListView resultView = new ResultsListView();
                resultView.setIdResolution(key);
                resultView.setResolution(key);
                resultView.setTotal(resultsRepo.get(key));
                results.add(resultView);
            }

        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return results;
    }

    public List<ResultsListView> sumarizeResolvedByStatus(CasesFilterRequest filterRequest) {
        List<ResultsListView> results = new ArrayList<>();
        try {
            Map<String, Long> resultsRepo = caseElasticService.getCasesCountByStatus(filterRequest);
            Set<String> resultKeys = resultsRepo.keySet();
            for (String key : resultKeys) {
                ResultsListView resultView = new ResultsListView();
                resultView.setStatus(key);
                resultView.setTotal(resultsRepo.get(key));
                results.add(resultView);
            }

        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return results;
    }

    public List<ResultsListView> sumarizeResolvedByPending(CasesFilterRequest filterRequest) {
        List<ResultsListView> results = new ArrayList<>();
        try {
            Map<String, Long> resultsRepo = caseElasticService.getCasesCountByPending(filterRequest);
            Set<String> resultKeys = resultsRepo.keySet();
            for (String key : resultKeys) {
                ResultsListView resultView = new ResultsListView();
                resultView.setPending(key);
                resultView.setTotal(resultsRepo.get(key));
                results.add(resultView);
            }

        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return results;
    }

    public JsonArray findAllAsJsonObject(PageRequest pageRequest, CasesFilterRequest filterRequest) {
        JsonObject jsonObject = caseElasticService.getCasesAsJsonObject(pageRequest.getOffset(),
                pageRequest.getLimit(), pageRequest.getSortField(), pageRequest.getSortOrder(), filterRequest);
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

    public List<CaseLocationView> findCasesLocations(CasesFilterRequest filterRequest) {
        List<CaseLocationView> cases = new ArrayList<>();
        try {
            List<Case> casesRepo = caseElasticService.getCasesByFilters(filterRequest);

            for (Case caseRepo : casesRepo) {
                if (caseRepo.getTriggerEvent() != null) {
                    CaseLocationView caseView = new CaseLocationView();
                    caseView.setId(caseRepo.getId());
                    caseView.setLocation(caseRepo.getTriggerEvent().getLocation() != null ? caseRepo.getTriggerEvent()
                            .getLocation() : null);
                    caseView.setLocationMethod(caseRepo.getTriggerEvent().getLocationMethod() != null ? caseRepo
                            .getTriggerEvent().getLocationMethod() : null);
                    caseView.setOriginalLocation(caseRepo.getTriggerEvent().getOriginalLocation() != null ? caseRepo
                            .getTriggerEvent().getOriginalLocation() : null);
                    cases.add(caseView);
                }
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return cases;
    }

    public List<ResultsListView> sumarizeResolutionByUser(CasesFilterRequest filterRequest) {
        List<ResultsListView> results = new ArrayList<>();
        try {
            Map<String, Long> resultsRepo = caseElasticService.getResolutionsByAssigned(filterRequest);
            Set<String> resultKeys = resultsRepo.keySet();
            for (String key : resultKeys) {
                ResultsListView resultView = new ResultsListView();
                resultView.setAssigned(key);
                resultView.setTotal(resultsRepo.get(key));
                results.add(resultView);
            }

        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return results;
    }

}
