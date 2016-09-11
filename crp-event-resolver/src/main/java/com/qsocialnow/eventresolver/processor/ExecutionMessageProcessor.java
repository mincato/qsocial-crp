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
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.elasticsearch.services.cases.CaseService;
import com.qsocialnow.eventresolver.action.Action;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExecutionMessageProcessor {

    private static final Logger log = LoggerFactory.getLogger(ExecutionMessageProcessor.class);

    @Autowired
    private CaseService caseService;

    @Resource
    private Map<ActionType, Action> actions;

    public void execute(ExecutionMessageRequest request) {
        if (request != null) {
            InPutBeanDocument input = request.getInput();
            DetectionCriteria detectionCriteria = request.getDetectionCriteria();
            if (detectionCriteria != null) {
                if (detectionCriteria.isExecuteMergeAction()) {
                    Case originCase = null;
                    if (detectionCriteria.isFindCaseByDomain()) {
                        if (input.getOriginIdCase() != null) {
                            log.info("Trying to merge case: " + input.getOriginIdCase());
                            originCase = caseService.findCaseById(input.getOriginIdCase());
                        } else {
                            log.info("Trying to merge case by event: " + input.getOriginIdCase());
                            originCase = caseService.findCaseByEventId(input.getId());
                        }
                    } else {
                        log.info("Trying to merge case finding by triggers from Domain: " + request.getDomain().getId());
                        originCase = caseService.findCaseByTriggers(request.getDomain().getTriggers());
                    }
                    Action action = actions.get(ActionType.MERGE_CASE);
                    action.execute(input, originCase, null);
                }

                Object currentInput = request.getInput();
                if (detectionCriteria.getActionCriterias() != null) {
                    for (AutomaticActionCriteria automaticActionCriteria : detectionCriteria.getActionCriterias()) {
                        Action action = actions.get(automaticActionCriteria.getActionType());
                        if (action != null) {
                            log.info(String.format("Executing action: %s", automaticActionCriteria.getActionType()));
                            currentInput = action.execute(currentInput, automaticActionCriteria.getParameters());
                        } else {
                            log.warn(String.format("There is no implementation action for: %s",
                                    automaticActionCriteria.getActionType()));
                        }
                    }
                }
            }
        }
    }

    public void setActions(Map<ActionType, Action> actions) {
        this.actions = actions;
    }

}
