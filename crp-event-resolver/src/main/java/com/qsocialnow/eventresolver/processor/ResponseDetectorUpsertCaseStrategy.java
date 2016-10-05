package com.qsocialnow.eventresolver.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.elasticsearch.services.cases.CaseService;
import com.qsocialnow.eventresolver.action.MergeCaseAction;

@Service
public class ResponseDetectorUpsertCaseStrategy implements UpsertCaseStrategy {

    private static final Logger log = LoggerFactory.getLogger(ResponseDetectorUpsertCaseStrategy.class);

    @Autowired
    private CaseService caseService;

    @Autowired
    private MergeCaseAction mergeAction;

    @Override
    public Case upsert(ExecutionMessageRequest request) {
        Case originCase = null;
        DetectionCriteria detectionCriteria = request.getDetectionCriteria();
        Event input = request.getInput();
        if (detectionCriteria.isExecuteMergeAction()) {
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
            mergeAction.mergeCase(input, originCase);
        }
        return null;
    }

}
