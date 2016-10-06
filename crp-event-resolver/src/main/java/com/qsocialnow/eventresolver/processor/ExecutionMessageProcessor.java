package com.qsocialnow.eventresolver.processor;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.AutomaticActionCriteria;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.elasticsearch.services.cases.CaseService;
import com.qsocialnow.eventresolver.action.Action;

@Service
public class ExecutionMessageProcessor {

    private static final Logger log = LoggerFactory.getLogger(ExecutionMessageProcessor.class);

    @Autowired
    private UpsertCaseStrategySelector upsertCaseStrategySelector;

    @Autowired
    private CaseService caseService;

    @Resource
    private Map<ActionType, Action> actions;

    public void execute(ExecutionMessageRequest request) {
        if (request != null) {
            DetectionCriteria detectionCriteria = request.getDetectionCriteria();
            if (detectionCriteria != null) {
                UpsertCaseStrategy upsertCaseStrategy = upsertCaseStrategySelector.select(request);
                Case caseObject = upsertCaseStrategy.upsert(request);

                if (caseObject != null) {
                    if (CollectionUtils.isNotEmpty(detectionCriteria.getActionCriterias())) {
                        for (AutomaticActionCriteria automaticActionCriteria : detectionCriteria.getActionCriterias()) {
                            try {
                                Action action = actions.get(automaticActionCriteria.getActionType());
                                if (action != null) {
                                    log.info(String.format("Executing action: %s",
                                            automaticActionCriteria.getActionType()));
                                    caseObject = action.execute(caseObject, automaticActionCriteria.getParameters(),
                                            request);
                                    ActionRegistry actionRegistry = createActionRegistry(automaticActionCriteria);
                                    caseObject.getActionsRegistry().add(actionRegistry);
                                } else {
                                    log.warn(String.format("There is no implementation action for: %s",
                                            automaticActionCriteria.getActionType()));
                                }
                            } catch (Exception e) {
                                log.error(String.format("There was an error executing action: %s",
                                        automaticActionCriteria.getActionType()));
                            }
                        }
                    }
                    caseService.indexCaseByBulkProcess(caseObject);
                }
            }
        }
    }

    private ActionRegistry createActionRegistry(AutomaticActionCriteria automaticActionCriteria) {
        ActionRegistry actionRegistry = new ActionRegistry();
        actionRegistry.setType(automaticActionCriteria.getActionType());
        actionRegistry.setDate(new Date().getTime());
        actionRegistry.setAction(automaticActionCriteria.getActionType().name());
        if (automaticActionCriteria.getParameters() != null) {
            Object comment = automaticActionCriteria.getParameters().get(ActionParameter.COMMENT);
            if (comment != null) {
                actionRegistry.setComment((String) comment);
            }
        }
        return actionRegistry;
    }

    public void setActions(Map<ActionType, Action> actions) {
        this.actions = actions;
    }

    public void setUpsertCaseStrategySelector(UpsertCaseStrategySelector upsertCaseStrategySelector) {
        this.upsertCaseStrategySelector = upsertCaseStrategySelector;
    }

}
