package com.qsocialnow.elasticsearch.services.cases;

import java.time.LocalDateTime;
import java.util.List;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.elasticsearch.configuration.CaseConfigurator;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.cases.ActionRegistryMapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.ActionRegistryType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class RegistryService {

    private final static String INDEX_NAME = "registry_";
    
    private static AWSElasticsearchConfigurationProvider elasticSearchCaseConfigurator;
    
    public RegistryService (AWSElasticsearchConfigurationProvider configurationProvider){
    	elasticSearchCaseConfigurator = configurationProvider;
    }

    public String indexRegistry(String idCase, ActionRegistry document) {

        RepositoryFactory<ActionRegistryType> esfactory = new RepositoryFactory<ActionRegistryType>(elasticSearchCaseConfigurator);
        Repository<ActionRegistryType> repository = esfactory.initManager();
        repository.initClient();

        ActionRegistryMapping mapping = ActionRegistryMapping.getInstance();

        String indexName = INDEX_NAME + generateIndexValue();
        mapping.setIndex(indexName);

        // validete index name
        boolean isCreated = repository.validateIndex(indexName);
        // create index
        if (!isCreated) {
            repository.createIndex(mapping.getIndex());
        }
        // index document
        ActionRegistryType documentIndexed = mapping.getDocumentType(document);
        documentIndexed.setIdCase(idCase);
        String response = repository.indexMapping(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public List<ActionRegistry> getRegistries(int from, int size) {

        CaseConfigurator configurator = new CaseConfigurator();
        return getRegistries(configurator, from, size);
    }

    public List<ActionRegistry> getRegistries(AWSElasticsearchConfigurationProvider configurator, int from, int size) {

        RepositoryFactory<ActionRegistryType> esfactory = new RepositoryFactory<ActionRegistryType>(configurator);
        Repository<ActionRegistryType> repository = esfactory.initManager();
        repository.initClient();

        ActionRegistryMapping mapping = ActionRegistryMapping.getInstance();
        SearchResponse<ActionRegistry> response = repository.search(from, size, "openDate", mapping);

        List<ActionRegistry> registries = response.getSources();

        repository.closeClient();
        return registries;
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

}
