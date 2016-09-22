package com.qsocialnow.services.impl;

import static com.qsocialnow.pagination.PaginationConstants.ACTIVE_PAGE_DEFAULT;
import static com.qsocialnow.pagination.PaginationConstants.PAGE_SIZE_DEFAULT;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.common.model.config.CaseCategorySetListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.CaseCategorySetService;

@Service("mockCaseCategorySetService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MockCaseCategorySetService implements CaseCategorySetService {

    @Override
    public PageResponse<CaseCategorySetListView> findAll(int pageNumber, int pageSize, Map<String, String> filters) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/casecategorysets.json");

        @SuppressWarnings("serial")
        Type listType = new TypeToken<ArrayList<CaseCategorySetListView>>() {
        }.getType();

        List<CaseCategorySetListView> list = gsonBuilder.create().fromJson(
                new InputStreamReader(systemResourceAsStream), listType);

        return new PageResponse<CaseCategorySetListView>(list, ACTIVE_PAGE_DEFAULT, PAGE_SIZE_DEFAULT);
    }

    @Override
    public CaseCategorySet create(CaseCategorySet currentCaseCategorySet) {
        return currentCaseCategorySet;
    }

    @Override
    public CaseCategorySet findOne(String caseCategorySetId) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/casecategorysets.json");

        @SuppressWarnings("serial")
        Type listType = new TypeToken<ArrayList<CaseCategorySet>>() {
        }.getType();

        List<CaseCategorySet> list = gsonBuilder.create().fromJson(new InputStreamReader(systemResourceAsStream),
                listType);
        Optional<CaseCategorySet> caseCategorySetOptional = list.stream()
                .filter(caseCategorySet -> caseCategorySet.getId().equals(caseCategorySetId)).findFirst();
        if (caseCategorySetOptional.isPresent()) {
            return caseCategorySetOptional.get();
        }
        return null;
    }

    @Override
    public CaseCategorySet update(CaseCategorySet currentCaseCategorySet) {
        return currentCaseCategorySet;
    }

}