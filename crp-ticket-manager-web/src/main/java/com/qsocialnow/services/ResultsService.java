package com.qsocialnow.services;

import java.util.Map;

import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface ResultsService {

    PageResponse<ResultsListView> sumarizeAll(PageRequest pageRequest, Map<String, String> filters);

    byte[] getReport(Map<String, String> filters);

}
