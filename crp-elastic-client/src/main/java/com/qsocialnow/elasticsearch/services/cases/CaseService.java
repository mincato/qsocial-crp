package com.qsocialnow.elasticsearch.services.cases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.configuration.QueueConfigurator;
import com.qsocialnow.elasticsearch.mappings.cases.ActionRegistryMapping;
import com.qsocialnow.elasticsearch.mappings.cases.CaseMapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.ActionRegistryType;
import com.qsocialnow.elasticsearch.mappings.types.cases.CaseType;
import com.qsocialnow.elasticsearch.mappings.types.cases.IdentityType;
import com.qsocialnow.elasticsearch.queues.QueueProducer;
import com.qsocialnow.elasticsearch.queues.QueueService;
import com.qsocialnow.elasticsearch.queues.QueueServiceFactory;
import com.qsocialnow.elasticsearch.queues.QueueType;
import com.qsocialnow.elasticsearch.repositories.IndexResponse;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

import io.searchbox.core.BulkResult.BulkResultItem;

public class CaseService extends CaseIndexService {

    private static final Logger log = LoggerFactory.getLogger(CaseService.class);

    private static QueueService queueService;

    private static QueueProducer<Case> producer;

    private static QueueProducer<Case> failProducer;

    private static CaseConsumer consumer;

    private static CaseConsumer failConsumer;

    private static QueueConfigurator caseQueueConfigurator;

    private static final AtomicInteger failRetriesCount = new AtomicInteger(0);

    private static final int TOTAL_BULK_INDEX_RETRIES_COUNT = 5;

    public CaseService(QueueConfigurator queueConfigurator, AWSElasticsearchConfigurationProvider configurationProvider) {
        super(configurationProvider);
        caseQueueConfigurator = queueConfigurator;
        initQueues();
        initIndex();
    }

    private void initQueues() {
        QueueServiceFactory queueServiceFactory = QueueServiceFactory.getInstance();
        queueService = queueServiceFactory.getQueueServiceInstance(QueueType.CASES, caseQueueConfigurator);

        if (queueService != null) {
            producer = new QueueProducer<Case>(QueueType.CASES.type());
            consumer = new CaseConsumer(QueueType.CASES.type(), this);
            producer.addConsumer(consumer);

            queueService.startProducerConsumer(producer, consumer);

            failProducer = new QueueProducer<Case>(QueueType.CASES.type());
            failConsumer = new CaseConsumer(QueueType.CASES.type(), this);
            failProducer.addConsumer(failConsumer);

            queueService.startFailProducerConsumer(failProducer, failConsumer);
        }
    }

    public String indexCase(Case document) {

        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();

        String indexName = this.getIndex(repository);
        mapping.setIndex(indexName);

        // validete index name
        boolean isCreated = repository.validateIndex(indexName);
        // create index
        if (!isCreated) {
            repository.createIndex(mapping.getIndex());
        }
        // index document
        document.setLastModifiedTimestamp(new Date().getTime());
        CaseType documentIndexed = mapping.getDocumentType(document);
        String response = repository.indexMapping(mapping, documentIndexed);

        repository.closeClient();
        return response;
    }

    public void indexCaseByBulkProcess(Case document) {
        boolean isSucceeded = addItemInQueue(document);
        if (!isSucceeded) {
            // TODO fail process to index without queue?
            log.error("Unable to create bigqueue instance to allow bulk index-cases: ");
            indexCase(document);
        }
    }

    public void indexBulkCases(List<Case> documents) {

        if (documents != null && documents.size() > 0) {

            RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
            Repository<CaseType> repository = esfactory.initManager();
            repository.initClient();

            CaseMapping mapping = CaseMapping.getInstance();

            String indexName = this.getIndex(repository);
            mapping.setIndex(indexName);

            // index document
            List<IdentityType> documentsTypes = new ArrayList<>();
            for (Case caseDocument : documents) {
                caseDocument.setLastModifiedTimestamp(new Date().getTime());
                documentsTypes.add(mapping.getDocumentType(caseDocument));
            }

            IndexResponse<Case> response = repository.bulkOperation(mapping, documentsTypes);
            repository.closeClient();

            if (response.isSucceeded()) {
                failRetriesCount.set(0);

                List<BulkResultItem> items = response.getSourcesBulk();
                List<IdentityType> registries = new ArrayList<>();
                ActionRegistryMapping mappingRegistry = ActionRegistryMapping.getInstance();
                mappingRegistry.setIndex(indexName);

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
                            elasticSearchCaseConfigurator);
                    Repository<ActionRegistryType> repositoryRegistry = esRegistryfactory.initManager();
                    repositoryRegistry.initClient();
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

    public Case findCaseById(String originIdCase) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        mapping.setIndex(this.getQueryIndex());
        SearchResponse<Case> response = repository.findByAlias(originIdCase, mapping);

        Case caseDocument = response.getSource();
        log.info("Retrieving from ES Case:" + caseDocument.getId());
        repository.closeClient();
        return caseDocument;
    }

    private boolean addItemInQueue(Case item) {
        boolean isQueueCreatedOK = false;
        if (producer != null) {
            producer.addItem(item);
            isQueueCreatedOK = true;
        }
        return isQueueCreatedOK;
    }

    private boolean addFailItemInQueue(Case item, boolean isDeadItem) {
        boolean isQueueCreatedOK = false;
        if (failProducer == null) {
            if (!isDeadItem) {
                failProducer.addItem(item);
            } else {
                failProducer.addDeadItem(item);
            }
            isQueueCreatedOK = true;
        }
        return isQueueCreatedOK;
    }

    public List<Case> findOpenCasesForSubject(String idSubject) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        mapping.setIndex(this.getQueryIndex());

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.matchQuery("open", true));
        if (idSubject != null) {
            filters = filters.must(QueryBuilders.matchQuery("subject.sourceId", idSubject));
        }

        SearchResponse<Case> response = repository.searchWithFilters(null, null, "lastModifiedTimestamp", filters,
                mapping);

        List<Case> cases = response.getSources();

        repository.closeClient();
        return cases;
    }

    public List<Case> findOpenCasesForSubjectByDomain(String idSubject, String domainId) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        mapping.setIndex(this.getQueryIndex());

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.matchQuery("open", true));
        if (idSubject != null) {
            filters = filters.must(QueryBuilders.matchQuery("subject.sourceId", idSubject));
        }
        if (domainId != null) {
            filters = filters.must(QueryBuilders.matchQuery("domainId", domainId));
        }

        SearchResponse<Case> response = repository.searchWithFilters(null, null, "lastModifiedTimestamp", filters,
                mapping);

        List<Case> cases = response.getSources();

        repository.closeClient();
        return cases;
    }

    public List<Case> findCasesByMessageIdByDomain(String messageId, String domainId) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        mapping.setIndex(this.getQueryIndex());

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        if (messageId != null) {
            filters = filters.must(QueryBuilders.matchQuery("messages.id", messageId));
        }
        if (domainId != null) {
            filters = filters.must(QueryBuilders.matchQuery("domainId", domainId));
        }

        SearchResponse<Case> response = repository.searchWithFilters(null, null, "lastModifiedTimestamp", filters,
                mapping);

        List<Case> cases = response.getSources();

        repository.closeClient();
        return cases;
    }

}