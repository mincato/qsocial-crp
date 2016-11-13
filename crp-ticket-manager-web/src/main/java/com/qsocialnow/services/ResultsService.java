package com.qsocialnow.services;

import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.cases.CasesFilterRequestReport;
import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface ResultsService {

    PageResponse<ResultsListView> sumarizeAll(CasesFilterRequest filterRequest);

    byte[] getReport(CasesFilterRequestReport filterRequestReport);

    PageResponse<ResultsListView> sumarizeResolutionByUser(CasesFilterRequest filterRequest);

    PageResponse<ResultsListView> sumarizeStatusByUser(CasesFilterRequest filterRequest);

}
