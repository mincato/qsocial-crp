package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.model.config.UserResolverListView;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.elasticsearch.services.config.UserResolverService;

@Service
public class UserResolverRepository {

    private Logger log = LoggerFactory.getLogger(UserResolverRepository.class);

    @Autowired
    private UserResolverService userResolverElasticService;

    public UserResolver save(UserResolver newUserResolver) {
        try {
            String id = userResolverElasticService.indexUserResolver(newUserResolver);
            newUserResolver.setId(id);

            return newUserResolver;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public List<UserResolverListView> findAll(PageRequest pageRequest, String identifier) {
        List<UserResolverListView> userResolvers = new ArrayList<>();

        try {
            Integer offset = pageRequest != null ? pageRequest.getOffset() : null;
            Integer limit = pageRequest != null ? pageRequest.getLimit() : null;
            List<UserResolver> userResolversRepo = userResolverElasticService.getUserResolvers(offset, limit,
                    identifier);

            for (UserResolver userResolverRepo : userResolversRepo) {
                UserResolverListView userResolverListView = new UserResolverListView();
                userResolverListView.setId(userResolverRepo.getId());
                userResolverListView.setIdentifier(userResolverRepo.getIdentifier());
                userResolverListView.setActive(userResolverRepo.getActive());
                userResolverListView.setSource(userResolverRepo.getSource());

                userResolvers.add(userResolverListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return userResolvers;
    }

    public UserResolver findOne(String userResolverId) {
        UserResolver userResolver = null;

        try {
            userResolver = userResolverElasticService.findOne(userResolverId);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return userResolver;
    }

    public UserResolver delete(String userResolverId) {
        try {
            userResolverElasticService.deleteUserResolver(userResolverId);
            UserResolver userResolver = new UserResolver();
            userResolver.setId(userResolverId);
            return userResolver;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public UserResolver update(UserResolver userResolver) {
        try {
            String id = userResolverElasticService.updateUserResolver(userResolver);
            userResolver.setId(id);
            return userResolver;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

}
