package com.qsocialnow.elasticsearch.services.cases;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.queues.QueueConsumer;

public class CaseConsumer extends QueueConsumer<Case> {

    private static final Logger log = LoggerFactory.getLogger(CaseConsumer.class);

    private List<Case> bulkDocuments = new ArrayList<Case>();

    private CaseService service;

    public CaseConsumer(String type, CaseService caseService) {
        super(type);
        this.service = caseService;
    }

    @Override
    public void process(Case caseDoc) {
        synchronized (bulkDocuments) {
            log.info("Adding to index bulk case:" + caseDoc.getTitle());
            bulkDocuments.add(caseDoc);
        }
    }

    @Override
    public void save() {
        synchronized (bulkDocuments) {
            if (bulkDocuments.size() > 0) {
                log.info("CaseConsumer start to perform a bulk index");
                service.indexBulkCases(bulkDocuments);
                bulkDocuments.clear();
            }
        }
    }

}
