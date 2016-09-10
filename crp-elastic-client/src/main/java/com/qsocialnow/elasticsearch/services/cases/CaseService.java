package com.qsocialnow.elasticsearch.services.cases;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.elasticsearch.configuration.CaseConfigurator;
import com.qsocialnow.elasticsearch.configuration.ConfigurationProvider;
import com.qsocialnow.elasticsearch.configuration.QueueConfigurator;
import com.qsocialnow.elasticsearch.mappings.cases.ActionRegistryMapping;
import com.qsocialnow.elasticsearch.mappings.cases.CaseMapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.ActionRegistryType;
import com.qsocialnow.elasticsearch.mappings.types.cases.CaseType;
import com.qsocialnow.elasticsearch.queues.QueueProducer;
import com.qsocialnow.elasticsearch.queues.QueueService;
import com.qsocialnow.elasticsearch.queues.QueueServiceFactory;
import com.qsocialnow.elasticsearch.queues.QueueType;
import com.qsocialnow.elasticsearch.repositories.IndexResponse;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

import io.searchbox.core.BulkResult.BulkResultItem;

public class CaseService {

    private static final Logger log = LoggerFactory.getLogger(CaseService.class);

    private final static String INDEX_NAME = "cases_";

    private final static String INDEX_NAME_REGISTRY = "registry_";

    private static QueueService queueService;

    private static QueueProducer<Case> producer;

    private static QueueProducer<Case> failProducer;

    private static CaseConsumer consumer;

    private static CaseConsumer failConsumer;

    private static QueueConfigurator caseQueueConfigurator;

    private static ConfigurationProvider elasticSearchCaseConfigurator;

    private static final AtomicInteger failRetriesCount = new AtomicInteger(0);

    private static final int TOTAL_BULK_INDEX_RETRIES_COUNT = 5;

    public String indexCase(Case document) {
        CaseConfigurator configurator = new CaseConfigurator();
        return indexCase(configurator, document);
    }

