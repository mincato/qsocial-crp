package com.qsocialnow.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.cases.RegistryListView;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.persistence.ActionRegistryRepository;
import com.qsocialnow.persistence.CaseRepository;
import com.qsocialnow.service.action.Action;

@Service
public class CaseService {

    private static final Logger log = LoggerFactory.getLogger(CaseService.class);

    @Autowired
    private CaseRepository repository;

    @Autowired
    private ActionRegistryRepository actionRegistryRepository;

    @Resource
    private Map<ActionType, Action> actions;

    public PageResponse<CaseListView> findAll(Integer pageNumber, Integer pageSize) {
        List<CaseListView> cases = repository.findAll(new PageRequest(pageNumber, pageSize));

        PageResponse<CaseListView> page = new PageResponse<CaseListView>(cases, pageNumber, pageSize);
        return page;
    }

    public PageResponse<RegistryListView> findOne(String caseId, Integer pageNumber, Integer pageSize) {
        return null;
    }

    public Case executeAction(String caseId, ActionRequest actionRequest) {
        Case caseObject = repository.findOne(caseId);
        if (caseObject != null) {
            Action action = actions.get(actionRequest.getActionType());
            if (action != null) {
                ActionRegistry actionRegistry = action.execute(caseObject, actionRequest.getParameters());
                boolean updated = repository.update(caseObject);
                if (!updated) {
                    log.error("There was an error trying to update the case");
                    throw new RuntimeException("There was an error trying to update the case");
                }
                actionRegistryRepository.create(caseId, actionRegistry);
            } else {
                log.warn("The action does not exist");
                throw new RuntimeException("The action does not exist");
            }
        } else {
            log.warn("The case was not found");
            throw new RuntimeException("The case was not found");
        }
        return caseObject;
    }

    public void setRepository(CaseRepository repository) {
        this.repository = repository;
    }

}
