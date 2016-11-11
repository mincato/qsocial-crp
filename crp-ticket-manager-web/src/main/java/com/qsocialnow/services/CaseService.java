package com.qsocialnow.services;

import java.util.List;

import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface CaseService {

    PageResponse<CaseListView> findAll(CasesFilterRequest filterRequest);

    Case findById(String caseId);

    Case executeAction(String caseId, ActionRequest actionRequest);

    List<Resolution> getAvailableResolutions(String caseId);

    Case create(Case newCase);

    byte[] getReport(CasesFilterRequest filterRequest, String language, String timeZone);

    String calculateGeoJson(int pageNumber, int pageSize);

}
