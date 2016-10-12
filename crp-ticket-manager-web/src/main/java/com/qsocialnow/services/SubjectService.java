package com.qsocialnow.services;

import java.util.Map;

import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.cases.SubjectListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface SubjectService {

    Subject create(Subject currentSubject);

    Subject findOne(String subjectId);

    Subject update(Subject currentSubject);

    PageResponse<SubjectListView> findAll(int pageNumber, int pageSize, Map<String, String> filters);

}
