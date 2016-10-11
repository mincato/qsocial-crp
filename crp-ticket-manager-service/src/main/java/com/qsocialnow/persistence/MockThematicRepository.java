package com.qsocialnow.persistence;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.model.ThematicsByClientOrganizationIdOutput;

public class MockThematicRepository implements ThematicRepository {

    @Override
    public List<Thematic> findAll() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/thematics.json");
        return gsonBuilder.create()
                .fromJson(new InputStreamReader(systemResourceAsStream), ThematicsByClientOrganizationIdOutput.class)
                .getThematics();
    }

}
