package com.qsocialnow.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.SegmentListView;
import com.qsocialnow.common.model.config.Status;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.filter.FilterNormalizer;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.model.request.TriggerListRequest;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.persistence.CaseCategorySetRepository;
import com.qsocialnow.persistence.SubjectCategorySetRepository;
import com.qsocialnow.persistence.TriggerRepository;

@Service
public class TriggerService {

    private static final Logger log = LoggerFactory.getLogger(TriggerService.class);

    @Autowired
    private FilterNormalizer filterNormalizer;

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private CaseCategorySetRepository caseCategorySetRepository;

    @Autowired
    private SubjectCategorySetRepository subjectCategorySetRepository;

    @Autowired
    private CuratorFramework zookeeperClient;

    @Value("${app.domains.path}")
    private String domainsPath;

    public Trigger createTrigger(String domainId, Trigger trigger) {
        Trigger triggerSaved = null;
        try {
            trigger.getSegments().stream().forEach(segment -> {
                segment.getDetectionCriterias().forEach(detectionCriteria -> {
                    filterNormalizer.normalizeFilter(detectionCriteria.getFilter());
                });
            });
            triggerSaved = triggerRepository.save(domainId, trigger);
            zookeeperClient.setData().forPath(domainsPath.concat(domainId));
        } catch (Exception e) {
            log.error("There was an error creating trigger: " + trigger.getName(), e);
            throw new RuntimeException(e.getMessage());
        }
        return triggerSaved;
    }

    public PageResponse<TriggerListView> findAll(String domainId, Integer pageNumber, Integer pageSize, String name,
            String status, String fromDate, String toDate) {
        Status filterStatus = status != null ? Status.valueOf(status) : null;
        TriggerListRequest triggerListRequest = new TriggerListRequest(name, filterStatus, fromDate, toDate);
        List<TriggerListView> triggers = triggerRepository.findAll(domainId,
                new PageRequest(pageNumber, pageSize, null), triggerListRequest);

        PageResponse<TriggerListView> page = new PageResponse<TriggerListView>(triggers, pageNumber, pageSize);
        return page;
    }

    public Trigger findOne(String domainId, String triggerId) {
        Trigger trigger = triggerRepository.findWithSegments(triggerId);
        return trigger;
    }

    public Segment findSegment(String domainId, String triggerId, String segmentId) {
        Segment segment = triggerRepository.findSegment(segmentId);
        return segment;
    }

    public List<SegmentListView> findSegments(String domainId, String triggerId) {
        try {
            return triggerRepository.findSegments(triggerId);
        } catch (Exception e) {
            log.error("There was an error finding segments");
            throw new RuntimeException(e.getMessage());
        }
    }

    public Trigger update(String domainId, String triggerId, Trigger trigger) {
        Trigger triggerSaved = null;
        try {
            trigger.getSegments().stream().forEach(segment -> {
                segment.getDetectionCriterias().forEach(detectionCriteria -> {
                    filterNormalizer.normalizeFilter(detectionCriteria.getFilter());
                });
            });
            trigger.setId(triggerId);
            triggerSaved = triggerRepository.update(domainId, trigger);
            zookeeperClient.setData().forPath(domainsPath.concat(domainId));
        } catch (Exception e) {
            log.error("There was an error updating trigger: " + trigger.getDescription(), e);
            throw new RuntimeException(e.getMessage());
        }
        return triggerSaved;
    }

    public List<CaseCategorySet> findCaseCategories(String domainId, String triggerId) {
        List<CaseCategorySet> caseCategoriesSet = new ArrayList<>();
        try {
            Trigger trigger = triggerRepository.findOne(triggerId);
            if (CollectionUtils.isNotEmpty(trigger.getCaseCategoriesSetIds())) {
                caseCategoriesSet = caseCategorySetRepository.findCategoriesSets(trigger.getCaseCategoriesSetIds());
                caseCategoriesSet.stream().forEach(caseCategorySet -> {
                    caseCategorySet.setCategories(caseCategorySetRepository.findCategories(caseCategorySet.getId()));
                });
            }
        } catch (Exception e) {
            log.error("There was an error getting subject categories");
            throw new RuntimeException(e.getMessage());
        }
        return caseCategoriesSet;
    }

    public List<SubjectCategorySet> findSubjectCategories(String domainId, String triggerId) {
        List<SubjectCategorySet> subjectCategoriesSet = new ArrayList<>();
        try {
            Trigger trigger = triggerRepository.findOne(triggerId);
            if (CollectionUtils.isNotEmpty(trigger.getSubjectCategoriesSetIds())) {
                subjectCategoriesSet = subjectCategorySetRepository.findCategoriesSets(trigger
                        .getSubjectCategoriesSetIds());
                subjectCategoriesSet.stream().forEach(
                        subjectCategorySet -> {
                            subjectCategorySet.setCategories(subjectCategorySetRepository
                                    .findCategories(subjectCategorySet.getId()));
                        });
            }
        } catch (Exception e) {
            log.error("There was an error getting subject categories");
            throw new RuntimeException(e.getMessage());
        }
        return subjectCategoriesSet;
    }

}
