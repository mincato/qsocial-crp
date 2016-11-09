package com.qsocialnow.services;

import java.util.List;
import java.util.Map;

import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.model.pagination.PageRequest;

public interface CaseService {

    PageResponse<CaseListView> findAll(PageRequest pageRequest, Map<String, String> filters);

    Case findById(String caseId);

    Case executeAction(String caseId, ActionRequest actionRequest);

    List<Resolution> getAvailableResolutions(String caseId);

    Case create(Case newCase);

    byte[] getReport(Map<String, String> filters, String language);

    String findGeoJson();

}
