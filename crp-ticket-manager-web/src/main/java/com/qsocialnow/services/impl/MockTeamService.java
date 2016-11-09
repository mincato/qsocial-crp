package com.qsocialnow.services.impl;

import static com.qsocialnow.pagination.PaginationConstants.ACTIVE_PAGE_DEFAULT;
import static com.qsocialnow.pagination.PaginationConstants.PAGE_SIZE_DEFAULT;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.config.BaseUserResolver;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.TeamListView;
import com.qsocialnow.common.model.config.User;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.TeamService;

@Service("mockTeamService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MockTeamService implements TeamService {

    @Override
    public Team create(Team currentTeam) {
        return currentTeam;
    }

    @Override
    public Team findOne(String teamId) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/teams.json");

        @SuppressWarnings("serial")
        Type listType = new TypeToken<ArrayList<Team>>() {
        }.getType();

        List<Team> list = gsonBuilder.create().fromJson(new InputStreamReader(systemResourceAsStream), listType);
        return list.get(0);
    }

    @Override
    public Team update(Team currentTeam) {
        return currentTeam;
    }

    @Override
    public PageResponse<TeamListView> findAll(int pageNumber, int pageSize, Map<String, String> filters) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/teams.json");

        @SuppressWarnings("serial")
        Type listType = new TypeToken<ArrayList<TeamListView>>() {
        }.getType();

        List<TeamListView> list = gsonBuilder.create()
                .fromJson(new InputStreamReader(systemResourceAsStream), listType);

        return new PageResponse<TeamListView>(list, ACTIVE_PAGE_DEFAULT, PAGE_SIZE_DEFAULT);
    }

    @Override
    public List<TeamListView> findAll() {
        return null;
    }

    @Override
    public List<BaseUserResolver> findUserResolvers(String teamId, Map<String, Object> filters) {
        return null;
    }

    @Override
    public List<User> findUsers(String teamId) {
        return null;
    }

    @Override
    public List<String> findAllActiveIdsByTeam(String teamId) {
        return new ArrayList<>();
    }

}