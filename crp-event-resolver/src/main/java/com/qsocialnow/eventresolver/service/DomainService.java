package com.qsocialnow.eventresolver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.eventresolver.config.CacheConfig;

@Component
public class DomainService {

    @Autowired
    private com.qsocialnow.elasticsearch.services.config.DomainService domainElasticService;

    @Cacheable(value = CacheConfig.DOMAINS_CACHE)
    public Domain findDomainWithTriggers(String domainId) {
        return domainElasticService.findDomainWithTriggers(domainId);
    }

}
