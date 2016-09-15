package com.qsocialnow.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.elasticsearch.services.config.UserResolverService;

@Service
public class UserResolverRepository {

    private Logger log = LoggerFactory.getLogger(UserResolverRepository.class);

    @Autowired
    private UserResolverService userResolverElasticService;

    public UserResolver findOne(String userResolverId) {
        try {
            UserResolver userResolver = userResolverElasticService.findOne(userResolverId);

            return userResolver;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

}
