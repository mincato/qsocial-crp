package com.qsocialnow.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.model.config.UserResolverListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.persistence.UserResolverRepository;

@Service
public class UserResolverService {

    private static final Logger log = LoggerFactory.getLogger(UserResolverService.class);

    @Autowired
    private UserResolverRepository userResolverRepository;

    public UserResolver createUserResolver(UserResolver userResolver) {
        UserResolver userResolverSaved = null;
        try {
            userResolverSaved = userResolverRepository.save(userResolver);
        } catch (Exception e) {
            log.error("There was an error creating user resolver: " + userResolver.getIdentifier(), e);
            throw new RuntimeException(e.getMessage());
        }
        return userResolverSaved;
    }

    public PageResponse<UserResolverListView> findAll(Integer pageNumber, Integer pageSize) {
        List<UserResolverListView> userResolvers = userResolverRepository
                .findAll(new PageRequest(pageNumber, pageSize));

        PageResponse<UserResolverListView> page = new PageResponse<UserResolverListView>(userResolvers, pageNumber,
                pageSize);
        return page;
    }

    public UserResolver findOne(String userResolverId) {
        UserResolver userResolver = userResolverRepository.findOne(userResolverId);
        return userResolver;
    }

}
