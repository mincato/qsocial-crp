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
import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.common.model.config.UserResolverListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.UserResolverService;

@Service("mockUserResolverService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MockUserResolverService implements UserResolverService {

    @Override
    public UserResolver create(UserResolver currentUserResolver) {
        return currentUserResolver;
    }

    @Override
    public UserResolver findOne(String userResolverId) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/usersresolver.json");

        @SuppressWarnings("serial")
        Type listType = new TypeToken<ArrayList<UserResolver>>() {
        }.getType();

        List<UserResolver> list = gsonBuilder.create()
                .fromJson(new InputStreamReader(systemResourceAsStream), listType);
        for (UserResolver user : list) {
            if (userResolverId.equals(user.getId())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public UserResolver update(UserResolver currentUserResolver) {
        return currentUserResolver;
    }

    @Override
    public PageResponse<UserResolverListView> findAll(int pageNumber, int pageSize, Map<String, String> filters) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/usersresolver.json");

        @SuppressWarnings("serial")
        Type listType = new TypeToken<ArrayList<UserResolverListView>>() {
        }.getType();

        List<UserResolverListView> list = gsonBuilder.create().fromJson(new InputStreamReader(systemResourceAsStream),
                listType);

        return new PageResponse<UserResolverListView>(list, ACTIVE_PAGE_DEFAULT, PAGE_SIZE_DEFAULT);
    }

    @Override
    public List<UserResolverListView> findAll(Map<String, String> filters) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/usersresolver.json");

        @SuppressWarnings("serial")
        Type listType = new TypeToken<ArrayList<UserResolverListView>>() {
        }.getType();

        return gsonBuilder.create().fromJson(new InputStreamReader(systemResourceAsStream), listType);

    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub
    }
}