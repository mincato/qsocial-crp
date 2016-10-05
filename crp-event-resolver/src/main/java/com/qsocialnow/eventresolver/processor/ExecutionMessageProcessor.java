package com.qsocialnow.eventresolver.processor;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.AutomaticActionCriteria;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.eventresolver.action.Action;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExecutionMessageProcessor {

    private static final Logger log = LoggerFactory.getLogger(ExecutionMessageProcessor.class);

    @Autowired
    private UpsertCaseStrategySelector upsertCaseStrategySelector;

    @Resource
    private Map<ActionType, Action> actions;

    public void execute(ExecutionMessageRequest request) {
        if (request != null) {
            DetectionCriteria detectionCriteria = request.getDetectionCriteria();
            if (detectionCriteria != null) {
                UpsertCaseStrategy upsertCaseStrategy = upsertCaseStrategySelector.select(request);
                Case caseObject = upsertCaseStrategy.upsert(request);

                if (caseObject != null) {
                    Object currentInput = request.getInput();
                    if (detectionCriteria.getActionCriterias() != null) {
                        for (AutomaticActionCriteria automaticActionCriteria : detectionCriteria.getActionCriterias()) {
                            Action action = actions.get(automaticActionCriteria.getActionType());
                            if (action != null) {
                                log.info(String.format("Executing action: %s", automaticActionCriteria.getActionType()));
                                currentInput = action.execute(currentInput, automaticActionCriteria.getParameters(),
                                        request);
                            } else {
                                log.warn(String.format("There is no implementation action for: %s",
                                        automaticActionCriteria.getActionType()));
                            }
                        }
                    }
                }
            }
        }
    }

    public void setActions(Map<ActionType, Action> actions) {
        this.actions = actions;
    }

    public void setUpsertCaseStrategySelector(UpsertCaseStrategySelector upsertCaseStrategySelector) {
        this.upsertCaseStrategySelector = upsertCaseStrategySelector;
    }

}
