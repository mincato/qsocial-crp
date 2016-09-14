package com.qsocialnow.eventresolver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.eventresolver.config.CacheConfig;
import com.qsocialnow.eventresolver.processor.MessageProcessor;

@Component
public class DomainService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);

    @Autowired
    private com.qsocialnow.elasticsearch.services.config.DomainService domainElasticService;

    @Cacheable(value = CacheConfig.DOMAINS_CACHE)
    public Domain findDomainWithTriggers(String domainId) {
    	
    	if (LOGGER.isInfoEnabled()) {
    		LOGGER.info("======== Recovering Domain from Configuration Storage ========");
    	}
    	
        return domainElasticService.findDomainWithTriggers(domainId);
    }

}
