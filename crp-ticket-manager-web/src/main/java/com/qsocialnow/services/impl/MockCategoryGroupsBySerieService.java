package com.qsocialnow.services.impl;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.qsocialnow.model.CategoryGroupBySerieIdInput;
import com.qsocialnow.model.CategoryGroupsBySerieIdOuptut;
import com.qsocialnow.services.CategoryGroupsBySerieService;

@Service("mockCategoryGroupBySerieService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MockCategoryGroupsBySerieService implements CategoryGroupsBySerieService {

    @Override
    public CategoryGroupsBySerieIdOuptut conjuntosBySerieId(CategoryGroupBySerieIdInput categoryGroupBySerieIdInput) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/conjuntos.json");
        return gsonBuilder.create().fromJson(new InputStreamReader(systemResourceAsStream),
                CategoryGroupsBySerieIdOuptut.class);
    }
}
