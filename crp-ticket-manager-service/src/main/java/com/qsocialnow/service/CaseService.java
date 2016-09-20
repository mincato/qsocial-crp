package com.qsocialnow.service;

import java.util.ArrayList;
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
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.persistence.ActionRegistryRepository;
import com.qsocialnow.persistence.CaseRepository;
import com.qsocialnow.persistence.TriggerRepository;
import com.qsocialnow.service.action.Action;

@Service
public class CaseService {

    private static final Logger log = LoggerFactory.getLogger(CaseService.class);

    @Autowired
    private CaseRepository repository;

    @Autowired
    private ActionRegistryRepository actionRegistryRepository;

    @Autowired
    private TriggerRepository triggerRepository;

    @Resource
    private Map<ActionType, Action> actions;

    public PageResponse<CaseListView> findAll(Integer pageNumber, Integer pageSize) {
        List<CaseListView> cases = repository.findAll(new PageRequest(pageNumber, pageSize));

        PageResponse<CaseListView> page = new PageResponse<CaseListView>(cases, pageNumber, pageSize);
        return page;
    }

    public Case findOne(String caseId) {
        try {
            Case caseObject = repository.findOne(caseId);
            return caseObject;
        } catch (Exception e) {
            log.error("There was an error executing action", e);
            throw e;
        }
    }

    public Case executeAction(String caseId, ActionRequest actionRequest) {
        try {
            Case caseObject = repository.findOne(caseId);
            if (caseObject != null) {
                Action action = actions.get(actionRequest.getActionType());
                if (action != null) {
                    boolean needsUpdate = action.execute(caseObject, actionRequest.getParameters());
                    boolean updated = needsUpdate ? repository.update(caseObject) : !needsUpdate;
                    if (!updated) {
                        log.error("There was an error trying to update the case");
                        throw new RuntimeException("There was an error trying to update the case");
                    }
                    ActionRegistry actionRegistry = createActionRegistry(actionRequest);
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
        } catch (Exception e) {
            log.error("There was an error executing action", e);
            throw e;
        }
    }

    public List<Resolution> getAvailableResolutions(String caseId) {
        List<Resolution> availableResolutions = new ArrayList<Resolution>();
        log.info("Retrieving resolution from case:" + caseId);
        Case caseObject = repository.findOne(caseId);
        log.info("Case:" + caseObject.getId());
        if (caseObject != null) {
            String triggerId = caseObject.getTriggerId();
            if (triggerId != null) {
                Trigger trigger = triggerRepository.findOne(triggerId);
                if (trigger != null) {
                    availableResolutions = trigger.getResolutions();
                }
            }
        } else {
            log.warn("The case was not found");
            throw new RuntimeException("The case was not found");
        }
        return availableResolutions;
    }

    public void setRepository(CaseRepository repository) {
        this.repository = repository;
    }

    private ActionRegistry createActionRegistry(ActionRequest actionRequest) {
        ActionRegistry actionRegistry = new ActionRegistry();
        actionRegistry.setType(actionRequest.getActionType());
        actionRegistry.setDate(new Date());
        actionRegistry.setAction(actionRequest.getActionType().name());
        if (actionRequest.getParameters() != null) {
            Object comment = actionRequest.getParameters().get(ActionParameter.COMMENT);
            if (comment != null) {
                actionRegistry.setComment((String) comment);
            }
        }
        return actionRegistry;
    }

}
