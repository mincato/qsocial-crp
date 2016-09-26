package com.qsocialnow.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.common.model.config.CaseCategorySetListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.persistence.CaseCategorySetRepository;

@Service
public class CaseCategorySetService {

    private static final Logger log = LoggerFactory.getLogger(CaseCategorySetService.class);

    @Autowired
    private CaseCategorySetRepository caseCategorySetRepository;

    public PageResponse<CaseCategorySetListView> findAll(Integer pageNumber, Integer pageSize, String name) {
        List<CaseCategorySetListView> caseCategorySets = caseCategorySetRepository.findAll(new PageRequest(pageNumber,
                pageSize), name);

        PageResponse<CaseCategorySetListView> page = new PageResponse<CaseCategorySetListView>(caseCategorySets,
                pageNumber, pageSize);
        return page;
    }

    public CaseCategorySet findOne(String caseCategorySetId) {
        CaseCategorySet caseCategorySet = caseCategorySetRepository.findOne(caseCategorySetId);
        return caseCategorySet;
    }

    public CaseCategorySet createCaseCategorySet(CaseCategorySet caseCategorySet) {
        CaseCategorySet caseCategorySetSaved = null;
        try {
            caseCategorySetSaved = caseCategorySetRepository.save(caseCategorySet);
            if (caseCategorySetSaved.getId() == null) {
                throw new Exception("There was an error creating CaseCategorySet: " + caseCategorySet.getDescription());
            }
        } catch (Exception e) {
            log.error("There was an error creating CaseCategorySet: " + caseCategorySet.getDescription(), e);
            throw new RuntimeException(e.getMessage());
        }
        return caseCategorySetSaved;
    }

    public CaseCategorySet update(String caseCategorySetId, CaseCategorySet caseCategorySet) {
        CaseCategorySet caseCategorySetSaved = null;
        try {
            caseCategorySet.setId(caseCategorySetId);
            caseCategorySetSaved = caseCategorySetRepository.update(caseCategorySet);
        } catch (Exception e) {
            log.error("There was an error updating CaseCategorySet: " + caseCategorySet.getDescription(), e);
            throw new RuntimeException(e.getMessage());
        }
        return caseCategorySetSaved;
    }

}
