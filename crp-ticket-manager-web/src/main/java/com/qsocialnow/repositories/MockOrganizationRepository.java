package com.qsocialnow.repositories;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.config.ClientOrganization;

@Repository("mockOrganizationRepository")
public class MockOrganizationRepository implements OrganizationRepository {

    @Override
    public List<ClientOrganization> findAll() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/organizations.json");

        @SuppressWarnings("serial")
        Type listType = new TypeToken<ArrayList<ClientOrganization>>() {
        }.getType();

        List<ClientOrganization> organizations = gsonBuilder.create().fromJson(
                new InputStreamReader(systemResourceAsStream), listType);

        return organizations;
    }

}