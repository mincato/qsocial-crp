package com.qsocialnow.eventresolver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.eventresolver.config.CacheConfig;
import com.qsocialnow.eventresolver.processor.MessageProcessorImpl;

@Component
public class UserResolverService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessorImpl.class);

    @Autowired
    private com.qsocialnow.elasticsearch.services.config.UserResolverService userResolverElasticService;

    @Cacheable(value = CacheConfig.USER_RESOLVERS_CACHE, unless = "#result == null")
    public UserResolver findOne(String userResolverId) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("======== Recovering UserResolver from Configuration Storage ========");
        }

        return userResolverElasticService.findOne(userResolverId);
    }

}
