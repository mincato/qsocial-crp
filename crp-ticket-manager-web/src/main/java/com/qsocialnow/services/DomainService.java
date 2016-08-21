package com.qsocialnow.services;

import com.qsocialnow.common.model.config.Domain;

public interface DomainService {

    Domain create(Domain currentDomain);

    Domain findOne(String domain);

    Domain update(Domain currentDomain);

}
