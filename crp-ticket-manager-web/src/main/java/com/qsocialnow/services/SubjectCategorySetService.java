package com.qsocialnow.services;

import java.util.Map;

import com.qsocialnow.common.model.config.Subject;
import com.qsocialnow.common.model.config.SubjectCategorySetListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface SubjectCategorySetService {

    Subject create(Subject currentSubjectCategorySet);

    Subject findOne(String subjectCategorySetId);

    Subject update(Subject currentSubjectCategorySet);

    PageResponse<SubjectCategorySetListView> findAll(int pageNumber, int pageSize, Map<String, String> filters);

}
