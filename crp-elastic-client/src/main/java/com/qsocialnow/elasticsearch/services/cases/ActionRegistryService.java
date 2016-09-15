package com.qsocialnow.elasticsearch.services.cases;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.cases.ActionRegistryMapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.ActionRegistryType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class ActionRegistryService extends DynamicIndexService{

	private static final Logger log = LoggerFactory.getLogger(ActionRegistryService.class);
	
    private static AWSElasticsearchConfigurationProvider elasticSearchCaseConfigurator;

    public ActionRegistryService(AWSElasticsearchConfigurationProvider configurationProvider) {
        elasticSearchCaseConfigurator = configurationProvider;
    }

    public String indexRegistry(String idCase, ActionRegistry document) {

        RepositoryFactory<ActionRegistryType> esfactory = new RepositoryFactory<ActionRegistryType>(
                elasticSearchCaseConfigurator);
        Repository<ActionRegistryType> repository = esfactory.initManager();
        repository.initClient();

        ActionRegistryMapping mapping = ActionRegistryMapping.getInstance();
        mapping.setIndex(this.getIndex(repository));

        // index document
        ActionRegistryType documentIndexed = mapping.getDocumentType(document);
        documentIndexed.setIdCase(idCase);
        String response = repository.indexMapping(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public List<ActionRegistry> findRegistries(int from, int size, String caseId) {

        RepositoryFactory<ActionRegistryType> esfactory = new RepositoryFactory<ActionRegistryType>(
                elasticSearchCaseConfigurator);
        Repository<ActionRegistryType> repository = esfactory.initManager();
        repository.initClient();

        ActionRegistryMapping mapping = ActionRegistryMapping.getInstance();
        mapping.setIndex(this.getQueryIndex());
        
        log.info("Retriving registries from case: " + caseId);
        SearchResponse<ActionRegistry> response = repository.queryByField(mapping, from, size, "action", "idCase",
                caseId);

        List<ActionRegistry> registries = response.getSources();

        repository.closeClient();
        return registries;
    }

    public List<ActionRegistry> findRegistriesByText(int from, int size, String caseId,String textValue) {

        RepositoryFactory<ActionRegistryType> esfactory = new RepositoryFactory<ActionRegistryType>(
                elasticSearchCaseConfigurator);
        Repository<ActionRegistryType> repository = esfactory.initManager();
        repository.initClient();

        ActionRegistryMapping mapping = ActionRegistryMapping.getInstance();
        mapping.setIndex(this.getQueryIndex());

        log.info("Retriving registries from case: " + caseId +" - text:"+textValue);
        SearchResponse<ActionRegistry> response = repository.queryByField(mapping, from, size, "action", "idCase",
                caseId);

        List<ActionRegistry> registries = response.getSources();

        repository.closeClient();
        return registries;
    }    
}
