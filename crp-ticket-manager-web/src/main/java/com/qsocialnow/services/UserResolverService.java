package com.qsocialnow.services;

import java.util.List;
import java.util.Map;

import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.model.config.UserResolverListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface UserResolverService {

    UserResolver create(UserResolver currentUserResolver);

    UserResolver findOne(String userResolverId);

    UserResolver update(UserResolver currentUserResolver);

    PageResponse<UserResolverListView> findAll(int pageNumber, int pageSize, Map<String, String> filters);

    List<UserResolverListView> findAll(Map<String, String> filters);

    void delete(String id);

    List<UserResolver> findByTeam(String teamId);

}
