package com.qsocialnow.eventresolver.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Message;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.elasticsearch.services.cases.CaseService;

@Component("mergeCaseAction")
public class MergeCaseAction {

    @Autowired
    private CaseService caseElasticService;

    private static final Logger log = LoggerFactory.getLogger(MergeCaseAction.class);

    public Case mergeCase(Event event, Case outputElement) {
        log.info("Starting to merge case: " + outputElement.getId());

        // Adding a registry
        List<ActionRegistry> registries = new ArrayList<>();
        ActionRegistry registry = new ActionRegistry();
        registry.setAction(ActionType.MERGE_CASE.name());
        registry.setAutomatic(true);
        registry.setComment("Id: " + event.getId() + " - " + event.getTitulo());
        registry.setDate(new Date().getTime());
        registry.setEvent(event);
        registries.add(registry);
        Message message = new Message();
        message.setFromResponseDetector(event.isResponseDetected());
        message.setId(event.getId());
        outputElement.addMessage(message);
        outputElement.setLastPostId(event.getId());
        outputElement.setActionsRegistry(registries);
        outputElement.setPendingResponse(true);

        caseElasticService.indexCaseByBulkProcess(outputElement);

        return outputElement;
    }

    public void updateRegistry(Case caseObject, ActionRegistry actionRegistry, Event event) {
        actionRegistry.setEvent(event);
        List<ActionRegistry> registries = new ArrayList<>();
        registries.add(actionRegistry);
        caseObject.setActionsRegistry(registries);
        caseElasticService.indexCaseByBulkProcess(caseObject);
    }

}
