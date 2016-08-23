package com.qsocialnow.elasticsearch.configuration;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.elasticsearch.services.cases.CaseService;

public class ProducerTask extends TimerTask {

    private CaseService caseService;

    private static final AtomicInteger itemCount = new AtomicInteger(0);

    private QueueConfigurator configuration = new QueueConfigurator();

    public ProducerTask() {
        this.caseService = new CaseService();
    }

    @Override
    public void run() {

        System.out.println("Creating case");
        Case caseDocument = new Case();
        caseDocument.setTitle("Testing cases");
        caseDocument.setDescription("Case: " + itemCount.incrementAndGet());
        caseDocument.setOpen(true);
        caseDocument.setOpenDate(new Date());

        caseService.indexCaseByBulkProcess(configuration, caseDocument);
    }

}
