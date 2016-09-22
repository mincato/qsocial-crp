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
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.common.model.config.SubjectCategorySetListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.SubjectCategorySetService;

@Service("mockSubjectCategorySetService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MockSubjectCategorySetService implements SubjectCategorySetService {

    @Override
    public PageResponse<SubjectCategorySetListView> findAll(int pageNumber, int pageSize, Map<String, String> filters) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/subjectcategorysets.json");

        @SuppressWarnings("serial")
        Type listType = new TypeToken<ArrayList<SubjectCategorySetListView>>() {
        }.getType();

        List<SubjectCategorySetListView> list = gsonBuilder.create().fromJson(
                new InputStreamReader(systemResourceAsStream), listType);

        return new PageResponse<SubjectCategorySetListView>(list, ACTIVE_PAGE_DEFAULT, PAGE_SIZE_DEFAULT);
    }

    @Override
    public SubjectCategorySet create(SubjectCategorySet currentSubjectCategorySet) {
        return currentSubjectCategorySet;
    }

    @Override
    public SubjectCategorySet findOne(String subjectCategorySetId) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/subjectcategorysets.json");

        @SuppressWarnings("serial")
        Type listType = new TypeToken<ArrayList<SubjectCategorySet>>() {
        }.getType();

        List<SubjectCategorySet> list = gsonBuilder.create().fromJson(new InputStreamReader(systemResourceAsStream),
                listType);
        Optional<SubjectCategorySet> subjectCategorySetOptional = list.stream()
                .filter(subjectCategorySet -> subjectCategorySet.getId().equals(subjectCategorySetId)).findFirst();
        if (subjectCategorySetOptional.isPresent()) {
            return subjectCategorySetOptional.get();
        }
        return null;
    }

    @Override
    public SubjectCategorySet update(SubjectCategorySet currentSubjectCategorySet) {
        return currentSubjectCategorySet;
    }

}