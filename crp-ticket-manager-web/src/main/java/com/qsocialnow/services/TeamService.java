package com.qsocialnow.services;

import java.util.List;
import java.util.Map;

import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.TeamListView;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface TeamService {

    Team create(Team currentTeam);

    Team findOne(String teamId);

    Team update(Team currentTeam);

    PageResponse<TeamListView> findAll(int pageNumber, int pageSize, Map<String, String> filters);

    List<TeamListView> findAll();

    List<BaseUserResolver> findUserResolvers(String teamId, Map<String, Object> filters);

    List<User> findUsers(String teamId);

}
