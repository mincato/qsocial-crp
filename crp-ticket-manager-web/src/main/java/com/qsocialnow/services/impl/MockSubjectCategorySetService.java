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
import com.qsocialnow.common.model.config.Subject;
import com.qsocialnow.common.model.config.SubjectCategorySetListView;
import com.qsocialnow.common.model.pagination.PageResponse;

@Service("mockSubjectCategorySetService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MockSubjectCategorySetService {

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

    public Subject create(Subject currentSubjectCategorySet) {
        return currentSubjectCategorySet;
    }

    public Subject findOne(String subjectCategorySetId) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/subjectcategorysets.json");

        @SuppressWarnings("serial")
        Type listType = new TypeToken<ArrayList<Subject>>() {
        }.getType();

        List<Subject> list = gsonBuilder.create().fromJson(new InputStreamReader(systemResourceAsStream),
                listType);
        Optional<Subject> subjectCategorySetOptional = list.stream()
                .filter(subjectCategorySet -> subjectCategorySet.getId().equals(subjectCategorySetId)).findFirst();
        if (subjectCategorySetOptional.isPresent()) {
            return subjectCategorySetOptional.get();
        }
        return null;
    }

    public Subject update(Subject currentSubjectCategorySet) {
        return currentSubjectCategorySet;
    }
}