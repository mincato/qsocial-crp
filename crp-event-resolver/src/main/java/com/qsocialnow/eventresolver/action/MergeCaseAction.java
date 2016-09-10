package com.qsocialnow.eventresolver.action;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.Event;
import com.qsocialnow.common.model.event.InPutBeanDocument;
import com.qsocialnow.elasticsearch.services.cases.CaseService;

@Component("mergeCaseAction")
public class MergeCaseAction implements Action<InPutBeanDocument, Case> {

    @Autowired
    private CaseService caseElasticService;

    private static final Logger log = LoggerFactory.getLogger(MergeCaseAction.class);

    @Override
    public Case execute(InPutBeanDocument inputElement, List<String> parameters) {
        return null;
    }

    @Override
    public Case execute(InPutBeanDocument inputElement, Case outputElement, List<String> parameters) {
        log.info("Starting to merge case...");

        // creating first registry
        List<ActionRegistry> registries = new ArrayList<>();
        ActionRegistry registry = new ActionRegistry();
        registry.setAction("Merge Event - Case");
        registry.setAutomatic(true);
        registry.setDate(inputElement.getFechaCreacion());
        registry.setUserName(inputElement.getUsuarioCreacion());
        Event event = new Event();
        event.setId(inputElement.getId());
        event.setDescription(inputElement.getName());

        registry.setEvent(event);

        registries.add(registry);
        outputElement.setActionsRegistry(registries);

        try {
            caseElasticService.indexCaseByBulkProcess(outputElement);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return outputElement;
    }

}
