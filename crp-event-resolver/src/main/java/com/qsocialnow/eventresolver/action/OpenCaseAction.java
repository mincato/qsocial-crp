package com.qsocialnow.eventresolver.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.elasticsearch.services.cases.CaseService;
import com.qsocialnow.eventresolver.processor.ExecutionMessageRequest;

@Component("openCaseAction")
public class OpenCaseAction implements Action<InPutBeanDocument, Case> {

    @Autowired
    private CaseService caseElasticService;

    private static final Logger log = LoggerFactory.getLogger(OpenCaseAction.class);

    @Override
    public Case execute(InPutBeanDocument inputElement, List<String> parameters, ExecutionMessageRequest request) {
        log.info("Executing action: " + ActionType.OPEN_CASE.name());
        Case newCase = Case.getNewCaseFromEvent(inputElement);
        newCase.setTeamId(request.getSegment().getTeam().getId());
        try {
            caseElasticService.indexCaseByBulkProcess(newCase);

        } catch (Exception e) {
            log.error("There was an error executing action", e);
        }
        return newCase;
    }

    @Override
    public Case execute(InPutBeanDocument inputElement, Case outputElement, List<String> parameters) {
        // TODO Auto-generated method stub
        return null;
    }

}
