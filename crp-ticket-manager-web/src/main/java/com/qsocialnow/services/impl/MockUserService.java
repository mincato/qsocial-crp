package com.qsocialnow.services.impl;

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
import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.services.UserService;

@Service("mockUserService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MockUserService implements UserService {

    @Override
    public List<UserListView> findAll(Map<String, String> filters) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/users.json");

        @SuppressWarnings("serial")
        Type listType = new TypeToken<ArrayList<UserListView>>() {
        }.getType();

        List<UserListView> list = gsonBuilder.create()
                .fromJson(new InputStreamReader(systemResourceAsStream), listType);

        return list;
    }

}