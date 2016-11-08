package com.qsocialnow.persistence;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.model.config.UserResolverListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.util.FilterConstants;
import com.qsocialnow.elasticsearch.services.config.UserResolverService;

@Service
public class UserResolverRepository {

    private static final String NEW_NODE_VALUE = "NEW";

    private Logger log = LoggerFactory.getLogger(UserResolverRepository.class);

    @Autowired
    private UserResolverService userResolverElasticService;

    @Autowired
    private CuratorFramework zookeeperClient;

    @Value("${app.twitter.users.path}")
    private String twitterUsersZnodePath;

    @Value("${app.client}")
    private String appClient;

    @Value("${app.facebook.users.path}")
    private String facebookUsersZnodePath;

    public UserResolver save(UserResolver newUserResolver) {
        try {
            String id = userResolverElasticService.indexUserResolver(newUserResolver);
            newUserResolver.setId(id);
            createNewUserResolverUserNode(newUserResolver.getIdentifier(), newUserResolver.getSource());
            return newUserResolver;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    private void createNewUserResolverUserNode(String identifier, Long sourceId) {

        try {
            if (FilterConstants.MEDIA_TWITTER.equals(sourceId)) {
                String clientTwitterUsersZnodePath = MessageFormat.format(twitterUsersZnodePath, appClient);
                zookeeperClient.create().forPath(clientTwitterUsersZnodePath + "/" + identifier,
                        NEW_NODE_VALUE.getBytes());
            }

            if (FilterConstants.MEDIA_FACEBOOK.equals(sourceId)) {
                String clientFacebookUsersZnodePath = MessageFormat.format(facebookUsersZnodePath, appClient);
                zookeeperClient.create().forPath(clientFacebookUsersZnodePath + "/" + identifier,
                        NEW_NODE_VALUE.getBytes());
            }
        } catch (Exception e) {
            log.error("Unexpected error trying to creade user resolver node to be consumed by response", e);
        }
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
                userResolverListView.setActive(userResolverRepo.isActive());
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

    public List<UserResolver> findUserResolvers(List<BaseUserResolver> userResolvers, Boolean status, Long source) {
        List<String> ids = userResolvers.stream().map(BaseUserResolver::getId).collect(Collectors.toList());
        List<UserResolver> userResolversRepo = userResolverElasticService.findByIds(ids, status, source);
        return userResolversRepo;
    }

}
