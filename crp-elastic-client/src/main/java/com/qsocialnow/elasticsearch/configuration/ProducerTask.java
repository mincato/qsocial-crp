package com.qsocialnow.elasticsearch.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.event.Event;
import com.qsocialnow.elasticsearch.services.cases.CaseTicketService;

public class ProducerTask extends TimerTask {

    private CaseTicketService caseService;

    private static final AtomicInteger itemCount = new AtomicInteger(0);

    private QueueConfigurator configuration = new QueueConfigurator("/tmp/bigqueue/", "centaurico/",
            "centaurico/error/");

    private AWSElasticsearchConfigurationProvider caseConfig = new CaseConfigurator();

    public ProducerTask() {
        this.caseService = new CaseTicketService(caseConfig);
    }

    @Override
    public void run() {

        Case caseDocument = new Case();
        caseDocument.setTitle("Testing case:" + itemCount.get());
        caseDocument.setDescription("Case: " + itemCount.get());
        caseDocument.setOpen(true);
        caseDocument.setOpenDate(new Date().getTime());

        ActionRegistry registry = new ActionRegistry();
        registry.setAction("Registry case :" + itemCount.getAndIncrement());
        registry.setAutomatic(true);
        registry.setComment("Registry Comment");
        registry.setDate(new Date().getTime());
        registry.setType(ActionType.OPEN_CASE);
        Event event = new Event();
        registry.setEvent(event);

        List<ActionRegistry> registries = new ArrayList<>();
        registries.add(registry);

        caseDocument.setActionsRegistry(registries);

        caseService.findCaseById("AVcur5GW8M02MOG2A4op");

        // caseService.indexCaseByBulkProcess(caseDocument);
    }

}
