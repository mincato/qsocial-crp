package com.qsocialnow.persistence;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.config.CategoryGroup;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.model.CategoryGroupsBySerieIdOuptut;
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

    @Override
    public List<CategoryGroup> findCategoryGroupsBySerieId(Long thematicId, Long serieId) {
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
