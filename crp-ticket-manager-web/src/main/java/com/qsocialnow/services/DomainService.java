package com.qsocialnow.services;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface DomainService {

    Domain create(Domain currentDomain);

    Domain findOne(String domain);

    Domain findOneWithActiveResolutions(String domain);

    Domain update(Domain currentDomain);

    PageResponse<DomainListView> findAll(int pageNumber, int pageSize);

    PageResponse<DomainListView> findAllByUserNameAllowed(String userName);

    PageResponse<DomainListView> findAllByName(int pageNumber, int pageSize, String name);

    PageResponse<DomainListView> findAll();
}
