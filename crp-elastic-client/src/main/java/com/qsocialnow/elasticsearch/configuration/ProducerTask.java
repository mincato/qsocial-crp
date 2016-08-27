package com.qsocialnow.elasticsearch.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import com.qsocialnow.common.model.cases.Event;
import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.elasticsearch.services.cases.CaseService;

public class ProducerTask extends TimerTask {

    private CaseService caseService;

    private static final AtomicInteger itemCount = new AtomicInteger(0);

    private QueueConfigurator configuration = new QueueConfigurator();

    private CaseConfigurator caseConfig = new CaseConfigurator();

    public ProducerTask() {
        this.caseService = new CaseService();
    }

    @Override
    public void run() {

        System.out.println("Creating case");
        Case caseDocument = new Case();
        caseDocument.setTitle("Testing case:" + itemCount.get());
        caseDocument.setDescription("Case: " + itemCount.get());
        caseDocument.setOpen(true);
        caseDocument.setOpenDate(new Date());

        ActionRegistry registry = new ActionRegistry();
        registry.setAction("Registry case :" + itemCount.getAndIncrement());
        registry.setAutomatic(true);
        registry.setComment("Registry Comment");
        registry.setDate(new Date());
        registry.setType(ActionType.OPEN_CASE);
        Event event = new Event();
        event.setDescription("Description registry");
        event.setId("evento ID");
        event.setTopic("Topico de Evento");
        registry.setEvent(event);

        List<ActionRegistry> registries = new ArrayList<>();
        registries.add(registry);

        caseDocument.setActionsRegistry(registries);

        caseService.indexCaseByBulkProcess(configuration, caseConfig, caseDocument);
    }

}
