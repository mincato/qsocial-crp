package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;
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
            List<SubjectCategorySet> subjectCategorySetsRepo = subjectCategorySetElasticService.findAll(offset, limit,
                    name);

            for (SubjectCategorySet subjectCategorySet : subjectCategorySetsRepo) {
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

    public SubjectCategorySet save(SubjectCategorySet subjectCategorySet) {
        try {

            List<SubjectCategory> subjectCategories = subjectCategorySet.getCategories();

            SubjectCategorySet newSubjectCategorySet = new SubjectCategorySet();
            newSubjectCategorySet.setDescription(subjectCategorySet.getDescription());
            newSubjectCategorySet = subjectCategorySetElasticService.indexSubjectCategorySet(newSubjectCategorySet,
                    subjectCategories);
            return newSubjectCategorySet;

        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public SubjectCategorySet findOne(String subjectCategorySetId) {
        SubjectCategorySet subjectCategorySet = null;

        try {
            subjectCategorySet = subjectCategorySetElasticService.findOne(subjectCategorySetId);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return subjectCategorySet;
    }

    public SubjectCategorySet update(SubjectCategorySet subjectCategorySet) {
        try {
            String id = subjectCategorySetElasticService.updateSubjectCategorySet(subjectCategorySet);
            subjectCategorySet.setId(id);
            return subjectCategorySet;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public List<SubjectCategorySet> findAll() {
        List<SubjectCategorySet> subjectCategorySets = new ArrayList<>();

        try {
            subjectCategorySets = subjectCategorySetElasticService.findAll(null, null, null);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return subjectCategorySets;
    }

    public List<SubjectCategory> findCategories(String subjectCategorySetId) {
        List<SubjectCategory> subjectCategories = new ArrayList<>();

        try {
            subjectCategories = subjectCategorySetElasticService.findCategories(subjectCategorySetId);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return subjectCategories;
    }

    public List<SubjectCategorySet> findCategoriesSets(List<String> subjectCategoriesSetIds) {
        return subjectCategorySetElasticService.findByIds(subjectCategoriesSetIds);
    }

}
