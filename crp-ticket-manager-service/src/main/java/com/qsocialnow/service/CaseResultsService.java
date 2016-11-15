package com.qsocialnow.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.util.UserConstants;
import com.qsocialnow.persistence.CaseRepository;
import com.qsocialnow.persistence.DomainRepository;
import com.qsocialnow.service.action.Action;

@Service
public class CaseResultsService {

    @Autowired
    private CaseRepository repository;

    @Autowired
    private DomainRepository domainRepository;

    @Resource
    private Map<ActionType, Action> actions;

    public PageResponse<ResultsListView> getResults(CasesFilterRequest filterRequest) {
        List<ResultsListView> casesToSumarize = null;

        if (UserConstants.REPORT_BY_RESOLUTION.equals(filterRequest.getFieldToSumarize())) {
            casesToSumarize = repository.sumarizeResolvedByResolution(filterRequest);
            if (casesToSumarize != null && casesToSumarize.size() > 0) {
                Domain domain = domainRepository.findOne(filterRequest.getDomain());
                if (domain != null) {
                    List<Resolution> resolutions = domain.getResolutions();
                    Map<String, String> resolutionById = resolutions.stream().collect(
                            Collectors.toMap(x -> x.getId(), x -> x.getDescription()));
                    casesToSumarize.stream().forEach(
                            result -> result.setResolution(resolutionById.get(result.getResolution())));
                }
            }
        } else if (UserConstants.REPORT_BY_STATUS.equals(filterRequest.getFieldToSumarize())) {
            casesToSumarize = repository.sumarizeByStatus(filterRequest);

        } else if (UserConstants.REPORT_BY_PENDING.equals(filterRequest.getFieldToSumarize())) {
            casesToSumarize = repository.sumarizeByPending(filterRequest);
        } else {
            casesToSumarize = repository.sumarizeByUnitAdmin(filterRequest);
        }

        Collections.sort(casesToSumarize, new Comparator<ResultsListView>() {

            @Override
            public int compare(ResultsListView o1, ResultsListView o2) {
                Long r1 = (o1 == null) ? Long.MAX_VALUE : o1.getTotal();
                Long r2 = (o2 == null) ? Long.MAX_VALUE : o2.getTotal();
                return r2.compareTo(r1);
            }
        });
        PageResponse<ResultsListView> page = new PageResponse<ResultsListView>(casesToSumarize, null, null);
        return page;
    }

    public PageResponse<ResultsListView> getResolutionsByUser(String idResolution, CasesFilterRequest filterRequest) {
        List<ResultsListView> casesByResolution = repository.sumarizeResolutionByUser(filterRequest);
        PageResponse<ResultsListView> page = new PageResponse<ResultsListView>(casesByResolution, null, null);
        return page;
    }

    public PageResponse<ResultsListView> getStatusByUser(CasesFilterRequest filterRequest) {
        List<ResultsListView> casesByResolution = repository.sumarizeResolutionByUser(filterRequest);
        PageResponse<ResultsListView> page = new PageResponse<ResultsListView>(casesByResolution, null, null);
        return page;
    }
}
