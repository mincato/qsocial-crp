package com.qsocialnow.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.common.model.config.SubjectCategorySetListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.persistence.SubjectCategorySetRepository;

@Service
public class SubjectCategorySetService {

    private static final Logger log = LoggerFactory.getLogger(SubjectCategorySetService.class);

    @Autowired
    private SubjectCategorySetRepository subjectCategorySetRepository;

    public PageResponse<SubjectCategorySetListView> findAll(Integer pageNumber, Integer pageSize, String name) {
        List<SubjectCategorySetListView> subjectCategorySets = subjectCategorySetRepository.findAll(new PageRequest(
                pageNumber, pageSize, null), name);

        PageResponse<SubjectCategorySetListView> page = new PageResponse<SubjectCategorySetListView>(
                subjectCategorySets, pageNumber, pageSize);
        return page;
    }

    public SubjectCategorySet findOne(String subjectCategorySetId) {
        SubjectCategorySet subjectCategorySet = subjectCategorySetRepository.findOne(subjectCategorySetId);
        return subjectCategorySet;
    }

    public SubjectCategorySet findOneWithActiveCategories(String subjectCategorySetId) {
        SubjectCategorySet subjectCategorySet = subjectCategorySetRepository
                .findOneWithActiveCategories(subjectCategorySetId);
        return subjectCategorySet;
    }

    public SubjectCategorySet createSubjectCategorySet(SubjectCategorySet subjectCategorySet) {
        SubjectCategorySet subjectCategorySetSaved = null;
        try {
            subjectCategorySetSaved = subjectCategorySetRepository.save(subjectCategorySet);
            if (subjectCategorySetSaved.getId() == null) {
                throw new Exception("There was an error creating SubjectCategorySet: "
                        + subjectCategorySet.getDescription());
            }
        } catch (Exception e) {
            log.error("There was an error creating SubjectCategorySet: " + subjectCategorySet.getDescription(), e);
            throw new RuntimeException(e.getMessage());
        }
        return subjectCategorySetSaved;
    }

    public SubjectCategorySet update(String subjectCategorySetId, SubjectCategorySet subjectCategorySet) {
        SubjectCategorySet subjectCategorySetSaved = null;
        try {
            subjectCategorySet.setId(subjectCategorySetId);
            subjectCategorySetSaved = subjectCategorySetRepository.update(subjectCategorySet);
        } catch (Exception e) {
            log.error("There was an error updating SubjectCategorySet: " + subjectCategorySet.getDescription(), e);
            throw new RuntimeException(e.getMessage());
        }
        return subjectCategorySetSaved;
    }

    public List<SubjectCategorySet> findByIds(String ids) {
        if (StringUtils.isBlank(ids)) {
            return new ArrayList<>();
        }
        List<String> categoriesIds = Arrays.asList(ids.split(","));
        List<SubjectCategorySet> sets = subjectCategorySetRepository.findByIds(categoriesIds);

        sets.stream().forEach(set -> {
            set.setCategories(subjectCategorySetRepository.findCategories(set.getId()));
        });

        return sets;
    }

    public List<SubjectCategorySet> findAll() {
        return subjectCategorySetRepository.findAll();
    }

    public List<SubjectCategorySet> findAllActive() {
        return subjectCategorySetRepository.findAllActive();
    }

    public List<SubjectCategory> findAllCategories() {
        return subjectCategorySetRepository.findAllCategories();
    }

}
