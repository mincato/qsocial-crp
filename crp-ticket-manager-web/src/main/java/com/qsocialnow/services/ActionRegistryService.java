package com.qsocialnow.services;

import java.util.Date;

import com.qsocialnow.common.model.cases.RegistryListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface ActionRegistryService {

    PageResponse<RegistryListView> findRegistries(int activePage, int pageSize, String caseId);

    PageResponse<RegistryListView> findRegistriesBy(int activePage, int pageSize, String caseId, String textValue,
            String action, String user, Date fromDate, Date toDate);

}
