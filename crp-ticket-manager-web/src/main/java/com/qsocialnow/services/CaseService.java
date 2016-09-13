package com.qsocialnow.services;

import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.cases.RegistryListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface CaseService {

    PageResponse<CaseListView> findAll(int pageNumber, int pageSize);

    PageResponse<RegistryListView> findCaseWithRegistries(int activePage, int pageSize, String caseId);

    Case executeAction(String caseId, ActionRequest actionRequest);

}
