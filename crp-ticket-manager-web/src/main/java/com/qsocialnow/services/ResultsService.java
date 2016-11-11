package com.qsocialnow.services;

import java.util.Map;

import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface ResultsService {

    PageResponse<ResultsListView> sumarizeAll(CasesFilterRequest filterRequest);

    byte[] getReport(Map<String, String> filters, String language);

    PageResponse<ResultsListView> sumarizeResolutionByUser(CasesFilterRequest filterRequest);

}
