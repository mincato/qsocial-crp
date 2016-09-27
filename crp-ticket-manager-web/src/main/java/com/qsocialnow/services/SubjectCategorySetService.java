package com.qsocialnow.services;

import java.util.Map;

import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.common.model.config.SubjectCategorySetListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface SubjectCategorySetService {

    SubjectCategorySet create(SubjectCategorySet currentSubjectCategorySet);

    SubjectCategorySet findOne(String subjectCategorySetId);

    SubjectCategorySet update(SubjectCategorySet currentSubjectCategorySet);

    PageResponse<SubjectCategorySetListView> findAll(int pageNumber, int pageSize, Map<String, String> filters);

}
