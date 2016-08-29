package com.qsocialnow.services;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface DomainService {

    Domain create(Domain currentDomain);

    Domain findOne(String domain);

    Domain update(Domain currentDomain);

    PageResponse<DomainListView> findAll(int pageNumber, int pageSize);

    PageResponse<DomainListView> findAllByName(int pageNumber, int pageSize, String name);

    void createTrigger(String domain, Trigger trigger);

}
