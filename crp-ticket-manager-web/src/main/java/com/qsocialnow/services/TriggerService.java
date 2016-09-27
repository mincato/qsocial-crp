package com.qsocialnow.services;

import java.util.Map;

import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface TriggerService {

    Trigger create(String domainId, Trigger trigger);

    PageResponse<TriggerListView> findAll(String domainId, int pageNumber, int pageSize, Map<String, String> filters);

    Trigger findOne(String domainId, String triggerId);

    Trigger update(String domainId, Trigger trigger);
}