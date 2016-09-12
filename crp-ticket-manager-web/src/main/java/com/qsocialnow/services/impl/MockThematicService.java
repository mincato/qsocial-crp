package com.qsocialnow.services.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.qsocialnow.model.Thematic;
import com.qsocialnow.model.ThematicsByClientOrganizationIdOutput;
import com.qsocialnow.services.ThematicService;

@Service("mockThematicService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MockThematicService implements ThematicService {

    @Override
    public List<Thematic> findAll() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/thematics.json");
        return gsonBuilder.create()
                .fromJson(new InputStreamReader(systemResourceAsStream), ThematicsByClientOrganizationIdOutput.class)
                .getThematics();
    }

}
