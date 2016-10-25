package com.qsocialnow.service;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.model.config.UserResolverListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.persistence.UserResolverRepository;

@Service
public class UserResolverService {

    private static final Logger log = LoggerFactory.getLogger(UserResolverService.class);

    @Autowired
    private UserResolverRepository userResolverRepository;

    @Autowired
    private CuratorFramework zookeeperClient;

    @Value("${app.user.resolvers.path}")
    private String userResolversPath;

    public UserResolver createUserResolver(UserResolver userResolver) {
        UserResolver userResolverSaved = null;
        try {
            userResolverSaved = userResolverRepository.save(userResolver);
            if (userResolverSaved.getId() == null) {
                throw new Exception("There was an error creating user resolver: " + userResolverSaved.getIdentifier());
            }
            zookeeperClient.setData().forPath(userResolversPath);
        } catch (Exception e) {
            log.error("There was an error creating user resolver: " + userResolver.getIdentifier(), e);
            throw new RuntimeException(e.getMessage());
        }
        return userResolverSaved;
    }

    public PageResponse<UserResolverListView> findAll(Integer pageNumber, Integer pageSize, String identifier) {
        List<UserResolverListView> userResolvers = userResolverRepository.findAll(new PageRequest(pageNumber, pageSize,
                null), identifier);

        PageResponse<UserResolverListView> page = new PageResponse<UserResolverListView>(userResolvers, pageNumber,
                pageSize);
        return page;
    }

    public List<UserResolverListView> findAll() {
        List<UserResolverListView> userResolvers = userResolverRepository.findAll(null, null);
        return userResolvers;
    }

    public UserResolver findOne(String userResolverId) {
        UserResolver userResolver = userResolverRepository.findOne(userResolverId);
        return userResolver;
    }

    public UserResolver update(String userResolverId, UserResolver userResolver) {
        UserResolver userResolverSaved = null;
        try {
            userResolver.setId(userResolverId);
            userResolverSaved = userResolverRepository.update(userResolver);
        } catch (Exception e) {
            log.error("There was an error updating user resolver: " + userResolver.getIdentifier(), e);
            throw new RuntimeException(e.getMessage());
        }
        return userResolverSaved;
    }

    public UserResolver delete(String userResolverId) {
        try {
            return userResolverRepository.delete(userResolverId);
        } catch (Exception e) {
            log.error("There was an error deleting user resolver: " + userResolverId, e);
            throw new RuntimeException(e.getMessage());
        }
    }

}
