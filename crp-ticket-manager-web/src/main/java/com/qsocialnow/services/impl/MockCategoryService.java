package com.qsocialnow.services.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.config.CategoryGroup;
import com.qsocialnow.model.CategoryGroupBySerieIdInput;
import com.qsocialnow.model.CategoryGroupsBySerieIdOuptut;
import com.qsocialnow.services.CategoryService;

@Service("mockCategoryService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MockCategoryService implements CategoryService {

    @Override
    public List<CategoryGroup> findBySerieId(CategoryGroupBySerieIdInput categoryGroupInput) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/conjuntos.json");
        CategoryGroupsBySerieIdOuptut conjuntosBySerieId = gsonBuilder.create().fromJson(
                new InputStreamReader(systemResourceAsStream), CategoryGroupsBySerieIdOuptut.class);
        conjuntosBySerieId.getConjuntos().sort(new Comparator<CategoryGroup>() {

            @Override
            public int compare(CategoryGroup o1, CategoryGroup o2) {
                return o1.getNumeroDeOrden().compareTo(o2.getNumeroDeOrden());
            }

        });
        return conjuntosBySerieId.getConjuntos();
    }

}
