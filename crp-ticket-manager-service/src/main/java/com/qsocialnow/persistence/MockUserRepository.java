package com.qsocialnow.persistence;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.config.UserListView;

public class MockUserRepository implements UserRepository {

    @Override
    public List<UserListView> findAll() {
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
