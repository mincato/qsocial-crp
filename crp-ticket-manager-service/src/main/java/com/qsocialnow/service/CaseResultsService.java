package com.qsocialnow.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.persistence.CaseRepository;
import com.qsocialnow.persistence.DomainRepository;
import com.qsocialnow.service.action.Action;

@Service
public class CaseResultsService {

    private static final Logger log = LoggerFactory.getLogger(CaseResultsService.class);

    @Autowired
    private CaseRepository repository;

    @Autowired
    private DomainRepository domainRepository;

    @Resource
    private Map<ActionType, Action> actions;

    public PageResponse<ResultsListView> getResults(CasesFilterRequest filterRequest) {

        log.info("Trying to retrieve cases from :" + filterRequest.getDomain());
        List<ResultsListView> casesByResolution = repository.sumarizeResolvedByResolution(filterRequest);
        if (casesByResolution != null && casesByResolution.size() > 0) {
            Domain domain = domainRepository.findOne(filterRequest.getDomain());
            if (domain != null) {
                List<Resolution> resolutions = domain.getResolutions();
                Map<String, String> resolutionById = resolutions.stream().collect(
                        Collectors.toMap(x -> x.getId(), x -> x.getDescription()));
                casesByResolution.stream().forEach(
                        result -> result.setResolution(resolutionById.get(result.getResolution())));
            }
        }
        PageResponse<ResultsListView> page = new PageResponse<ResultsListView>(casesByResolution, null, null);
        return page;
    }

    public PageResponse<ResultsListView> getResolutionsByUser(String idResolution, CasesFilterRequest filterRequest) {
        log.info("Trying to retrieve cases from :" + filterRequest.getDomain());
        List<ResultsListView> casesByResolution = repository.sumarizeResolutionByUser(filterRequest);
        PageResponse<ResultsListView> page = new PageResponse<ResultsListView>(casesByResolution, null, null);
        return page;
    }
}
