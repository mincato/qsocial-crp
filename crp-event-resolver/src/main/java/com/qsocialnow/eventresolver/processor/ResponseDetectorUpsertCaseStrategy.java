package com.qsocialnow.eventresolver.processor;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.cases.Case;
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
        log.info("Processing upsert for response detector event");
        Case originCase = null;
        Event input = request.getInput();
        if (input.getOriginIdCase() != null) {
            log.info("Trying to merge case: " + input.getOriginIdCase());
            originCase = caseService.findCaseById(input.getOriginIdCase());
            boolean isDuplicated = originCase.getMessages().stream()
                    .anyMatch(message -> message.getId().equals(input.getId()));
            if (isDuplicated) {
                log.info("The event is duplicated");
            } else {
                originCase = mergeAction.mergeCase(input, originCase);
            }
        } else {
            log.info("Trying to merge case by event: " + input.getOriginIdCase());
            Case mergeCase = findCaseToMerge(request);
            if (mergeCase != null) {
                log.info("merging case");
                originCase = mergeAction.mergeCase(input, mergeCase);
            }
        }
        return null;
    }

    private Case findCaseToMerge(ExecutionMessageRequest request) {
        Case caseToMerge = null;
        List<Case> openCases = caseService.findOpenCasesForSubject(request.getInput().getIdUsuarioOriginal());
        if (CollectionUtils.isNotEmpty(openCases)) {
            boolean isDuplicated = openCases.stream().anyMatch(
                    caseObject -> {
                        return caseObject.getMessages().stream()
                                .anyMatch(message -> message.getId().equals(request.getInput().getId()));
                    });
            if (!isDuplicated) {
                caseToMerge = openCases.get(0);
            }
        }
        return caseToMerge;
    }

}