    public String indexCase(ConfigurationProvider configurator, Case document) {

        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(configurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();

        String indexName = INDEX_NAME + generateIndexValue();
        mapping.setIndex(indexName);

        // validete index name
        boolean isCreated = repository.validateIndex(indexName);
        // create index
        if (!isCreated) {
            repository.createIndex(mapping.getIndex());
        }
        // index document
        CaseType documentIndexed = mapping.getDocumentType(document);
        String response = repository.indexMapping(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public void indexCaseByBulkProcess(QueueConfigurator queueConfigurator, ConfigurationProvider configurator,
            Case document) {

        if (caseQueueConfigurator == null)
            caseQueueConfigurator = queueConfigurator;

        if (elasticSearchCaseConfigurator == null)
            elasticSearchCaseConfigurator = configurator;

        boolean isSucceeded = addItemInQueue(document);

        if (!isSucceeded) {
            // TODO fail process to index without queue?
            log.error("Unable to create bigqueue instance to allow bulk index-cases: ");
            indexCase(configurator, document);
        }
    }

    public void indexBulkCases(List<Case> documents) {
        CaseConfigurator configurator = new CaseConfigurator();
        indexBulkCases(configurator, documents);
    }

    public void indexBulkCases(ConfigurationProvider configurator, List<Case> documents) {

        if (documents != null && documents.size() > 0) {

            RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(configurator);
            Repository<CaseType> repository = esfactory.initManager();
            repository.initClient();

            CaseMapping mapping = CaseMapping.getInstance();

            String indexName = INDEX_NAME + generateIndexValue();
            mapping.setIndex(indexName);

            // validete index name
            boolean isCreated = repository.validateIndex(indexName);
            // create index
            if (!isCreated) {
                repository.createIndex(mapping.getIndex());
            }
            // index document
            List<CaseType> documentsTypes = new ArrayList<>();
            for (Case caseDocument : documents) {
                documentsTypes.add(mapping.getDocumentType(caseDocument));
            }

            IndexResponse<Case> response = repository.bulkOperation(mapping, documentsTypes);
            repository.closeClient();

            if (response.isSucceeded()) {
                failRetriesCount.set(0);

                List<BulkResultItem> items = response.getSourcesBulk();
                List<ActionRegistryType> registries = new ArrayList<>();
                ActionRegistryMapping mappingRegistry = ActionRegistryMapping.getInstance();

                String indexNameRegistry = INDEX_NAME_REGISTRY + generateIndexValue();
                mappingRegistry.setIndex(indexNameRegistry);

                for (int i = 0; i < documents.size(); i++) {
                    if (items.get(i) != null) {
                        String idCase = items.get(i).id;
                        Case caseIndexed = documents.get(i);

                        List<ActionRegistry> caseRegistries = caseIndexed.getActionsRegistry();
                        if (caseRegistries != null) {
                            for (ActionRegistry actionRegistry : caseRegistries) {
                                ActionRegistryType documentIndexed = mappingRegistry.getDocumentType(actionRegistry);
                                documentIndexed.setIdCase(idCase);
                                registries.add(documentIndexed);
                            }
                        }
                    }
                }

                if (registries.size() > 0) {
                    RepositoryFactory<ActionRegistryType> esRegistryfactory = new RepositoryFactory<ActionRegistryType>(
                            configurator);
                    Repository<ActionRegistryType> repositoryRegistry = esRegistryfactory.initManager();
                    repositoryRegistry.initClient();
                    // validete index name
                    boolean isRegistryIndexCreated = repositoryRegistry.validateIndex(indexNameRegistry);
                    // create index
                    if (!isRegistryIndexCreated) {
                        repositoryRegistry.createIndex(mappingRegistry.getIndex());
                    }
                    repositoryRegistry.bulkOperation(mappingRegistry, registries);
                    repositoryRegistry.closeClient();
                }
            } else {// fail indexing cases
                if (failRetriesCount.get() <= TOTAL_BULK_INDEX_RETRIES_COUNT) {
                    log.info("Adding fail items into queue");
                    for (Case caseFail : documents) {
                        addFailItemInQueue(caseFail, false);
                    }
                    failRetriesCount.incrementAndGet();
                } else {
                    log.info("Adding dead items into queue");
                    for (Case caseDead : documents) {
                        addFailItemInQueue(caseDead, true);
                    }
                }
            }
        }
    }

    public List<Case> getCases(int from, int size) {
        CaseConfigurator configurator = new CaseConfigurator();
        return getCases(configurator, from, size);
    }

    public List<Case> getCases(ConfigurationProvider configurator, int from, int size) {

        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(configurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        SearchResponse<Case> response = repository.search(from, size, "openDate", mapping);

        List<Case> cases = response.getSources();

        repository.closeClient();
        return cases;
    }

    private String generateIndexValue() {
        String indexSufix = null;

        LocalDateTime dateTime = LocalDateTime.now();
        int day = dateTime.getDayOfMonth();
        int month = dateTime.getMonthValue();
        int year = dateTime.getYear();
        indexSufix = year + "_" + month + "_" + day;

        return indexSufix;
    }

    private boolean addItemInQueue(Case item) {
        boolean isQueueCreatedOK = false;
        if (queueService == null) {
            QueueServiceFactory queueServiceFactory = QueueServiceFactory.getInstance();
            queueService = queueServiceFactory.getQueueServiceInstance(QueueType.CASES, caseQueueConfigurator);
        }
        if (producer == null) {
            producer = new QueueProducer<Case>(QueueType.CASES.type());
            consumer = new CaseConsumer(QueueType.CASES.type(), elasticSearchCaseConfigurator);
            producer.addConsumer(consumer);

            queueService.startConsumer(consumer);
            queueService.startProducer(producer);
        }
        producer.addItem(item);
        isQueueCreatedOK = true;
        return isQueueCreatedOK;
    }

    private boolean addFailItemInQueue(Case item, boolean isDeadItem) {
        boolean isQueueCreatedOK = false;
        if (queueService == null) {
            QueueServiceFactory queueServiceFactory = QueueServiceFactory.getInstance();
            queueService = queueServiceFactory.getQueueServiceInstance(QueueType.CASES, caseQueueConfigurator);
        }
        if (failProducer == null) {
            failProducer = new QueueProducer<Case>(QueueType.CASES.type());
            failConsumer = new CaseConsumer(QueueType.CASES.type(), elasticSearchCaseConfigurator);
            failProducer.addConsumer(failConsumer);

            queueService.startFailConsumer(failConsumer);
            queueService.startFailProducer(failProducer);
        }
        if (!isDeadItem) {
            failProducer.addItem(item);
        } else {
            failProducer.addDeadItem(item);
        }
        isQueueCreatedOK = true;
        return isQueueCreatedOK;
    }
}
