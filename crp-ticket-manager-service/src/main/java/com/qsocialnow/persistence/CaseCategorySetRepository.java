package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.common.model.config.CaseCategorySetListView;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.elasticsearch.services.config.CaseCategorySetService;

@Service
public class CaseCategorySetRepository {

    private Logger log = LoggerFactory.getLogger(CaseCategorySetRepository.class);

    @Autowired
    private CaseCategorySetService caseCategorySetElasticService;

    public List<CaseCategorySetListView> findAll(PageRequest pageRequest, String name) {
        List<CaseCategorySetListView> caseCategorySets = new ArrayList<>();

        try {
            Integer offset = pageRequest != null ? pageRequest.getOffset() : null;
            Integer limit = pageRequest != null ? pageRequest.getLimit() : null;
            List<CaseCategorySet> caseCategorySetsRepo = caseCategorySetElasticService.findAll(offset, limit, name);

            for (CaseCategorySet caseCategorySet : caseCategorySetsRepo) {
                CaseCategorySetListView teamListView = new CaseCategorySetListView();
                teamListView.setId(caseCategorySet.getId());
                teamListView.setDescription(caseCategorySet.getDescription());
                caseCategorySets.add(teamListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return caseCategorySets;
    }

    public CaseCategorySet save(CaseCategorySet caseCategorySet) {
        try {

            List<CaseCategory> caseCategories = caseCategorySet.getCategories();

            CaseCategorySet newCaseCategorySet = new CaseCategorySet();
            newCaseCategorySet.setDescription(caseCategorySet.getDescription());
            newCaseCategorySet = caseCategorySetElasticService.indexCaseCategorySet(newCaseCategorySet, caseCategories);
            return newCaseCategorySet;

        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public CaseCategorySet findOne(String caseCategorySetId) {
        CaseCategorySet caseCategorySet = null;

        try {
            caseCategorySet = caseCategorySetElasticService.findOne(caseCategorySetId);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return caseCategorySet;
    }

    public CaseCategorySet update(CaseCategorySet caseCategorySet) {
        try {
            String id = caseCategorySetElasticService.updateCaseCategorySet(caseCategorySet);
            caseCategorySet.setId(id);
            return caseCategorySet;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }
}
