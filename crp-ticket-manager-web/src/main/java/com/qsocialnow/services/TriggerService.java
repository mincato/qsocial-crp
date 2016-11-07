package com.qsocialnow.services;

import java.util.List;
import java.util.Map;

import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.SegmentListView;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.pagination.PageResponse;

public interface TriggerService {

    Trigger create(String domainId, Trigger trigger);

    PageResponse<TriggerListView> findAll(String domainId, int pageNumber, int pageSize, Map<String, String> filters);

    Trigger findOne(String domainId, String triggerId);

    Trigger update(String domainId, Trigger trigger);

    Segment findSegment(String domainId, String triggerId, String segmentId);

    List<SegmentListView> findSegments(String domainId, String triggerId);

    List<CaseCategorySet> findCategories(String domainId, String triggerId);

    List<CaseCategorySet> findActiveCategories(String domainId, String triggerId);

    List<SubjectCategorySet> findSubjectCategories(String domainId, String triggerId);

    List<TriggerListView> findAllActive(String domainId);
}
