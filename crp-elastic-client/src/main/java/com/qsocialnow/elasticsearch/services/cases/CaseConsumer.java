package com.qsocialnow.elasticsearch.services.cases;

import java.util.ArrayList;
import java.util.List;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.elasticsearch.configuration.ConfigurationProvider;
import com.qsocialnow.elasticsearch.queues.Consumer;

public class CaseConsumer extends Consumer<Case> {

    private List<Case> bulkDocuments = new ArrayList<Case>();

    private CaseService service = new CaseService();

    private ConfigurationProvider configurator;

    public CaseConsumer(ConfigurationProvider configurator) {
        this.configurator = configurator;
    }

    @Override
    public void addDocument(Case caseDoc) {
        synchronized (bulkDocuments) {
            bulkDocuments.add(caseDoc);
        }
    }

    @Override
    public void saveDocuments() {
        synchronized (bulkDocuments) {
            service.indexBulkCases(this.configurator, bulkDocuments);
            bulkDocuments.clear();
        }
    }

}
