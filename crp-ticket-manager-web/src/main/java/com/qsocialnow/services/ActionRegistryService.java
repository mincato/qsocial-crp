package com.qsocialnow.services;

import com.qsocialnow.common.model.cases.RegistryListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface ActionRegistryService {

    PageResponse<RegistryListView> findCaseWithRegistries(int activePage, int pageSize, String caseId);

}
