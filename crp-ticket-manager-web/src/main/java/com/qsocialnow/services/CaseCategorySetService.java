package com.qsocialnow.services;

import java.util.Map;

import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.common.model.config.CaseCategorySetListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface CaseCategorySetService {

    CaseCategorySet create(CaseCategorySet currentCaseCategorySet);

    CaseCategorySet findOne(String caseCategorySetId);

    CaseCategorySet update(CaseCategorySet currentCaseCategorySet);

    PageResponse<CaseCategorySetListView> findAll(int pageNumber, int pageSize, Map<String, String> filters);

}
