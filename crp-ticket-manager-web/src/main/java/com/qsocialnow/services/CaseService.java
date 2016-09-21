package com.qsocialnow.services;

import java.util.List;

import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface CaseService {

    PageResponse<CaseListView> findAll(int pageNumber, int pageSize);

    Case findById(String caseId);

    Case executeAction(String caseId, ActionRequest actionRequest);

    List<Resolution> getAvailableResolutions(String caseId);

}
