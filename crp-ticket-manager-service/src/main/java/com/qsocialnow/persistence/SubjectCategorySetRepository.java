package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.Subject;
import com.qsocialnow.common.model.config.SubjectCategorySetListView;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.elasticsearch.services.config.SubjectCategorySetService;

@Service
public class SubjectCategorySetRepository {

    private Logger log = LoggerFactory.getLogger(SubjectCategorySetRepository.class);

    @Autowired
    private SubjectCategorySetService subjectCategorySetElasticService;

    public List<SubjectCategorySetListView> findAll(PageRequest pageRequest, String name) {
        List<SubjectCategorySetListView> subjectCategorySets = new ArrayList<>();

        try {
            Integer offset = pageRequest != null ? pageRequest.getOffset() : null;
            Integer limit = pageRequest != null ? pageRequest.getLimit() : null;
            List<Subject> subjectCategorySetsRepo = subjectCategorySetElasticService.findAll(offset, limit,
                    name);

            for (Subject subjectCategorySet : subjectCategorySetsRepo) {
                SubjectCategorySetListView subjectCategorySetListView = new SubjectCategorySetListView();
                subjectCategorySetListView.setId(subjectCategorySet.getId());
                subjectCategorySetListView.setDescription(subjectCategorySet.getDescription());
                subjectCategorySets.add(subjectCategorySetListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return subjectCategorySets;
    }

    public Subject save(Subject subjectCategorySet) {
        try {

            List<SubjectCategory> subjectCategories = subjectCategorySet.getCategories();

            Subject newSubjectCategorySet = new Subject();
            newSubjectCategorySet.setDescription(subjectCategorySet.getDescription());
            newSubjectCategorySet = subjectCategorySetElasticService.indexSubjectCategorySet(newSubjectCategorySet,
                    subjectCategories);
            return newSubjectCategorySet;

        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public Subject findOne(String subjectCategorySetId) {
        Subject subjectCategorySet = null;

        try {
            subjectCategorySet = subjectCategorySetElasticService.findOne(subjectCategorySetId);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return subjectCategorySet;
    }

    public Subject update(Subject subjectCategorySet) {
        try {
            String id = subjectCategorySetElasticService.updateSubjectCategorySet(subjectCategorySet);
            subjectCategorySet.setId(id);
            return subjectCategorySet;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }
}
