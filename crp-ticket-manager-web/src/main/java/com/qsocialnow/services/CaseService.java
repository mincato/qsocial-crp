package com.qsocialnow.services;

import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface CaseService {

    PageResponse<CaseListView> findAll(int pageNumber, int pageSize);

}
