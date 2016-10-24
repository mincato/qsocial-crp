package com.qsocialnow.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public PageResponse<ResultsListView> getResults(String domainId) {
        log.info("Trying to retrieve cases from :" + domainId);
        List<ResultsListView> casesByResolution = repository.sumarizeResolvedByResolution(null, domainId);
        if (casesByResolution != null && casesByResolution.size() > 0) {
            Domain domain = domainRepository.findOne(domainId);
            if (domain != null) {
                List<Resolution> resolutions = domain.getResolutions();
                Map<String, String> resolutionById = resolutions.stream().collect(
                        Collectors.toMap(x -> x.getId().toLowerCase(), x -> x.getDescription()));
                casesByResolution.stream().forEach(
                        result -> result.setResolution(resolutionById.get(result.getResolution())));
            }
        }
        PageResponse<ResultsListView> page = new PageResponse<ResultsListView>(casesByResolution, null, null);
        return page;
    }
}
