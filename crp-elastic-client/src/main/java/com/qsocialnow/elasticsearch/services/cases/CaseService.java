package com.qsocialnow.elasticsearch.services.cases;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.elasticsearch.configuration.CaseConfigurator;
import com.qsocialnow.elasticsearch.configuration.ConfigurationProvider;
import com.qsocialnow.elasticsearch.configuration.QueueConfigurator;
import com.qsocialnow.elasticsearch.mappings.cases.ActionRegistryMapping;
import com.qsocialnow.elasticsearch.mappings.cases.CaseMapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.ActionRegistryType;
import com.qsocialnow.elasticsearch.mappings.types.cases.CaseType;
import com.qsocialnow.elasticsearch.queues.Producer;
import com.qsocialnow.elasticsearch.queues.QueueService;
import com.qsocialnow.elasticsearch.queues.QueueType;
import com.qsocialnow.elasticsearch.repositories.IndexResponse;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

import io.searchbox.core.BulkResult.BulkResultItem;

public class CaseService {

    private final static String INDEX_NAME = "cases_";
    
    private final static String INDEX_NAME_REGISTRY = "registry_";

    private static Producer<Case> producer;

    private static CaseConsumer consumer;

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

    public void indexCaseByBulkProcess(QueueConfigurator queueConfigurator,ConfigurationProvider configurator, Case document) {
        QueueService queueService = QueueService.getInstance(queueConfigurator);
        if(queueService.initQueue(QueueType.CASES.type())){
	        if (producer == null) {
	            producer = new Producer<Case>();
	            consumer = new CaseConsumer(configurator);
	            producer.addConsumer(consumer);
	
	            queueService.startConsumer(consumer);
	            queueService.startProducer(producer);
	        }
	        producer.addItem(document);
        }
        else{
        	//TODO fail process to index without queue?
        	
        }
    }

    public void indexBulkCases(List<Case> documents) {
        CaseConfigurator configurator = new CaseConfigurator();
        indexBulkCases(configurator, documents);
    }

    public void indexBulkCases(ConfigurationProvider configurator, List<Case> documents) {

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

        List<BulkResultItem> items = response.getSourcesBulk();
        List<ActionRegistryType> registries = new ArrayList<>();
        ActionRegistryMapping mappingRegistry = ActionRegistryMapping.getInstance();
        
        String indexNameRegistry = INDEX_NAME_REGISTRY + generateIndexValue();
        mappingRegistry.setIndex(indexNameRegistry);
        
        for (int i = 0; i < documents.size(); i++) {
			if(items.get(i) != null){
				String idCase = items.get(i).id;
        		Case caseIndexed =  documents.get(i);
			
        		List<ActionRegistry> caseRegistries =  caseIndexed.getActionsRegistry();
        		if(caseRegistries!=null){
	        		for (ActionRegistry actionRegistry : caseRegistries) {
	        			ActionRegistryType documentIndexed = mappingRegistry.getDocumentType(actionRegistry);
	        	        documentIndexed.setIdCase(idCase);
	        	        registries.add(documentIndexed);
					}
        		}
			}
		}
        RepositoryFactory<ActionRegistryType> esRegistryfactory = new RepositoryFactory<ActionRegistryType>(configurator);
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
        int hour = dateTime.getHour();

        indexSufix = year + "_" + month + "_" + day + "_" + hour;

        return indexSufix;
    }

}
